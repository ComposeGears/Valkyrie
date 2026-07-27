import { runButton } from "./core/dom";
import { addSettingsInputListeners, initSettingsListeners } from "./features/settings";
import { onMessage, onError } from "./core/api";
import { setStatus } from "./core/status";
import { initializeBulkActions, updateBulkActionState } from "./features/bulkActions";
import { createSelectionController } from "./controllers/selectionController";
import { createRequestController } from "./controllers/requestController";
import { createMainMessageHandler } from "./controllers/messageHandlers";
import { applyRunLifecycleState } from "./controllers/runLifecycleState";

const requestController = createRequestController({
  onTimedOut: () => {
    applyRunLifecycleState("timed-out");
  },
});

const selectionController = createSelectionController({
  requestConversion: () => {
    requestController.requestConversion();
  },
  cancelActiveRequest: () => {
    requestController.cancelActiveRequest();
  },
  updateBulkActionState,
});

runButton.addEventListener("click", () => {
  requestController.requestConversion();
});

addSettingsInputListeners(() => {
  selectionController.handleSettingsInputChanged();
});

onMessage(
  createMainMessageHandler({
    selectionController,
    requestController,
  }),
);

initSettingsListeners();
initializeBulkActions();
setStatus("Ready");
updateBulkActionState();

onError((event) => {
  setStatus(`UI error: ${event.message}`, "error");
});
