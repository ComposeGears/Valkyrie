export type OutputFormat = "backing_property" | "lazy_property";
export type AutoMirrorOption = "" | "true" | "false";

export type PluginSettings = {
  packageName: string;
  outputFormat: OutputFormat;
  useComposeColors: boolean;
  addTrailingComma: boolean;
  useExplicitMode: boolean;
  usePathDataString: boolean;
  autoMirror: AutoMirrorOption;
  autoExport: boolean;
};

export const DEFAULT_PLUGIN_SETTINGS: PluginSettings = {
  packageName: "com.example.icons",
  outputFormat: "backing_property",
  useComposeColors: true,
  addTrailingComma: false,
  useExplicitMode: false,
  usePathDataString: false,
  autoMirror: "",
  autoExport: true,
};

function asObject(value: unknown): Record<string, unknown> | null {
  if (value == null || typeof value !== "object") {
    return null;
  }
  return value as Record<string, unknown>;
}

function asOutputFormat(value: unknown): OutputFormat | null {
  return value === "backing_property" || value === "lazy_property" ? value : null;
}

function asAutoMirrorOption(value: unknown): AutoMirrorOption | null {
  return value === "" || value === "true" || value === "false" ? value : null;
}

export function sanitizePluginSettings(value: unknown): PluginSettings | null {
  const raw = asObject(value);
  if (!raw) {
    return null;
  }

  return {
    packageName: typeof raw.packageName === "string" ? raw.packageName : DEFAULT_PLUGIN_SETTINGS.packageName,
    outputFormat: asOutputFormat(raw.outputFormat) ?? DEFAULT_PLUGIN_SETTINGS.outputFormat,
    useComposeColors:
      typeof raw.useComposeColors === "boolean"
        ? raw.useComposeColors
        : DEFAULT_PLUGIN_SETTINGS.useComposeColors,
    addTrailingComma:
      typeof raw.addTrailingComma === "boolean"
        ? raw.addTrailingComma
        : DEFAULT_PLUGIN_SETTINGS.addTrailingComma,
    useExplicitMode:
      typeof raw.useExplicitMode === "boolean"
        ? raw.useExplicitMode
        : DEFAULT_PLUGIN_SETTINGS.useExplicitMode,
    usePathDataString:
      typeof raw.usePathDataString === "boolean"
        ? raw.usePathDataString
        : DEFAULT_PLUGIN_SETTINGS.usePathDataString,
    autoMirror: asAutoMirrorOption(raw.autoMirror) ?? DEFAULT_PLUGIN_SETTINGS.autoMirror,
    autoExport: typeof raw.autoExport === "boolean" ? raw.autoExport : DEFAULT_PLUGIN_SETTINGS.autoExport,
  };
}
