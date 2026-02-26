type ExportedIcon = {
  id: string;
  name: string;
  svg: string;
};

type ConversionStartedMessage = {
  type: "conversion-started";
  selectedCount: number;
};

type RunConversionMessage = {
  type: "run-conversion";
};

type CloseMessage = {
  type: "close-plugin";
};

type UiMessage = RunConversionMessage | CloseMessage;

const PLUGIN_UI_SIZE = { width: 1080, height: 760 };

figma.showUI(__html__, PLUGIN_UI_SIZE);

figma.ui.onmessage = async (message: UiMessage) => {
  try {
    if (message.type === "close-plugin") {
      figma.closePlugin();
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
