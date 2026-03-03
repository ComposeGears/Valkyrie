import { runButton } from "./dom";
import { addSettingsInputListeners, initSettingsListeners } from "./settings";
import { onMessage, onError } from "./api";
import { setStatus } from "./status";
import { initializeBulkActions, updateBulkActionState } from "./bulkActions";
import { createSelectionController } from "./selectionController";
import { createRequestController } from "./requestController";
import { createMainMessageHandler } from "./messageHandlers";
import { applyTerminalRunState } from "./runTerminalState";

const requestController = createRequestController({
  onTimedOut: () => {
    applyTerminalRunState("timed-out");
  },
});

const selectionController = createSelectionController({
  requestConversion: () => {
    requestController.requestConversion();
  },
  updateBulkActionState,
});

initSettingsListeners();
initializeBulkActions();
setStatus("Ready");
updateBulkActionState();

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

onError((event) => {
  setStatus(`UI error: ${event.message}`, "error");
});
