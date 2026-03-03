import type { ConvertResultWithSvg } from "../core/types";
import { resultsContainer, emptyState, emptyStateTitle, emptyStateDescription, selectionPreview, mainScroll } from "../core/dom";
import { escapeHtml, escapeAttr, copyText, flashButton, toBase64Utf8 } from "../core/utils";
import { setStatus } from "../core/status";
import { highlightKotlin } from "./highlight";

const EMPTY_TITLE_DEFAULT = "No icons exported yet";
const EMPTY_MESSAGE_DEFAULT = "Select one or more icons in Figma to generate Kotlin ImageVector code automatically.";
const EMPTY_TITLE_LOADING = "Generating code...";
const EMPTY_MESSAGE_LOADING = "Exporting your selected icon(s).";
const EMPTY_TITLE_AUTO_EXPORT_OFF = "Auto export is off";
const EMPTY_MESSAGE_AUTO_EXPORT_OFF = "Select icons and click Export.";
const LARGE_BATCH_COLLAPSE_THRESHOLD = 20;
const CODE_RENDER_BATCH_SIZE = 20;
const EXPAND_COLLAPSE_BATCH_SIZE = 80;

let activePreviewObserver: IntersectionObserver | null = null;
let activeCodeObserver: IntersectionObserver | null = null;
let queuedCodeRenderTasks: Array<() => void> = [];
let codeRenderFrameId: number | null = null;
let expanderBatchFrameId: number | null = null;
let activeExpanderBatchToken = 0;
let loadingResultsVisible = false;

function clearCodeRenderQueue(): void {
  queuedCodeRenderTasks = [];
  if (codeRenderFrameId !== null) {
    window.cancelAnimationFrame(codeRenderFrameId);
    codeRenderFrameId = null;
  }
}

function flushCodeRenderQueue(): void {
  codeRenderFrameId = null;
  let processed = 0;
  while (queuedCodeRenderTasks.length > 0 && processed < CODE_RENDER_BATCH_SIZE) {
    const task = queuedCodeRenderTasks.shift();
    task?.();
    processed += 1;
  }

  if (queuedCodeRenderTasks.length > 0) {
    codeRenderFrameId = window.requestAnimationFrame(flushCodeRenderQueue);
  }
}

function enqueueCodeRenderTask(task: () => void): void {
  queuedCodeRenderTasks.push(task);
  if (codeRenderFrameId === null) {
    codeRenderFrameId = window.requestAnimationFrame(flushCodeRenderQueue);
  }
}

function applyExpanderBatch(
  expanders: Array<{ expand: (renderCode: boolean) => void; collapse: () => void }>,
  action: "expand" | "collapse",
  renderCodeOnExpand = false,
): void {
  activeExpanderBatchToken += 1;
  const token = activeExpanderBatchToken;
  if (expanderBatchFrameId !== null) {
    window.cancelAnimationFrame(expanderBatchFrameId);
    expanderBatchFrameId = null;
  }

  let index = 0;

  const runBatch = (): void => {
    if (token !== activeExpanderBatchToken) {
      expanderBatchFrameId = null;
      return;
    }

    const end = Math.min(index + EXPAND_COLLAPSE_BATCH_SIZE, expanders.length);
    while (index < end) {
      if (action === "expand") {
        expanders[index].expand(renderCodeOnExpand);
      } else {
        expanders[index].collapse();
      }
      index += 1;
    }

    if (index < expanders.length) {
      expanderBatchFrameId = window.requestAnimationFrame(runBatch);
      return;
    }

    expanderBatchFrameId = null;
  };

  runBatch();
}

export function renderLoadingResults(selectedCount: number): void {
  loadingResultsVisible = true;
  resultsContainer.classList.remove("single-result");
  resultsContainer.innerHTML = "";
  emptyState.classList.add("hidden");

  const card = document.createElement("section");
  card.className = "result-card loading-card";

  const header = document.createElement("div");
  header.className = "card-header";

  const info = document.createElement("div");
  info.className = "card-info";

  const title = document.createElement("h3");
  title.textContent = selectedCount > 1 ? `Exporting ${selectedCount} icons...` : "Exporting icon...";
  info.appendChild(title);

  const filename = document.createElement("span");
  filename.className = "filename";
  filename.textContent = "Preparing Kotlin output";
  info.appendChild(filename);

  header.appendChild(info);
  card.appendChild(header);

  const body = document.createElement("div");
  body.className = "card-code";
  body.innerHTML =
    `<div class="loading-lines">` +
    `<div class="loading-line w-80"></div>` +
    `<div class="loading-line w-60"></div>` +
    `<div class="loading-line w-90"></div>` +
    `<div class="loading-line w-40"></div>` +
    `</div>`;
  card.appendChild(body);

  resultsContainer.appendChild(card);
}

export function updateSelectionPreview(count: number, names: string[]): void {
  if (count === 0) {
    selectionPreview.innerHTML = `<div class="selection-empty">No icons selected</div>`;
    return;
  }

  const label = count === 1 ? "icon" : "icons";
  const nameList = names.join(", ") + (count > names.length ? `, +${count - names.length} more` : "");

  selectionPreview.innerHTML =
    `<div class="selection-count"><span class="num">${count}</span> ${label} selected</div>` +
    `<div class="selection-names" title="${escapeAttr(nameList)}">${escapeHtml(nameList)}</div>`;
}

export function renderResults(results: ConvertResultWithSvg[]): void {
  loadingResultsVisible = false;
  activeExpanderBatchToken += 1;
  if (expanderBatchFrameId !== null) {
    window.cancelAnimationFrame(expanderBatchFrameId);
    expanderBatchFrameId = null;
  }
  const previousMainScrollTop = mainScroll.scrollTop;
  const previousMainScrollLeft = mainScroll.scrollLeft;
  clearCodeRenderQueue();
  if (activePreviewObserver) {
    activePreviewObserver.disconnect();
    activePreviewObserver = null;
  }
  if (activeCodeObserver) {
    activeCodeObserver.disconnect();
    activeCodeObserver = null;
  }
  resultsContainer.classList.toggle("single-result", results.length === 1);
  const previousScrollState = new Map<string, { top: number; left: number }>();
  const existingCards = resultsContainer.querySelectorAll(".result-card");
  existingCards.forEach((cardNode) => {
    const existingCard = cardNode as HTMLElement;
    const key = existingCard.dataset.resultKey;
    if (!key) {
      return;
    }

    const codeWrapper = existingCard.querySelector(".card-code") as HTMLElement | null;
    if (!codeWrapper) {
      return;
    }

    previousScrollState.set(key, {
      top: codeWrapper.scrollTop,
      left: codeWrapper.scrollLeft,
    });
  });

  resultsContainer.innerHTML = "";

  if (results.length === 0) {
    emptyState.classList.remove("hidden");
    mainScroll.scrollTop = previousMainScrollTop;
    mainScroll.scrollLeft = previousMainScrollLeft;
    return;
  }

  emptyState.classList.add("hidden");

  const successfulCount = results.filter((result) => result.success).length;
  const showExpandControls = successfulCount > 1;
  const collapseByDefault = successfulCount >= LARGE_BATCH_COLLAPSE_THRESHOLD;
  const expanders: Array<{ expand: (renderCode: boolean) => void; collapse: () => void }> = [];
  const codeRenderers = new WeakMap<HTMLElement, () => void>();
  const previewObserver = collapseByDefault && typeof IntersectionObserver !== "undefined"
    ? new IntersectionObserver((entries, observer) => {
      for (const entry of entries) {
        if (!entry.isIntersecting) {
          continue;
        }

        const image = entry.target as HTMLImageElement;
        const svg = image.dataset.svg;
        if (svg) {
          image.src = "data:image/svg+xml;base64," + toBase64Utf8(svg);
          delete image.dataset.svg;
        }
        observer.unobserve(image);
      }
    }, {
      root: mainScroll,
      rootMargin: "200px",
      threshold: 0,
    })
    : null;
  activePreviewObserver = previewObserver;

  const codeObserver = collapseByDefault && typeof IntersectionObserver !== "undefined"
    ? new IntersectionObserver((entries) => {
      for (const entry of entries) {
        if (!entry.isIntersecting) {
          continue;
        }

        const codeWrapper = entry.target as HTMLElement;
        const renderCode = codeRenderers.get(codeWrapper);
        if (renderCode && codeWrapper.dataset.expanded === "true") {
          enqueueCodeRenderTask(renderCode);
        }
      }
    }, {
      root: mainScroll,
      rootMargin: "1200px",
      threshold: 0,
    })
    : null;
  activeCodeObserver = codeObserver;

  if (showExpandControls) {
    const toolbar = document.createElement("div");
    toolbar.className = "results-toolbar";

    const label = document.createElement("span");
    label.className = "results-toolbar-label";
    label.textContent = collapseByDefault ? `Large batch (${successfulCount} files)` : `${successfulCount} files`;
    toolbar.appendChild(label);

    const controls = document.createElement("div");
    controls.className = "results-toolbar-actions";

    const expandAllButton = document.createElement("button");
    expandAllButton.className = "card-btn";
    expandAllButton.textContent = "Expand all";
    expandAllButton.addEventListener("click", () => {
      applyExpanderBatch(expanders, "expand", !codeObserver);
    });
    controls.appendChild(expandAllButton);

    const collapseAllButton = document.createElement("button");
    collapseAllButton.className = "card-btn";
    collapseAllButton.textContent = "Collapse all";
    collapseAllButton.addEventListener("click", () => {
      applyExpanderBatch(expanders, "collapse");
    });
    controls.appendChild(collapseAllButton);

    toolbar.appendChild(controls);
    resultsContainer.appendChild(toolbar);
  }

  for (const result of results) {
    const resultKey = `${result.success ? "ok" : "error"}:${result.iconName}:${result.fileName}`;
    const card = document.createElement("section");
    card.className = "result-card";
    card.dataset.resultKey = resultKey;

    const header = document.createElement("div");
    header.className = "card-header";

    if (result.svg) {
      const preview = document.createElement("div");
      preview.className = "card-preview";
      const img = document.createElement("img");
      img.alt = result.iconName;
      if (previewObserver) {
        img.dataset.svg = result.svg;
        previewObserver.observe(img);
      } else {
        img.src = "data:image/svg+xml;base64," + toBase64Utf8(result.svg);
      }
      preview.appendChild(img);
      header.appendChild(preview);
    }

    const info = document.createElement("div");
    info.className = "card-info";
    const title = document.createElement("h3");
    title.textContent = result.iconName;
    info.appendChild(title);

    if (result.success && result.fileName) {
      const filename = document.createElement("span");
      filename.className = "filename";
      filename.textContent = result.fileName;
      info.appendChild(filename);
    }

    header.appendChild(info);

    let codeWrapperForCard: HTMLDivElement | null = null;

    if (result.success) {
      const actions = document.createElement("div");
      actions.className = "card-actions";

      let codeWrapper: HTMLDivElement | null = null;
      let codeRendered = false;
      const ensureCodeRendered = (): void => {
        if (!codeWrapper || codeRendered) {
          return;
        }

        const pre = document.createElement("pre");
        const code = document.createElement("code");
        code.innerHTML = highlightKotlin(result.code);
        pre.appendChild(code);
        codeWrapper.appendChild(pre);
        codeRendered = true;
      };

      const requestCodeRender = (): void => {
        if (!codeWrapper || codeRendered) {
          return;
        }

        if (collapseByDefault) {
          enqueueCodeRenderTask(() => {
            if (!codeWrapper || codeRendered || codeWrapper.dataset.expanded !== "true") {
              return;
            }
            ensureCodeRendered();
          });
          return;
        }

        ensureCodeRendered();
      };

      const setExpanded = (expanded: boolean, button: HTMLButtonElement | null, shouldRenderCode: boolean): void => {
        if (!codeWrapper) {
          return;
        }

        codeWrapper.dataset.expanded = expanded ? "true" : "false";

        if (expanded) {
          if (shouldRenderCode) {
            requestCodeRender();
          }
          codeWrapper.classList.remove("hidden");
        } else {
          codeWrapper.classList.add("hidden");
        }

        if (button) {
          button.textContent = expanded ? "Collapse" : "Expand";
        }
      };

      const copyButton = document.createElement("button");
      copyButton.className = "card-btn";
      copyButton.textContent = "Copy";
      copyButton.addEventListener("click", async () => {
        const copied = await copyText(result.code);
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
        const blob = new Blob([result.code], { type: "text/plain;charset=utf-8" });
        const url = URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.download = result.fileName;
        link.click();
        URL.revokeObjectURL(url);
        flashButton(downloadButton, "Done!");
      });
      actions.appendChild(downloadButton);

      let toggleButton: HTMLButtonElement | null = null;
      if (showExpandControls) {
        toggleButton = document.createElement("button");
        toggleButton.className = "card-btn";
        toggleButton.textContent = "Expand";
        toggleButton.addEventListener("click", () => {
          if (!codeWrapper) {
            return;
          }

          const willExpand = codeWrapper.classList.contains("hidden");
          setExpanded(willExpand, toggleButton, willExpand);
        });
        actions.appendChild(toggleButton);
      }

      header.appendChild(actions);

      codeWrapper = document.createElement("div");
      codeWrapper.className = "card-code";
      codeWrapperForCard = codeWrapper;
      codeRenderers.set(codeWrapper, ensureCodeRendered);
      if (codeObserver) {
        codeObserver.observe(codeWrapper);
      }

      if (showExpandControls) {
        const startExpanded = !collapseByDefault;
        setExpanded(startExpanded, toggleButton, startExpanded);
        expanders.push({
          expand: (renderCode: boolean) => setExpanded(true, toggleButton, renderCode),
          collapse: () => setExpanded(false, toggleButton, false),
        });
      } else {
        codeWrapper.dataset.expanded = "true";
        requestCodeRender();
      }
    }

    card.appendChild(header);

    if (codeWrapperForCard) {
      card.appendChild(codeWrapperForCard);
    }

    if (!result.success) {
      const error = document.createElement("div");
      error.className = "card-error";
      error.textContent = result.error ?? "Unknown conversion error";
      card.appendChild(error);
    }

    resultsContainer.appendChild(card);

    const previous = previousScrollState.get(resultKey);
    if (previous) {
      const codeWrapper = card.querySelector(".card-code") as HTMLElement | null;
      if (codeWrapper) {
        codeWrapper.scrollTop = previous.top;
        codeWrapper.scrollLeft = previous.left;
      }
    }
  }

  mainScroll.scrollTop = previousMainScrollTop;
  mainScroll.scrollLeft = previousMainScrollLeft;
}

export function showLoadingEmptyState(): void {
  emptyStateTitle.textContent = EMPTY_TITLE_LOADING;
  emptyStateDescription.textContent = EMPTY_MESSAGE_LOADING;
}

export function showDefaultEmptyState(): void {
  emptyStateTitle.textContent = EMPTY_TITLE_DEFAULT;
  emptyStateDescription.textContent = EMPTY_MESSAGE_DEFAULT;
}

export function showAutoExportDisabledEmptyState(): void {
  emptyStateTitle.textContent = EMPTY_TITLE_AUTO_EXPORT_OFF;
  emptyStateDescription.textContent = EMPTY_MESSAGE_AUTO_EXPORT_OFF;
}

export function isLoadingResultsVisible(): boolean {
  return loadingResultsVisible;
}
