import { packageInput, outputFormatInput, useComposeColorsInput, addTrailingCommaInput, useExplicitModeInput, usePathDataStringInput, autoMirrorInput, autoExportInput, settingsInputs } from "../core/dom";
import type { ConvertOptions } from "./converterAdapter";
import { sendMessage } from "../core/api";
import type { PluginSettings } from "../../shared/pluginSettings";
import { autoMirrorOptionToSelectValue, parseAutoMirrorOption, sanitizePluginSettings } from "../../shared/pluginSettings";

let saveSettingsTimeoutId: number | null = null;

export function addSettingsInputListeners(listener: () => void): void {
  for (const input of settingsInputs) {
    input.addEventListener("input", listener);
    input.addEventListener("change", listener);
  }
}

export function getSettingsValues(): PluginSettings {
  return {
    packageName: packageInput.value,
    outputFormat: outputFormatInput.value as PluginSettings["outputFormat"],
    useComposeColors: useComposeColorsInput.checked,
    addTrailingComma: addTrailingCommaInput.checked,
    useExplicitMode: useExplicitModeInput.checked,
    usePathDataString: usePathDataStringInput.checked,
    autoMirror: parseAutoMirrorOption(autoMirrorInput.value),
    autoExport: autoExportInput.checked,
  };
}

export function applySettings(settings: PluginSettings | null): void {
  const parsed = sanitizePluginSettings(settings);
  if (!parsed) {
    return;
  }

  packageInput.value = parsed.packageName;
  outputFormatInput.value = parsed.outputFormat;
  useComposeColorsInput.checked = parsed.useComposeColors;
  addTrailingCommaInput.checked = parsed.addTrailingComma;
  useExplicitModeInput.checked = parsed.useExplicitMode;
  usePathDataStringInput.checked = parsed.usePathDataString;
  autoMirrorInput.value = autoMirrorOptionToSelectValue(parsed.autoMirror);
  autoExportInput.checked = parsed.autoExport;
}

export function scheduleSaveSettings(): void {
  if (saveSettingsTimeoutId !== null) {
    window.clearTimeout(saveSettingsTimeoutId);
  }
  saveSettingsTimeoutId = window.setTimeout(() => {
    sendMessage({ type: "save-settings", settings: getSettingsValues() });
    saveSettingsTimeoutId = null;
  }, 500);
}

export function initSettingsListeners(): void {
  addSettingsInputListeners(scheduleSaveSettings);
  sendMessage({ type: "load-settings" });
}

export function getConvertOptions(): ConvertOptions {
  return {
    packageName: packageInput.value.trim(),
    outputFormat: outputFormatInput.value as ConvertOptions["outputFormat"],
    useComposeColors: useComposeColorsInput.checked,
    addTrailingComma: addTrailingCommaInput.checked,
    useExplicitMode: useExplicitModeInput.checked,
    usePathDataString: usePathDataStringInput.checked,
    indentSize: 4,
    autoMirror: parseAutoMirrorOption(autoMirrorInput.value),
  };
}
