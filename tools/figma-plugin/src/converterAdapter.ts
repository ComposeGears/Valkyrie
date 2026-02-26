export type ConvertOptions = {
  packageName: string;
  useComposeColors: boolean;
  addTrailingComma: boolean;
  useExplicitMode: boolean;
  usePathDataString: boolean;
  indentSize: number;
  autoMirror: "" | "true" | "false";
};

export type ConvertResult = {
  ok: boolean;
  iconName: string;
  fileName: string;
  content: string;
  error?: string;
};

type WasmConverter = {
  convertSvg: (
    svg: string,
    iconName: string,
    packageName: string,
    useComposeColors: boolean,
    addTrailingComma: boolean,
    useExplicitMode: boolean,
    usePathDataString: boolean,
    indentSize: number,
    autoMirror: string,
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
      ok: false,
      iconName,
      fileName: "",
      content: "",
      error: "Wasm converter is not loaded. Run `pnpm build:all` in tools/figma-plugin and reload plugin.",
    };
  }

  const json = converter.convertSvg(
    svg,
    iconName,
    options.packageName,
    options.useComposeColors,
    options.addTrailingComma,
    options.useExplicitMode,
    options.usePathDataString,
    options.indentSize,
    options.autoMirror,
  );

  return JSON.parse(json) as ConvertResult;
}
