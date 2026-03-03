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
  canceled?: boolean;
  canceledReason?: "user" | "superseded";
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
  launchCommand?: "open-exporter" | "re-export";
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

export type RequestSelectionMessage = {
  type: "request-selection";
};

export type CloseMessage = {
  type: "close-plugin";
};

export type SaveSettingsMessage = {
  type: "save-settings";
  settings: PluginSettings;
};

export type LoadSettingsMessage = {
  type: "load-settings";
};

export type CancelConversionMessage = {
  type: "cancel-conversion";
  requestId: number;
};

export type UiToMainMessage =
  | RunConversionMessage
  | CancelConversionMessage
  | RequestSelectionMessage
  | CloseMessage
  | SaveSettingsMessage
  | LoadSettingsMessage;
