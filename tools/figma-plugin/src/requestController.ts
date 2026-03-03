import { sendMessage } from "./api";
import { createTimeoutError, formatPluginError } from "./errorFormatter";
import { setStatus } from "./status";

const REQUEST_TIMEOUT_MS = 5000;

type RequestControllerDeps = {
  onRunningChanged?: (running: boolean) => void;
};

export function createRequestController(deps: RequestControllerDeps = {}) {
  let latestRequestId = 0;
  let activeRequestId: number | null = null;
  let pendingTimeoutId: number | null = null;

  const setRunning = (running: boolean): void => {
    deps.onRunningChanged?.(running);
  };

  const clearPendingTimeout = (): void => {
    if (pendingTimeoutId !== null) {
      window.clearTimeout(pendingTimeoutId);
      pendingTimeoutId = null;
    }
  };

  const setPendingTimeout = (callback: () => void, ms: number): void => {
    clearPendingTimeout();
    pendingTimeoutId = window.setTimeout(callback, ms);
  };

  return {
    requestConversion(): void {
      const hadActiveRequest = activeRequestId !== null;
      latestRequestId += 1;
      const requestId = latestRequestId;
      activeRequestId = requestId;
      setRunning(true);

      setStatus(
        hadActiveRequest
          ? "Previous run superseded by newer run. Requesting latest export from Figma..."
          : "Requesting export from Figma...",
        "working",
      );
      clearPendingTimeout();
      setPendingTimeout(() => {
        if (requestId !== latestRequestId) {
          return;
        }
        activeRequestId = null;
        setRunning(false);
        setStatus(formatPluginError(createTimeoutError()), "error");
      }, REQUEST_TIMEOUT_MS);

      sendMessage({ type: "run-conversion", requestId });
    },

    cancelActiveRequest(): boolean {
      if (activeRequestId === null) {
        return false;
      }

      const requestId = activeRequestId;
      activeRequestId = null;
      setRunning(false);
      clearPendingTimeout();
      sendMessage({ type: "cancel-conversion", requestId });
      setStatus("Run canceled. Start a new export when ready.", "ready");
      return true;
    },

    isLatestRequest(requestId: number): boolean {
      return requestId === latestRequestId;
    },

    completeRequest(requestId: number): boolean {
      if (requestId !== latestRequestId) {
        return false;
      }

      activeRequestId = null;
      setRunning(false);
      clearPendingTimeout();
      return true;
    },
  };
}
