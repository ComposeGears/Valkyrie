import * as converter from "./valkyrie-components-converter-figma.mjs";

if (typeof converter._initialize === "function") {
  converter._initialize();
}

window.ValkyrieFigmaWasmConverter = {
  convertSvg: converter.convertSvg,
  normalizeIconName: converter.normalizeIconName,
};
