import type { ConvertOptions, ConvertResult } from "./converterAdapter";

export type ExportedIcon = {
  id: string;
  name: string;
  svg: string;
};

export type ConversionReadyMessage = {
  type: "conversion-ready";
  icons: ExportedIcon[];
  error?: string;
};

export type ConversionStartedMessage = {
  type: "conversion-started";
  selectedCount: number;
};

export type SelectionChangedMessage = {
  type: "selection-changed";
  count: number;
  names: string[];
};

export type SettingsLoadedMessage = {
  type: "settings-loaded";
  settings: Record<string, unknown> | null;
};

export type PluginMessage =
  | ConversionReadyMessage
  | ConversionStartedMessage
  | SelectionChangedMessage
  | SettingsLoadedMessage;

export type ConvertResultWithSvg = ConvertResult & { svg?: string };

export type StatusType = "ready" | "working" | "error";
