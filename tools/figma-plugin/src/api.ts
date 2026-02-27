import type { PluginMessage } from "./types";

export function sendMessage(message: Record<string, unknown>): void {
  parent.postMessage({ pluginMessage: message }, "*");
}

export function onMessage(handler: (message: PluginMessage) => void): void {
  window.onmessage = (event: MessageEvent<{ pluginMessage: PluginMessage }>) => {
    const message = event.data.pluginMessage;
    if (message) {
      handler(message);
    }
  };
}

export function onError(handler: (event: ErrorEvent) => void): void {
  window.addEventListener("error", handler);
}
