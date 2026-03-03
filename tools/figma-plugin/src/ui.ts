import { runButton, cancelButton } from "./dom";
import { getConversionResultsCount, getConversionResults } from "./state";
import { addSettingsInputListeners, initSettingsListeners } from "./settings";
import { sendMessage, onMessage, onError } from "./api";
import { setStatus } from "./status";
import { initializeBulkActions, updateBulkActionState } from "./bulkActions";
import { createSelectionController } from "./selectionController";
import { createRequestController } from "./requestController";
import { createMainMessageHandler } from "./messageHandlers";
import { renderResults, showCanceledEmptyState } from "./render";

const requestController = createRequestController({
  onRunningChanged: (running) => {
    cancelButton.disabled = !running;
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

cancelButton.addEventListener("click", () => {
  const canceled = requestController.cancelActiveRequest();
  if (!canceled) {
    return;
  }

  if (getConversionResultsCount() > 0) {
    renderResults(Array.from(getConversionResults()));
    return;
  }

  renderResults([]);
  showCanceledEmptyState();
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

sendMessage({ type: "request-selection" });
