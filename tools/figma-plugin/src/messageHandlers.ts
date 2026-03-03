import type { MainToUiMessage } from "./messages";
import { applySettings } from "./settings";
import { setStatus } from "./status";
import { renderLoadingResults, showLoadingEmptyState } from "./render";
import { runConversion } from "./conversion";
import { applyTerminalRunState } from "./runTerminalState";

type SelectionController = {
  handleSelectionChanged: (count: number, names: string[]) => void;
  handleSettingsLoaded: () => void;
};

type RequestController = {
  acknowledgeRequestStart: (requestId: number) => boolean;
  completeRequest: (requestId: number) => boolean;
};

type MessageHandlerDeps = {
  selectionController: SelectionController;
  requestController: RequestController;
};

export function createMainMessageHandler(deps: MessageHandlerDeps): (message: MainToUiMessage) => void {
  return (message: MainToUiMessage) => {
    switch (message.type) {
      case "selection-changed": {
        deps.selectionController.handleSelectionChanged(message.count, message.names);
        return;
      }

      case "settings-loaded": {
        applySettings(message.settings);
        deps.selectionController.handleSettingsLoaded();
        return;
      }

      case "conversion-started": {
        if (!deps.requestController.acknowledgeRequestStart(message.requestId)) {
          return;
        }
        renderLoadingResults(message.selectedCount);
        showLoadingEmptyState();
        setStatus(`Exporting ${message.selectedCount} selected node(s)...`, "working");
        return;
      }

      case "conversion-ready": {
        if (!deps.requestController.completeRequest(message.requestId)) {
          return;
        }

        if (message.superseded) {
          applyTerminalRunState("superseded");
          return;
        }

        runConversion(message.icons, {
          attemptedCount: message.attemptedCount,
          exportFailedCount: message.exportFailedCount,
          upstreamError: message.error,
        });
        return;
      }

      case "settings-error": {
        setStatus(message.error, "error");
        return;
      }

      default:
        return;
    }
  };
}
