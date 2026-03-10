import { autoExportInput, runButton } from "../core/dom";
import { renderResults, showAutoExportDisabledEmptyState, showDefaultEmptyState, showLoadingEmptyState, updateSelectionPreview } from "../features/render";
import { clearConversionResults } from "../core/state";

const AUTO_RUN_DEBOUNCE_MS = 300;

type SelectionControllerDeps = {
  requestConversion: () => void;
  cancelActiveRequest: () => void;
  updateBulkActionState: () => void;
};

type SelectionUiState = "default-empty" | "auto-export-disabled" | "auto-export-ready";

function updateRunButtonLabel(): void {
  runButton.textContent = autoExportInput.checked ? "Refresh" : "Export";
}

function showSelectionEmptyState(state: Exclude<SelectionUiState, "auto-export-ready">): void {
  if (state === "auto-export-disabled") {
    showAutoExportDisabledEmptyState();
    return;
  }

  showDefaultEmptyState();
}

export function createSelectionController(deps: SelectionControllerDeps) {
  let autoRunTimeoutId: number | null = null;
  let latestSelectionCount = 0;
  let hasRequestedInitialAutoConversion = false;
  let settingsInitialized = false;

  const scheduleAutoConversion = (immediate = false): boolean => {
    if (autoRunTimeoutId !== null) {
      window.clearTimeout(autoRunTimeoutId);
      autoRunTimeoutId = null;
    }

    if (!autoExportInput.checked || latestSelectionCount === 0) {
      return false;
    }

    if (immediate) {
      hasRequestedInitialAutoConversion = true;
      deps.requestConversion();
      return true;
    }

    autoRunTimeoutId = window.setTimeout(() => {
      autoRunTimeoutId = null;
      hasRequestedInitialAutoConversion = true;
      deps.requestConversion();
    }, AUTO_RUN_DEBOUNCE_MS);

    return true;
  };

  const deriveSelectionUiState = (): SelectionUiState => {
    if (latestSelectionCount === 0 || !settingsInitialized) {
      return "default-empty";
    }

    if (!autoExportInput.checked) {
      return "auto-export-disabled";
    }

    return "auto-export-ready";
  };

  return {
    handleSelectionChanged(count: number, names: string[]): void {
      latestSelectionCount = count;
      updateRunButtonLabel();
      updateSelectionPreview(count, names);

      if (count === 0) {
        deps.cancelActiveRequest();
        scheduleAutoConversion();
        clearConversionResults();
        renderResults([]);
        deps.updateBulkActionState();
        showSelectionEmptyState("default-empty");
        return;
      }

      const uiState = deriveSelectionUiState();
      if (uiState === "default-empty") {
        showSelectionEmptyState(uiState);
        return;
      }

      if (uiState === "auto-export-disabled") {
        scheduleAutoConversion();
        showSelectionEmptyState(uiState);
        return;
      }

      const shouldRunImmediately = !hasRequestedInitialAutoConversion;
      if (scheduleAutoConversion(shouldRunImmediately)) {
        showLoadingEmptyState();
      }
    },

    handleSettingsLoaded(): void {
      settingsInitialized = true;
      updateRunButtonLabel();

      const uiState = deriveSelectionUiState();
      if (uiState === "default-empty") {
        showSelectionEmptyState(uiState);
        return;
      }

      if (uiState === "auto-export-disabled") {
        scheduleAutoConversion();
        showSelectionEmptyState(uiState);
        return;
      }

      if (scheduleAutoConversion(!hasRequestedInitialAutoConversion)) {
        showLoadingEmptyState();
      }
    },

    handleSettingsInputChanged(): void {
      updateRunButtonLabel();
      const uiState = deriveSelectionUiState();

      if (uiState === "default-empty") {
        showSelectionEmptyState(uiState);
        return;
      }

      if (uiState === "auto-export-disabled") {
        scheduleAutoConversion();
        showSelectionEmptyState(uiState);
        return;
      }

      const isScheduled = scheduleAutoConversion();
      if (isScheduled) {
        showLoadingEmptyState();
      }
    },
  };
}
