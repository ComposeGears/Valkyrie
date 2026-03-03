import type { MainToUiMessage } from "./messages";
import { getConversionResultsCount, getConversionResults } from "./state";
import { applySettings } from "./settings";
import { setStatus } from "./status";
import {
  isLoadingResultsVisible,
  renderLoadingResults,
  renderResults,
  showCanceledEmptyState,
  showLoadingEmptyState,
} from "./render";
import { runConversion } from "./conversion";

type SelectionController = {
  handleSelectionChanged: (count: number, names: string[]) => void;
  handleSettingsLoaded: (options?: { suppressAutoRun?: boolean }) => void;
};

type RequestController = {
  requestConversion: () => void;
  isLatestRequest: (requestId: number) => boolean;
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
        const shouldReexport = message.launchCommand === "re-export";
        deps.selectionController.handleSettingsLoaded({ suppressAutoRun: shouldReexport });
        if (shouldReexport) {
          deps.requestController.requestConversion();
        }
        return;
      }

      case "conversion-started": {
        if (!deps.requestController.isLatestRequest(message.requestId)) {
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

        if (message.canceled) {
          setStatus(
            message.canceledReason === "superseded"
              ? "Run superseded by a newer request."
              : "Run canceled.",
            "ready",
          );

          if (getConversionResultsCount() > 0) {
            if (isLoadingResultsVisible()) {
              renderResults(Array.from(getConversionResults()));
            }
          } else if (message.canceledReason === "user") {
            renderResults([]);
            showCanceledEmptyState();
          }
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
