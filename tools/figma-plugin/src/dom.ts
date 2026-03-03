export const runButton = document.querySelector<HTMLButtonElement>("#run")!;
export const cancelButton = document.querySelector<HTMLButtonElement>("#cancel")!;
export const copyAllButton = document.querySelector<HTMLButtonElement>("#copy-all")!;
export const downloadAllButton = document.querySelector<HTMLButtonElement>("#download-all")!;
export const statusText = document.querySelector<HTMLSpanElement>("#status")!;
export const statusIcon = document.querySelector<HTMLDivElement>("#status-icon")!;
export const statusDetails = document.querySelector<HTMLDetailsElement>("#status-details")!;
export const statusDiagnostics = document.querySelector<HTMLPreElement>("#status-diagnostics")!;
export const packageInput = document.querySelector<HTMLInputElement>("#package")!;
export const outputFormatInput = document.querySelector<HTMLSelectElement>("#output-format")!;
export const useComposeColorsInput = document.querySelector<HTMLInputElement>("#compose-colors")!;
export const addTrailingCommaInput = document.querySelector<HTMLInputElement>("#trailing-comma")!;
export const useExplicitModeInput = document.querySelector<HTMLInputElement>("#explicit-mode")!;
export const usePathDataStringInput = document.querySelector<HTMLInputElement>("#path-data")!;
export const autoMirrorInput = document.querySelector<HTMLSelectElement>("#auto-mirror")!;
export const autoExportInput = document.querySelector<HTMLInputElement>("#auto-export")!;
export const resultsContainer = document.querySelector<HTMLDivElement>("#results")!;
export const emptyState = document.querySelector<HTMLDivElement>("#empty-state")!;
export const emptyStateTitle = emptyState.querySelector<HTMLHeadingElement>("h3")!;
export const emptyStateDescription = emptyState.querySelector<HTMLParagraphElement>("p")!;
export const selectionPreview = document.querySelector<HTMLDivElement>("#selection-preview")!;
export const mainScroll = document.querySelector<HTMLDivElement>("#main-scroll")!;

export const settingsInputs = [
  packageInput,
  outputFormatInput,
  useComposeColorsInput,
  addTrailingCommaInput,
  useExplicitModeInput,
  usePathDataStringInput,
  autoMirrorInput,
  autoExportInput,
];
