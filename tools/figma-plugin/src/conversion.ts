import { convert, isConverterReady, normalizeIconName } from "./converterAdapter";
import { packageInput } from "./dom";
import { createConverterUnavailableError, createSelectionError, createSettingsError, formatPluginError } from "./errorFormatter";
import type { ExportedIcon } from "./messages";
import { renderResults } from "./render";
import { getConvertOptions } from "./settings";
import { replaceConversionResults } from "./state";
import { setStatus } from "./status";
import type { ConvertResultWithSvg, StatusType } from "./types";
import { updateBulkActionState } from "./bulkActions";

const CONVERSION_CHUNK_SIZE = 40;
let latestConversionJobId = 0;

type ConversionContext = {
  attemptedCount?: number;
  exportFailedCount?: number;
  upstreamError?: string;
};

function waitForNextFrame(): Promise<void> {
  return new Promise((resolve) => {
    window.requestAnimationFrame(() => {
      resolve();
    });
  });
}

export function runConversion(icons: ExportedIcon[], context: ConversionContext = {}): void {
  void runConversionAsync(icons, context);
}

async function runConversionAsync(icons: ExportedIcon[], context: ConversionContext): Promise<void> {
  latestConversionJobId += 1;
  const conversionJobId = latestConversionJobId;

  const options = getConvertOptions();
  packageInput.classList.remove("input-error");
  const nextResults: ConvertResultWithSvg[] = [];

  if (!options.packageName) {
    packageInput.classList.add("input-error");
    replaceConversionResults([]);
    setStatus(
      formatPluginError(
        createSettingsError("Package name is required.", "Set a package name in Options and run export again."),
      ),
      "error",
    );
    renderResults([]);
    updateBulkActionState();
    return;
  }

  if (icons.length === 0) {
    replaceConversionResults([]);
    if (context.upstreamError) {
      setStatus(context.upstreamError, "error");
    } else {
      setStatus(
        formatPluginError(
          createSelectionError("No exportable selected icons.", "Select one or more exportable icon nodes in Figma and retry."),
        ),
        "error",
      );
    }
    renderResults([]);
    updateBulkActionState();
    return;
  }

  if (!isConverterReady()) {
    replaceConversionResults([]);
    renderResults([]);
    updateBulkActionState();
    setStatus(formatPluginError(createConverterUnavailableError()), "error");
    return;
  }

  const seenExact = new Set<string>();
  const seenInsensitive = new Set<string>();

  for (let index = 0; index < icons.length; index += 1) {
    if (conversionJobId !== latestConversionJobId) {
      return;
    }

    const icon = icons[index];
    const normalized = normalizeIconName(icon.name);
    const lowered = normalized.toLowerCase();

    if (seenExact.has(normalized)) {
      nextResults.push({
        ok: false,
        iconName: normalized,
        fileName: "",
        content: "",
        error: `Duplicate icon name '${normalized}'.`,
      });
      continue;
    }

    if (seenInsensitive.has(lowered)) {
      nextResults.push({
        ok: false,
        iconName: normalized,
        fileName: "",
        content: "",
        error: `Case-insensitive collision for '${normalized}'.`,
      });
      continue;
    }

    seenExact.add(normalized);
    seenInsensitive.add(lowered);

    const result: ConvertResultWithSvg = { ...convert(icon.svg, icon.name, options), svg: icon.svg };
    nextResults.push(result);

    if ((index + 1) % CONVERSION_CHUNK_SIZE === 0) {
      const progress = Math.min(index + 1, icons.length);
      setStatus(`Converting ${progress}/${icons.length} icons...`, "working");
      await waitForNextFrame();
    }
  }

  if (conversionJobId !== latestConversionJobId) {
    return;
  }

  replaceConversionResults(nextResults);
  renderResults(nextResults);
  updateBulkActionState();

  const successCount = nextResults.filter((item) => item.ok).length;
  const failCount = nextResults.length - successCount;
  const attemptedCount = context.attemptedCount ?? (nextResults.length + (context.exportFailedCount ?? 0));
  const exportFailedCount = context.exportFailedCount ?? Math.max(0, attemptedCount - nextResults.length);

  let statusMessage = "";
  let statusType: StatusType = "ready";

  if (exportFailedCount > 0) {
    statusMessage = `Converted ${successCount}/${attemptedCount} icon(s); ${exportFailedCount} export failure(s).`;
    if (failCount > 0) {
      statusMessage += ` ${failCount} conversion error(s).`;
    }
    statusType = "warning";
  } else if (failCount > 0) {
    statusMessage = `Converted ${successCount}/${nextResults.length} icon(s). ${failCount} error(s).`;
    statusType = "error";
  } else {
    statusMessage = `Converted ${successCount} icon(s) successfully.`;
  }

  setStatus(statusMessage, statusType);
}
