export function escapeHtml(text: string): string {
  const div = document.createElement("div");
  div.textContent = text;
  return div.innerHTML;
}

export function escapeAttr(text: string): string {
  return text
    .replace(/&/g, "&amp;")
    .replace(/"/g, "&quot;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;");
}

export async function copyText(text: string): Promise<boolean> {
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

const buttonFlashTimeouts = new WeakMap<HTMLButtonElement, number>();

export function flashButton(button: HTMLButtonElement, flashText: string): void {
  const existingTimeout = buttonFlashTimeouts.get(button);
  if (existingTimeout !== undefined) {
    window.clearTimeout(existingTimeout);
  }

  const originalText = button.textContent;
  button.textContent = flashText;
  button.classList.add("copied");
  const timeoutId = window.setTimeout(() => {
    button.textContent = originalText;
    button.classList.remove("copied");
    buttonFlashTimeouts.delete(button);
  }, 2000);
  buttonFlashTimeouts.set(button, timeoutId);
}

export function toBase64Utf8(text: string): string {
  const bytes = new TextEncoder().encode(text);
  let binary = "";

  for (let i = 0; i < bytes.length; i += 1) {
    binary += String.fromCharCode(bytes[i]);
  }

  return btoa(binary);
}
