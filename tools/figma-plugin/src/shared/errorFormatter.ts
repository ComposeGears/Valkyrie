export type PluginError = {
  summary: string;
  nextStep: string;
  diagnostics?: string;
};

export const DIAGNOSTICS_DELIMITER = " Diagnostics: ";

export function formatPluginError(error: PluginError): string {
  const base = `${error.summary} Next: ${error.nextStep}`;
  if (!error.diagnostics) {
    return base;
  }

  return `${base}${DIAGNOSTICS_DELIMITER}${error.diagnostics}`;
}

export function createSelectionError(summary: string, nextStep: string): PluginError {
  return { summary, nextStep };
}

export function createSettingsError(summary: string, nextStep: string): PluginError {
  return { summary, nextStep };
}

export function createExportError(nodeName: string, diagnostics: string): PluginError {
  return {
    summary: `Failed to export '${nodeName}'.`,
    nextStep: "Check node permissions/structure, then retry.",
    diagnostics,
  };
}

export function createConverterUnavailableError(): PluginError {
  return {
    summary: "Converter runtime is unavailable.",
    nextStep: "Run pnpm build:all in tools/figma-plugin, reload plugin, and retry.",
  };
}

export function createTimeoutError(): PluginError {
  return {
    summary: "No response from Figma main thread.",
    nextStep: "Check plugin console for errors, then reload and retry.",
  };
}

export function createInternalError(diagnostics: string): PluginError {
  return {
    summary: "Unexpected plugin error.",
    nextStep: "Retry once. If it persists, reload plugin and report the diagnostics.",
    diagnostics,
  };
}
