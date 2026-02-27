import type { ConvertResultWithSvg } from "./types";

export const conversionResults: ConvertResultWithSvg[] = [];

let pendingTimeoutId: number | null = null;

export function setPendingTimeout(callback: () => void, ms: number): void {
  clearPendingTimeout();
  pendingTimeoutId = window.setTimeout(callback, ms);
}

export function clearPendingTimeout(): void {
  if (pendingTimeoutId !== null) {
    window.clearTimeout(pendingTimeoutId);
    pendingTimeoutId = null;
  }
}
