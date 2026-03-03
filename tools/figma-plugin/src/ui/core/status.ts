import { statusText, statusIcon, statusDetails, statusDiagnostics } from "./dom";
import type { StatusType } from "./types";

const DIAGNOSTICS_MARKER = " Diagnostics: ";

function splitDiagnostics(message: string): { summary: string; diagnostics: string | null } {
  const markerIndex = message.indexOf(DIAGNOSTICS_MARKER);
  if (markerIndex < 0) {
    return { summary: message, diagnostics: null };
  }

  const summary = message.slice(0, markerIndex).trim();
  const diagnostics = message.slice(markerIndex + DIAGNOSTICS_MARKER.length).trim();
  return { summary, diagnostics: diagnostics.length > 0 ? diagnostics : null };
}

export function setStatus(message: string, type: StatusType = "ready"): void {
  const { summary, diagnostics } = splitDiagnostics(message);

  statusText.textContent = summary;
  statusIcon.className = `status-icon ${type}`;

  if (diagnostics) {
    statusDiagnostics.textContent = diagnostics;
    statusDetails.classList.remove("hidden");
  } else {
    statusDiagnostics.textContent = "";
    statusDetails.open = false;
    statusDetails.classList.add("hidden");
  }
}
