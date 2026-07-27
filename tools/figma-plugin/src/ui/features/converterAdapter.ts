import type { AutoMirrorOption, OutputFormat } from "../../shared/pluginSettings";
import type { ConvertResult } from "../core/types";

export type ConvertOptions = {
  packageName: string;
  outputFormat: OutputFormat;
  useComposeColors: boolean;
  addTrailingComma: boolean;
  useExplicitMode: boolean;
  usePathDataString: boolean;
  indentSize: number;
  autoMirror: AutoMirrorOption;
};

type WasmConvertResult = {
  type: string;
  iconName: string;
  fileName?: string;
  code?: string;
  error?: string;
};

type WasmConverter = {
  convertSvg: (
    svg: string,
    iconName: string,
    packageName: string,
    outputFormat: string,
    useComposeColors: boolean,
    addTrailingComma: boolean,
    useExplicitMode: boolean,
    usePathDataString: boolean,
    indentSize: number,
    autoMirror: boolean | null,
  ) => string;
  normalizeIconName: (iconName: string) => string;
};

declare global {
  interface Window {
    ValkyrieFigmaWasmConverter?: WasmConverter;
  }
}

export function isConverterReady(): boolean {
  return typeof window.ValkyrieFigmaWasmConverter?.convertSvg === "function";
}

export function normalizeIconName(iconName: string): string {
  if (!window.ValkyrieFigmaWasmConverter) {
    return iconName;
  }
  return window.ValkyrieFigmaWasmConverter.normalizeIconName(iconName);
}

export function convert(svg: string, iconName: string, options: ConvertOptions): ConvertResult {
  const converter = window.ValkyrieFigmaWasmConverter;
  if (!converter) {
    return {
      success: false,
      iconName,
      fileName: "",
      code: "",
      error: "Wasm converter is not loaded. Run `pnpm build:all` in tools/figma-plugin and reload plugin.",
    };
  }

  const json = converter.convertSvg(
    svg,
    iconName,
    options.packageName,
    options.outputFormat,
    options.useComposeColors,
    options.addTrailingComma,
    options.useExplicitMode,
    options.usePathDataString,
    options.indentSize,
    options.autoMirror,
  );

  try {
    return toPluginConvertResult(JSON.parse(json) as WasmConvertResult);
  } catch {
    return {
      success: false,
      iconName,
      fileName: "",
      code: "",
      error: "Failed to parse converter response. Please report this issue.",
    };
  }
}

function toPluginConvertResult(result: WasmConvertResult): ConvertResult {
  if (isSuccessType(result.type)) {
    return {
      success: true,
      iconName: result.iconName,
      fileName: result.fileName ?? "",
      code: result.code ?? "",
    };
  }

  return {
    success: false,
    iconName: result.iconName,
    fileName: "",
    code: "",
    error: result.error ?? "Unknown conversion error",
  };
}

function isSuccessType(type: string): boolean {
  return type === "success";
}
