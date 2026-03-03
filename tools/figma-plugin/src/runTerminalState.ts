import { getConversionResults, getConversionResultsCount } from "./state";
import { isLoadingResultsVisible, renderResults } from "./render";
import { setStatus } from "./status";
import { createTimeoutError, formatPluginError } from "./errorFormatter";

export type TerminalRunState = "superseded" | "timed-out";

function restorePreviousResultsWhenLoading(): void {
  if (getConversionResultsCount() === 0) {
    return;
  }

  if (isLoadingResultsVisible()) {
    renderResults(Array.from(getConversionResults()));
  }
}

export function applyTerminalRunState(state: TerminalRunState): void {
  switch (state) {
    case "superseded": {
      setStatus("Run superseded by a newer request.", "ready");
      restorePreviousResultsWhenLoading();
      return;
    }

    case "timed-out": {
      setStatus(formatPluginError(createTimeoutError()), "error");
      return;
    }
  }
}
