import type {
  ConversionReadyMessage,
  ConversionStartedMessage,
  ExportedIcon,
  SelectionChangedMessage,
  SettingsErrorMessage,
  SettingsLoadedMessage,
  UiToMainMessage,
} from "./messages";
import { createExportError, createInternalError, createSelectionError, formatPluginError } from "./errorFormatter";
import { sanitizePluginSettings } from "./pluginSettings";

const PLUGIN_UI_SIZE = { width: 1080, height: 760, themeColors: true };
const SETTINGS_KEY = "valkyrie-export-settings";
const MAX_SELECTION_NAMES = 8;
const MAX_EXPORT_CONCURRENCY = 4;
const RELAUNCH_DATA = {
  "open-exporter": "Open exporter",
} as const;

type ActiveRun = {
  requestId: number;
  supersedeToken: {
    superseded: boolean;
  };
};

let activeRun: ActiveRun | null = null;

figma.showUI(__html__, PLUGIN_UI_SIZE);

// Send initial selection state
sendSelectionUpdate();

// Listen for selection changes
figma.on("selectionchange", () => {
  sendSelectionUpdate();
});

function sendSelectionUpdate(): void {
  const selected = figma.currentPage.selection;
  const exportable = selected.filter((node): node is SceneNode & ExportMixin => "exportAsync" in node);
  const names = exportable.slice(0, MAX_SELECTION_NAMES).map((node) => node.name);

  figma.ui.postMessage({
    type: "selection-changed",
    count: exportable.length,
    names,
  } satisfies SelectionChangedMessage);
}

figma.ui.onmessage = async (message: UiToMainMessage) => {
  if (message.type === "load-settings") {
    try {
      const savedSettings = await figma.clientStorage.getAsync(SETTINGS_KEY);
      const settings = sanitizePluginSettings(savedSettings);
      figma.ui.postMessage({
        type: "settings-loaded",
        settings,
      } satisfies SettingsLoadedMessage);
    } catch (error) {
      figma.ui.postMessage({
        type: "settings-error",
        error: formatPluginError(
          createInternalError(`Failed to load settings from client storage. ${String(error)}`),
        ),
      } satisfies SettingsErrorMessage);

      figma.ui.postMessage({
        type: "settings-loaded",
        settings: null,
      } satisfies SettingsLoadedMessage);
    }

    return;
  }

  if (message.type === "save-settings") {
    try {
      await figma.clientStorage.setAsync(SETTINGS_KEY, message.settings);
    } catch (error) {
      figma.ui.postMessage({
        type: "settings-error",
        error: formatPluginError(
          createInternalError(`Failed to save settings to client storage. ${String(error)}`),
        ),
      } satisfies SettingsErrorMessage);
    }
    return;
  }

  if (message.type !== "run-conversion") {
    return;
  }

  const requestId = message.requestId;
  if (activeRun && activeRun.requestId !== requestId) {
    activeRun.supersedeToken.superseded = true;
  }

  const supersedeToken: ActiveRun["supersedeToken"] = {
    superseded: false,
  };
  activeRun = { requestId, supersedeToken };

  try {
    const selected = figma.currentPage.selection;
    const exportableNodes = selected.filter((node): node is SceneNode & ExportMixin => "exportAsync" in node);

    figma.ui.postMessage({
      type: "conversion-started",
      requestId,
      selectedCount: exportableNodes.length,
    } satisfies ConversionStartedMessage);

    if (exportableNodes.length === 0) {
      figma.ui.postMessage({
        type: "conversion-ready",
        requestId,
        icons: [],
        error: formatPluginError(
          createSelectionError(
            "Select at least one exportable node.",
            "Pick icon nodes in the canvas and run export again.",
          ),
        ),
      } satisfies ConversionReadyMessage);
      return;
    }

    const { icons, firstError, failedCount, superseded } = await exportNodesAsSvg(exportableNodes, supersedeToken);

    if (superseded) {
      figma.ui.postMessage({
        type: "conversion-ready",
        requestId,
        icons: [],
        superseded: true,
      } satisfies ConversionReadyMessage);
      return;
    }

    applyRelaunchData(exportableNodes, icons);

    if (failedCount > 0 && icons.length > 0) {
      figma.notify(firstError ?? `Some icons failed to export (${failedCount}).`);
      figma.ui.postMessage({
        type: "conversion-ready",
        requestId,
        icons,
        attemptedCount: exportableNodes.length,
        exportFailedCount: failedCount,
      } satisfies ConversionReadyMessage);
      return;
    }

    if (firstError) {
      figma.notify(firstError);
      figma.ui.postMessage({
        type: "conversion-ready",
        requestId,
        icons,
        error: firstError,
      } satisfies ConversionReadyMessage);
      return;
    }

    figma.ui.postMessage({
      type: "conversion-ready",
      requestId,
      icons,
      attemptedCount: exportableNodes.length,
      exportFailedCount: 0,
    } satisfies ConversionReadyMessage);
  } catch (error) {
    figma.ui.postMessage({
      type: "conversion-ready",
      requestId,
      icons: [],
      error: formatPluginError(createInternalError(String(error))),
    } satisfies ConversionReadyMessage);
  } finally {
    if (activeRun && activeRun.requestId === requestId) {
      activeRun = null;
    }
  }
};

function decodeUtf8(bytes: Uint8Array): string {
  if (typeof TextDecoder !== "undefined") {
    return new TextDecoder("utf-8").decode(bytes);
  }

  let result = "";
  for (let i = 0; i < bytes.length; i += 1) {
    result += String.fromCharCode(bytes[i]);
  }
  return result;
}

async function exportNodesAsSvg(
  nodes: Array<SceneNode & ExportMixin>,
  supersedeToken: ActiveRun["supersedeToken"],
): Promise<{ icons: ExportedIcon[]; firstError: string | null; failedCount: number; superseded: boolean }> {
  const icons: Array<ExportedIcon | null> = new Array(nodes.length).fill(null);
  let firstError: string | null = null;
  let failedCount = 0;
  let nextIndex = 0;

  const workerCount = Math.min(MAX_EXPORT_CONCURRENCY, nodes.length);

  const workers = Array.from({ length: workerCount }, async () => {
    while (nextIndex < nodes.length) {
      if (supersedeToken.superseded) {
        break;
      }

      const index = nextIndex;
      nextIndex += 1;

      const node = nodes[index];
      try {
        const bytes = await node.exportAsync({ format: "SVG" });
        const svg = decodeUtf8(bytes);
        icons[index] = { id: node.id, name: node.name, svg };
      } catch (error) {
        failedCount += 1;
        if (firstError === null) {
          firstError = formatPluginError(createExportError(node.name, String(error)));
        }
      }
    }
  });

  await Promise.all(workers);

  return {
    icons: icons.filter((icon): icon is ExportedIcon => icon !== null),
    firstError,
    failedCount,
    superseded: supersedeToken.superseded,
  };
}

function applyRelaunchData(nodes: Array<SceneNode & ExportMixin>, successfulIcons: ExportedIcon[]): void {
  const successfulIds = new Set(successfulIcons.map((icon) => icon.id));
  for (const node of nodes) {
    if (!successfulIds.has(node.id)) {
      continue;
    }

    node.setRelaunchData(RELAUNCH_DATA);
  }
}
