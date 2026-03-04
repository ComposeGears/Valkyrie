import { sendMessage } from "../core/api";
import { createTimeoutError, formatPluginError } from "../../shared/errorFormatter";
import { setStatus } from "../core/status";

const REQUEST_ACK_TIMEOUT_MS = 5000;
const RUN_TIMEOUT_MS = 120000;

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

  const scheduleRequestTimeout = (requestId: number, state: RequestState, timeoutMs: number): void => {
    setPendingTimeout(() => {
      if (!activeRequest || activeRequest.id !== requestId || activeRequest.state !== state) {
        return;
      }

      activeRequest = null;
      if (deps.onTimedOut) {
        deps.onTimedOut();
      } else {
        setStatus(formatPluginError(createTimeoutError()), "error");
      }
    }, timeoutMs);
  };

  return {
    cancelActiveRequest(): void {
      activeRequest = null;
      clearPendingTimeout();
    },

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
      scheduleRequestTimeout(requestId, "requested", REQUEST_ACK_TIMEOUT_MS);

      sendMessage({ type: "run-conversion", requestId });
    },

    acknowledgeRequestStart(requestId: number): boolean {
      if (!activeRequest || activeRequest.id !== requestId || activeRequest.state !== "requested") {
        return false;
      }

      clearPendingTimeout();
      activeRequest.state = "started";
      scheduleRequestTimeout(requestId, "started", RUN_TIMEOUT_MS);
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
