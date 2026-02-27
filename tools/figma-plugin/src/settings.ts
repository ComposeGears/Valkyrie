import { packageInput, useComposeColorsInput, addTrailingCommaInput, useExplicitModeInput, usePathDataStringInput, autoMirrorInput, settingsInputs } from "./dom";
import type { ConvertOptions } from "./converterAdapter";

let saveSettingsTimeoutId: number | null = null;

export function getSettingsValues(): Record<string, unknown> {
  return {
    packageName: packageInput.value,
    useComposeColors: useComposeColorsInput.checked,
    addTrailingComma: addTrailingCommaInput.checked,
    useExplicitMode: useExplicitModeInput.checked,
    usePathDataString: usePathDataStringInput.checked,
    autoMirror: autoMirrorInput.value,
  };
}

export function applySettings(settings: Record<string, unknown>): void {
  if (typeof settings.packageName === "string") packageInput.value = settings.packageName;
  if (typeof settings.useComposeColors === "boolean") useComposeColorsInput.checked = settings.useComposeColors;
  if (typeof settings.addTrailingComma === "boolean") addTrailingCommaInput.checked = settings.addTrailingComma;
  if (typeof settings.useExplicitMode === "boolean") useExplicitModeInput.checked = settings.useExplicitMode;
  if (typeof settings.usePathDataString === "boolean") usePathDataStringInput.checked = settings.usePathDataString;
  if (typeof settings.autoMirror === "string") autoMirrorInput.value = settings.autoMirror;
}

export function scheduleSaveSettings(): void {
  if (saveSettingsTimeoutId !== null) {
    window.clearTimeout(saveSettingsTimeoutId);
  }
  saveSettingsTimeoutId = window.setTimeout(() => {
    parent.postMessage({ pluginMessage: { type: "save-settings", settings: getSettingsValues() } }, "*");
    saveSettingsTimeoutId = null;
  }, 500);
}

export function initSettingsListeners(): void {
  for (const input of settingsInputs) {
    input.addEventListener("input", scheduleSaveSettings);
    input.addEventListener("change", scheduleSaveSettings);
  }
  parent.postMessage({ pluginMessage: { type: "load-settings" } }, "*");
}

export function getConvertOptions(): ConvertOptions {
  return {
    packageName: packageInput.value.trim(),
    useComposeColors: useComposeColorsInput.checked,
    addTrailingComma: addTrailingCommaInput.checked,
    useExplicitMode: useExplicitModeInput.checked,
    usePathDataString: usePathDataStringInput.checked,
    indentSize: 4,
    autoMirror: autoMirrorInput.value as "" | "true" | "false",
  };
}
