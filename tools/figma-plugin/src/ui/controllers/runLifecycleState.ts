import { getConversionResults, getConversionResultsCount } from "../core/state";
import { isLoadingResultsVisible, renderResults } from "../features/render";
import { setStatus } from "../core/status";
import { createTimeoutError, formatPluginError } from "../../shared/errorFormatter";

export type RunLifecycleState = "superseded" | "timed-out";

function restorePreviousResultsWhenLoading(): void {
  if (getConversionResultsCount() === 0) {
    return;
  }

  if (isLoadingResultsVisible()) {
    renderResults(Array.from(getConversionResults()));
  }
}

function clearLoadingResultsWhenNoPrevious(): void {
  if (getConversionResultsCount() !== 0) {
    return;
  }

  if (isLoadingResultsVisible()) {
    renderResults([]);
  }
}

export function applyRunLifecycleState(state: RunLifecycleState): void {
  switch (state) {
    case "superseded": {
      setStatus("Run superseded by a newer request.", "ready");
      restorePreviousResultsWhenLoading();
      return;
    }

    case "timed-out": {
      restorePreviousResultsWhenLoading();
      clearLoadingResultsWhenNoPrevious();
      setStatus(formatPluginError(createTimeoutError()), "error");
      return;
    }
  }
}
