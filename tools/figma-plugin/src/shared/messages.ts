import type { PluginSettings } from "./pluginSettings";

export type ExportedIcon = {
  id: string;
  name: string;
  svg: string;
};

export type ConversionStartedMessage = {
  type: "conversion-started";
  requestId: number;
  selectedCount: number;
};

export type ConversionReadyMessage = {
  type: "conversion-ready";
  requestId: number;
  icons: ExportedIcon[];
  error?: string;
  attemptedCount?: number;
  exportFailedCount?: number;
  superseded?: boolean;
};

export type SettingsErrorMessage = {
  type: "settings-error";
  error: string;
};

export type SelectionChangedMessage = {
  type: "selection-changed";
  count: number;
  names: string[];
};

export type SettingsLoadedMessage = {
  type: "settings-loaded";
  settings: PluginSettings | null;
};

export type MainToUiMessage =
  | ConversionStartedMessage
  | ConversionReadyMessage
  | SelectionChangedMessage
  | SettingsLoadedMessage
  | SettingsErrorMessage;

export type RunConversionMessage = {
  type: "run-conversion";
  requestId: number;
};

export type SaveSettingsMessage = {
  type: "save-settings";
  settings: PluginSettings;
};

export type LoadSettingsMessage = {
  type: "load-settings";
};

export type UiToMainMessage =
  | RunConversionMessage
  | SaveSettingsMessage
  | LoadSettingsMessage;
