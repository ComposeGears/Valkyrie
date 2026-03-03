import type { MainToUiMessage, UiToMainMessage } from "./messages";

export function sendMessage(message: UiToMainMessage): void {
  parent.postMessage({ pluginMessage: message }, "*");
}

export function onMessage(handler: (message: MainToUiMessage) => void): () => void {
  const listener = (event: MessageEvent<{ pluginMessage?: MainToUiMessage }>) => {
    const message = event.data?.pluginMessage;
    if (message) {
      handler(message);
    }
  };
  window.addEventListener("message", listener);
  return () => {
    window.removeEventListener("message", listener);
  };
}

export function onError(handler: (event: ErrorEvent) => void): void {
  window.addEventListener("error", handler);
}
