import type { ConvertResultWithSvg } from "./types";
import { resultsContainer, emptyState, selectionPreview } from "./dom";
import { escapeHtml, escapeAttr, copyText, flashButton } from "./utils";
import { setStatus } from "./status";

export function updateSelectionPreview(count: number, names: string[]): void {
  if (count === 0) {
    selectionPreview.innerHTML = `<div class="selection-empty">No nodes selected</div>`;
    return;
  }

  const label = count === 1 ? "1 node" : `${count} nodes`;
  const nameList = names.join(", ") + (count > names.length ? `, +${count - names.length} more` : "");

  selectionPreview.innerHTML =
    `<div class="selection-count"><span class="num">${count}</span> ${label} selected</div>` +
    `<div class="selection-names" title="${escapeAttr(nameList)}">${escapeHtml(nameList)}</div>`;
}

export function renderResults(results: ConvertResultWithSvg[]): void {
  resultsContainer.innerHTML = "";

  if (results.length === 0) {
    emptyState.classList.remove("hidden");
    return;
  }

  emptyState.classList.add("hidden");

  for (const result of results) {
    const card = document.createElement("section");
    card.className = "result-card";

    const header = document.createElement("div");
    header.className = "card-header";

    if (result.svg) {
      const preview = document.createElement("div");
      preview.className = "card-preview";
      const img = document.createElement("img");
      img.src = "data:image/svg+xml;base64," + btoa(unescape(encodeURIComponent(result.svg)));
      img.alt = result.iconName;
      preview.appendChild(img);
      header.appendChild(preview);
    }

    const info = document.createElement("div");
    info.className = "card-info";
    const title = document.createElement("h3");
    title.textContent = result.iconName;
    info.appendChild(title);

    if (result.ok && result.fileName) {
      const filename = document.createElement("span");
      filename.className = "filename";
      filename.textContent = result.fileName;
      info.appendChild(filename);
    }

    header.appendChild(info);

    if (result.ok) {
      const actions = document.createElement("div");
      actions.className = "card-actions";

      const copyButton = document.createElement("button");
      copyButton.className = "card-btn";
      copyButton.textContent = "Copy";
      copyButton.addEventListener("click", async () => {
        const copied = await copyText(result.content);
        if (copied) {
          flashButton(copyButton, "Copied!");
          setStatus(`Copied ${result.fileName}.`);
        } else {
          setStatus("Copy failed. Use Download instead.", "error");
        }
      });
      actions.appendChild(copyButton);

      const downloadButton = document.createElement("button");
      downloadButton.className = "card-btn";
      downloadButton.textContent = "Download";
      downloadButton.addEventListener("click", () => {
        const blob = new Blob([result.content], { type: "text/plain;charset=utf-8" });
        const url = URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.download = result.fileName;
        link.click();
        URL.revokeObjectURL(url);
        flashButton(downloadButton, "Done!");
      });
      actions.appendChild(downloadButton);

      header.appendChild(actions);
    }

    card.appendChild(header);

    if (!result.ok) {
      const error = document.createElement("div");
      error.className = "card-error";
      error.textContent = result.error ?? "Unknown conversion error";
      card.appendChild(error);
    } else {
      const codeWrapper = document.createElement("div");
      codeWrapper.className = "card-code";
      const pre = document.createElement("pre");
      const code = document.createElement("code");
      code.textContent = result.content;
      pre.appendChild(code);
      codeWrapper.appendChild(pre);
      card.appendChild(codeWrapper);
    }

    resultsContainer.appendChild(card);
  }
}
