export const runButton = document.querySelector<HTMLButtonElement>("#run")!;
export const copyAllButton = document.querySelector<HTMLButtonElement>("#copy-all")!;
export const downloadAllButton = document.querySelector<HTMLButtonElement>("#download-all")!;
export const statusText = document.querySelector<HTMLSpanElement>("#status")!;
export const statusIcon = document.querySelector<HTMLDivElement>("#status-icon")!;
export const packageInput = document.querySelector<HTMLInputElement>("#package")!;
export const useComposeColorsInput = document.querySelector<HTMLInputElement>("#compose-colors")!;
export const addTrailingCommaInput = document.querySelector<HTMLInputElement>("#trailing-comma")!;
export const useExplicitModeInput = document.querySelector<HTMLInputElement>("#explicit-mode")!;
export const usePathDataStringInput = document.querySelector<HTMLInputElement>("#path-data")!;
export const autoMirrorInput = document.querySelector<HTMLSelectElement>("#auto-mirror")!;
export const resultsContainer = document.querySelector<HTMLDivElement>("#results")!;
export const emptyState = document.querySelector<HTMLDivElement>("#empty-state")!;
export const selectionPreview = document.querySelector<HTMLDivElement>("#selection-preview")!;

export const settingsInputs = [
  packageInput,
  useComposeColorsInput,
  addTrailingCommaInput,
  useExplicitModeInput,
  usePathDataStringInput,
  autoMirrorInput,
];
