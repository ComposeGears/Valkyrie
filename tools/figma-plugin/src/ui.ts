import { zipSync, strToU8 } from "fflate";
import { convert, isConverterReady, normalizeIconName } from "./converterAdapter";
import type { ExportedIcon, PluginMessage, ConvertResultWithSvg } from "./types";
import { runButton, copyAllButton, downloadAllButton } from "./dom";
import { conversionResults, setPendingTimeout, clearPendingTimeout } from "./state";
import { getConvertOptions, applySettings, initSettingsListeners } from "./settings";
import { sendMessage, onMessage, onError } from "./api";
import { setStatus } from "./status";
import { updateSelectionPreview, renderResults } from "./render";
import { copyText, flashButton } from "./utils";

initSettingsListeners();
setStatus("Ready");

runButton.addEventListener("click", () => {
  setStatus("Requesting export from Figma...", "working");
  clearPendingTimeout();
  setPendingTimeout(() => {
    setStatus("No response from Figma main thread. Check plugin console.", "error");
  }, 5000);
  sendMessage({ type: "run-conversion" });
});

copyAllButton.addEventListener("click", async () => {
  const successful = conversionResults.filter((item) => item.ok);
  if (successful.length === 0) {
    return;
  }
  const text = successful.map((item) => `// ${item.fileName}\n${item.content}`).join("\n\n");
  const copied = await copyText(text);
  if (copied) {
    flashButton(copyAllButton, "Copied!");
    setStatus(`Copied ${successful.length} generated file(s).`);
  } else {
    setStatus("Copy failed. Use Download instead.", "error");
  }
});

downloadAllButton.addEventListener("click", () => {
  const successful = conversionResults.filter((item) => item.ok);
  if (successful.length === 0) {
    return;
  }

  const files: Record<string, Uint8Array> = {};
  for (const result of successful) {
    files[result.fileName] = strToU8(result.content);
  }

  const zipped = zipSync(files, { level: 6 });
  const blob = new Blob([zipped.buffer as ArrayBuffer], { type: "application/zip" });
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = "valkyrie-icons.zip";
  link.click();
  URL.revokeObjectURL(url);

  flashButton(downloadAllButton, "Downloaded!");
  setStatus(`Downloaded ${successful.length} file(s) as ZIP.`);
});

onMessage((message: PluginMessage) => {
  switch (message.type) {
    case "selection-changed":
      updateSelectionPreview(message.count, message.names);
      break;

    case "settings-loaded":
      if (message.settings) {
        applySettings(message.settings);
      }
      break;

    case "conversion-started":
      setStatus(`Exporting ${message.selectedCount} selected node(s)...`, "working");
      break;

    case "conversion-ready":
      clearPendingTimeout();
      if (message.error) {
        setStatus(message.error, "error");
      }
      runConversion(message.icons);
      break;
  }
});

onError((event) => {
  setStatus(`UI error: ${event.message}`, "error");
});

function runConversion(icons: ExportedIcon[]): void {
  const options = getConvertOptions();
  conversionResults.length = 0;

  if (!options.packageName) {
    setStatus("Package name is required.", "error");
    return;
  }

  if (icons.length === 0) {
    setStatus("No exportable selected icons.", "error");
    renderResults([]);
    return;
  }

  if (!isConverterReady()) {
    setStatus("Converter not loaded. Run pnpm build:all and reload.", "error");
  }

  const seenExact = new Set<string>();
  const seenInsensitive = new Set<string>();

  for (const icon of icons) {
    const normalized = normalizeIconName(icon.name);
    const lowered = normalized.toLowerCase();

    if (seenExact.has(normalized)) {
      conversionResults.push({
        ok: false,
        iconName: normalized,
        fileName: "",
        content: "",
        error: `Duplicate icon name '${normalized}'.`,
      });
      continue;
    }

    if (seenInsensitive.has(lowered)) {
      conversionResults.push({
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
    conversionResults.push(result);
  }

  renderResults(conversionResults);

  const successCount = conversionResults.filter((item) => item.ok).length;
  const failCount = conversionResults.length - successCount;
  const statusMsg = failCount > 0
    ? `Converted ${successCount}/${conversionResults.length} icon(s). ${failCount} error(s).`
    : `Converted ${successCount} icon(s) successfully.`;
  setStatus(statusMsg, failCount > 0 ? "error" : "ready");
}
