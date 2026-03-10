import type { MainToUiMessage } from "../../shared/messages";
import { applySettings } from "../features/settings";
import { setStatus } from "../core/status";
import { renderLoadingResults, showLoadingEmptyState } from "../features/render";
import { runConversion } from "../features/conversion";
import { applyRunLifecycleState } from "./runLifecycleState";

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
        setStatus(`Exporting ${message.selectedCount} selected icon(s)...`, "working");
        return;
      }

      case "conversion-ready": {
        if (!deps.requestController.completeRequest(message.requestId)) {
          return;
        }

        if (message.superseded) {
          applyRunLifecycleState("superseded");
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
