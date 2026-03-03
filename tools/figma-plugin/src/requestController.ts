import { sendMessage } from "./api";
import { createTimeoutError, formatPluginError } from "./errorFormatter";
import { setStatus } from "./status";

const REQUEST_TIMEOUT_MS = 5000;

type RequestState = "requested" | "started";

type RequestControllerDeps = {
  onTimedOut?: () => void;
};

export function createRequestController(deps: RequestControllerDeps = {}) {
  let latestRequestId = 0;
  let activeRequest: { id: number; state: RequestState } | null = null;
  let pendingTimeoutId: number | null = null;

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
      const hadActiveRequest = activeRequest !== null;
      latestRequestId += 1;
      const requestId = latestRequestId;
      activeRequest = { id: requestId, state: "requested" };

      setStatus(
        hadActiveRequest
          ? "Previous run superseded by newer run. Requesting latest export from Figma..."
          : "Requesting export from Figma...",
        "working",
      );
      clearPendingTimeout();
      setPendingTimeout(() => {
        if (!activeRequest || activeRequest.id !== requestId || activeRequest.state !== "requested") {
          return;
        }
        activeRequest = null;
        if (deps.onTimedOut) {
          deps.onTimedOut();
        } else {
          setStatus(formatPluginError(createTimeoutError()), "error");
        }
      }, REQUEST_TIMEOUT_MS);

      sendMessage({ type: "run-conversion", requestId });
    },

    acknowledgeRequestStart(requestId: number): boolean {
      if (!activeRequest || activeRequest.id !== requestId || activeRequest.state !== "requested") {
        return false;
      }

      clearPendingTimeout();
      activeRequest.state = "started";
      return true;
    },

    completeRequest(requestId: number): boolean {
      if (!activeRequest || activeRequest.id !== requestId) {
        return false;
      }

      activeRequest = null;
      clearPendingTimeout();
      return true;
    },
  };
}
