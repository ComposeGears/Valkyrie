type ExportedIcon = {
  id: string;
  name: string;
  svg: string;
};

type ConversionStartedMessage = {
  type: "conversion-started";
  selectedCount: number;
};

type SelectionChangedMessage = {
  type: "selection-changed";
  count: number;
  names: string[];
};

type SettingsLoadedMessage = {
  type: "settings-loaded";
  settings: Record<string, unknown> | null;
};

type RunConversionMessage = {
  type: "run-conversion";
};

type CloseMessage = {
  type: "close-plugin";
};

type SaveSettingsMessage = {
  type: "save-settings";
  settings: Record<string, unknown>;
};

type LoadSettingsMessage = {
  type: "load-settings";
};

type UiMessage = RunConversionMessage | CloseMessage | SaveSettingsMessage | LoadSettingsMessage;

const PLUGIN_UI_SIZE = { width: 1080, height: 760 };
const SETTINGS_KEY = "valkyrie-export-settings";
const MAX_SELECTION_NAMES = 8;

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

figma.ui.onmessage = async (message: UiMessage) => {
  try {
    if (message.type === "close-plugin") {
      figma.closePlugin();
      return;
    }

    if (message.type === "load-settings") {
      const settings = await figma.clientStorage.getAsync(SETTINGS_KEY);
      figma.ui.postMessage({
        type: "settings-loaded",
        settings: settings != null ? settings : null,
      } satisfies SettingsLoadedMessage);
      return;
    }

    if (message.type === "save-settings") {
      await figma.clientStorage.setAsync(SETTINGS_KEY, message.settings);
      return;
    }

    if (message.type !== "run-conversion") {
      return;
    }

    const selected = figma.currentPage.selection;
    const exportableNodes = selected.filter((node): node is SceneNode & ExportMixin => "exportAsync" in node);

    figma.ui.postMessage({
      type: "conversion-started",
      selectedCount: exportableNodes.length,
    } satisfies ConversionStartedMessage);

    if (exportableNodes.length === 0) {
      figma.ui.postMessage({
        type: "conversion-ready",
        icons: [] as ExportedIcon[],
        error: "Select at least one exportable node.",
      });
      return;
    }

    const icons: ExportedIcon[] = [];

    for (const node of exportableNodes) {
      try {
        const bytes = await node.exportAsync({ format: "SVG" });
        const svg = decodeUtf8(bytes);
        icons.push({ id: node.id, name: node.name, svg });
      } catch (error) {
        figma.notify(`Failed to export ${node.name}`);
        figma.ui.postMessage({
          type: "conversion-ready",
          icons,
          error: String(error),
        });
        return;
      }
    }

    figma.ui.postMessage({
      type: "conversion-ready",
      icons,
    });
  } catch (error) {
    figma.ui.postMessage({
      type: "conversion-ready",
      icons: [] as ExportedIcon[],
      error: `Main thread error: ${String(error)}`,
    });
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
