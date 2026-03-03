import { autoExportInput } from "./dom";
import { renderResults, showAutoExportDisabledEmptyState, showDefaultEmptyState, showLoadingEmptyState, updateSelectionPreview } from "./render";
import { clearConversionResults } from "./state";

const AUTO_RUN_DEBOUNCE_MS = 300;

type SelectionControllerDeps = {
  requestConversion: () => void;
  updateBulkActionState: () => void;
};

type SettingsLoadedOptions = {
  suppressAutoRun?: boolean;
};

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

  return {
    handleSelectionChanged(count: number, names: string[]): void {
      latestSelectionCount = count;
      updateSelectionPreview(count, names);

      if (count === 0) {
        scheduleAutoConversion();
        clearConversionResults();
        renderResults([]);
        deps.updateBulkActionState();
        showDefaultEmptyState();
        return;
      }

      if (!settingsInitialized) {
        showDefaultEmptyState();
        return;
      }

      if (!autoExportInput.checked) {
        scheduleAutoConversion();
        showAutoExportDisabledEmptyState();
        return;
      }

      const shouldRunImmediately = !hasRequestedInitialAutoConversion;
      if (scheduleAutoConversion(shouldRunImmediately)) {
        showLoadingEmptyState();
      }
    },

    handleSettingsLoaded(options: SettingsLoadedOptions = {}): void {
      settingsInitialized = true;

      if (latestSelectionCount === 0) {
        showDefaultEmptyState();
        return;
      }

      if (!autoExportInput.checked) {
        scheduleAutoConversion();
        showAutoExportDisabledEmptyState();
        return;
      }

      if (options.suppressAutoRun) {
        showLoadingEmptyState();
        return;
      }

      if (scheduleAutoConversion(!hasRequestedInitialAutoConversion)) {
        showLoadingEmptyState();
      }
    },

    handleSettingsInputChanged(): void {
      const isScheduled = scheduleAutoConversion();

      if (latestSelectionCount === 0) {
        showDefaultEmptyState();
        return;
      }

      if (!autoExportInput.checked) {
        showAutoExportDisabledEmptyState();
        return;
      }

      if (isScheduled) {
        showLoadingEmptyState();
      }
    },
  };
}
