import { statusText, statusIcon } from "./dom";
import type { StatusType } from "./types";

export function setStatus(message: string, type: StatusType = "ready"): void {
  statusText.textContent = message;
  statusIcon.className = `status-icon ${type}`;
}
