import { zipSync, strToU8 } from "fflate";
import { copyAllButton, downloadAllButton } from "./dom";
import { getSuccessfulConversionResults, hasSuccessfulConversionResults } from "./state";
import { setStatus } from "./status";
import { copyText, flashButton } from "./utils";

export function initializeBulkActions(): void {
  copyAllButton.addEventListener("click", async () => {
    const successful = getSuccessfulConversionResults();
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
    const successful = getSuccessfulConversionResults();
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
}

export function updateBulkActionState(): void {
  const hasSuccessful = hasSuccessfulConversionResults();
  copyAllButton.disabled = !hasSuccessful;
  downloadAllButton.disabled = !hasSuccessful;
}
