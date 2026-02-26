import { convert, isConverterReady, normalizeIconName, type ConvertOptions, type ConvertResult } from "./converterAdapter";

type ExportedIcon = {
  id: string;
  name: string;
  svg: string;
};

type ConversionReadyMessage = {
  type: "conversion-ready";
  icons: ExportedIcon[];
  error?: string;
};

type ConversionStartedMessage = {
  type: "conversion-started";
  selectedCount: number;
};

type PluginMessage = ConversionReadyMessage | ConversionStartedMessage;

const runButton = document.querySelector<HTMLButtonElement>("#run")!;
const copyAllButton = document.querySelector<HTMLButtonElement>("#copy-all")!;
const statusText = document.querySelector<HTMLParagraphElement>("#status")!;
const packageInput = document.querySelector<HTMLInputElement>("#package")!;
const useComposeColorsInput = document.querySelector<HTMLInputElement>("#compose-colors")!;
const addTrailingCommaInput = document.querySelector<HTMLInputElement>("#trailing-comma")!;
const useExplicitModeInput = document.querySelector<HTMLInputElement>("#explicit-mode")!;
const usePathDataStringInput = document.querySelector<HTMLInputElement>("#path-data")!;
const autoMirrorInput = document.querySelector<HTMLSelectElement>("#auto-mirror")!;
const resultsContainer = document.querySelector<HTMLDivElement>("#results")!;

const conversionResults: ConvertResult[] = [];
let pendingTimeoutId: number | null = null;

statusText.textContent = "UI script loaded. Ready.";

runButton.addEventListener("click", () => {
  statusText.textContent = "Requesting export from Figma...";
  if (pendingTimeoutId !== null) {
    window.clearTimeout(pendingTimeoutId);
  }
  pendingTimeoutId = window.setTimeout(() => {
    statusText.textContent = "No response from Figma main thread yet. Check plugin console for errors.";
  }, 5000);
  parent.postMessage({ pluginMessage: { type: "run-conversion" } }, "*");
});

copyAllButton.addEventListener("click", async () => {
  const successful = conversionResults.filter((item) => item.ok);
  if (successful.length === 0) {
    return;
  }
  const text = successful.map((item) => `// ${item.fileName}\n${item.content}`).join("\n\n");
  const copied = await copyText(text);
  statusText.textContent = copied
    ? `Copied ${successful.length} generated file(s).`
    : "Copy failed. Use Download instead.";
});

window.onmessage = (event: MessageEvent<{ pluginMessage: PluginMessage }>) => {
  const message = event.data.pluginMessage;
  if (!message) {
    return;
  }

  if (message.type === "conversion-started") {
    statusText.textContent = `Exporting ${message.selectedCount} selected node(s)...`;
    return;
  }

  if (message.type !== "conversion-ready") {
    return;
  }

  if (pendingTimeoutId !== null) {
    window.clearTimeout(pendingTimeoutId);
    pendingTimeoutId = null;
  }

  if (message.error) {
    statusText.textContent = message.error;
  }

  runConversion(message.icons);
};

window.addEventListener("error", (event) => {
  statusText.textContent = `UI error: ${event.message}`;
});

function runConversion(icons: ExportedIcon[]): void {
  const options = getOptions();
  conversionResults.length = 0;

  if (!options.packageName) {
    statusText.textContent = "Package name is required.";
    return;
  }

  if (icons.length === 0) {
    statusText.textContent = "No exportable selected icons.";
    resultsContainer.innerHTML = "";
    return;
  }

  if (!isConverterReady()) {
    statusText.textContent = "Converter runtime not loaded in UI. Check ui.html converter script hook.";
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
    conversionResults.push(convert(icon.svg, icon.name, options));
  }

  renderResults(conversionResults);

  const successCount = conversionResults.filter((item) => item.ok).length;
  const failCount = conversionResults.length - successCount;
  statusText.textContent = `Converted ${successCount}/${conversionResults.length} icon(s). Errors: ${failCount}.`;
}

function getOptions(): ConvertOptions {
  return {
    packageName: packageInput.value.trim(),
    useComposeColors: useComposeColorsInput.checked,
    addTrailingComma: addTrailingCommaInput.checked,
    useExplicitMode: useExplicitModeInput.checked,
    usePathDataString: usePathDataStringInput.checked,
    indentSize: 4,
    autoMirror: autoMirrorInput.value as "" | "true" | "false",
  };
}

function renderResults(results: ConvertResult[]): void {
  resultsContainer.innerHTML = "";

  for (const result of results) {
    const card = document.createElement("section");
    card.className = "result-card";

    const title = document.createElement("h3");
    title.textContent = result.iconName;
    card.appendChild(title);

    if (!result.ok) {
      const error = document.createElement("p");
      error.className = "error";
      error.textContent = result.error ?? "Unknown conversion error";
      card.appendChild(error);
      resultsContainer.appendChild(card);
      continue;
    }

    const actions = document.createElement("div");
    actions.className = "actions";

    const copyButton = document.createElement("button");
    copyButton.textContent = "Copy";
    copyButton.addEventListener("click", async () => {
      const copied = await copyText(result.content);
      statusText.textContent = copied ? `Copied ${result.fileName}.` : "Copy failed. Use Download instead.";
    });
    actions.appendChild(copyButton);

    const downloadButton = document.createElement("button");
    downloadButton.textContent = "Download";
    downloadButton.addEventListener("click", () => {
      const blob = new Blob([result.content], { type: "text/plain;charset=utf-8" });
      const url = URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = result.fileName;
      link.click();
      URL.revokeObjectURL(url);
    });
    actions.appendChild(downloadButton);

    card.appendChild(actions);

    const code = document.createElement("textarea");
    code.value = result.content;
    code.readOnly = true;
    card.appendChild(code);

    resultsContainer.appendChild(card);
  }
}

async function copyText(text: string): Promise<boolean> {
  try {
    if (navigator.clipboard?.writeText) {
      await navigator.clipboard.writeText(text);
      return true;
    }
  } catch {
    // fallback below
  }

  const area = document.createElement("textarea");
  area.value = text;
  area.setAttribute("readonly", "");
  area.style.position = "fixed";
  area.style.opacity = "0";
  area.style.pointerEvents = "none";
  document.body.appendChild(area);
  area.focus();
  area.select();

  let copied = false;
  try {
    copied = document.execCommand("copy");
  } catch {
    copied = false;
  }

  document.body.removeChild(area);
  return copied;
}
