import { build, context } from "esbuild";
import { cp, mkdir, readFile, writeFile } from "node:fs/promises";
import { resolve } from "node:path";

const watch = process.argv.includes("--watch");
const root = resolve(process.cwd());
const srcDir = resolve(root, "src");
const distDir = resolve(root, "dist");
const repoRoot = resolve(root, "../..");
const converterDistDir = resolve(
  repoRoot,
  "components/converter/figma/build/compileSync/wasmJs/main/productionExecutable/kotlin",
);

await mkdir(distDir, { recursive: true });

const sharedOptions = {
  bundle: true,
  sourcemap: true,
  target: "es2020",
  logLevel: "info",
};

const codeConfig = {
  ...sharedOptions,
  entryPoints: [resolve(srcDir, "code.ts")],
  outfile: resolve(distDir, "code.js"),
  format: "iife",
  platform: "browser",
};

const uiConfig = {
  ...sharedOptions,
  entryPoints: [resolve(srcDir, "ui.ts")],
  outfile: resolve(distDir, "ui.js"),
  format: "iife",
  platform: "browser",
};

if (watch) {
  const [codeCtx, uiCtx] = await Promise.all([context(codeConfig), context(uiConfig)]);
  await Promise.all([codeCtx.watch(), uiCtx.watch()]);
} else {
  await Promise.all([build(codeConfig), build(uiConfig)]);
}

const srcUiHtmlPath = resolve(srcDir, "ui.html");
const distUiJsPath = resolve(distDir, "ui.js");
const distUiHtmlPath = resolve(distDir, "ui.html");

const escapeScriptTag = (text) => text.replaceAll("</script", "<\\/script");

let uiHtml = await readFile(srcUiHtmlPath, "utf8");
const uiScript = await readFile(distUiJsPath, "utf8");
let wasmBridgeScript = "window.ValkyrieFigmaWasmConverter = undefined;";

try {
  const uninstantiatedPath = resolve(converterDistDir, "valkyrie-components-converter-figma.uninstantiated.mjs");
  const wasmPath = resolve(converterDistDir, "valkyrie-components-converter-figma.wasm");
  const uninstantiated = await readFile(uninstantiatedPath, "utf8");
  const wasmBytes = await readFile(wasmPath);
  const wasmBase64 = wasmBytes.toString("base64");

  wasmBridgeScript = uninstantiated
    .replace("export async function instantiate", "async function instantiate")
    .replace(
      "fetch(new URL('./valkyrie-components-converter-figma.wasm',import.meta.url).href)",
      `fetch('data:application/wasm;base64,${wasmBase64}')`,
    )
    .concat(
      "\nconst __converter = (await instantiate({})).exports;\n" +
        "window.ValkyrieFigmaWasmConverter = { convertSvg: __converter.convertSvg, normalizeIconName: __converter.normalizeIconName };\n",
    );
} catch {
  process.stderr.write(
    "Converter artifacts missing. Run ../../gradlew :components:converter:figma:compileProductionExecutableKotlinWasmJs first.\n",
  );
}

uiHtml = uiHtml
  .replace("/*__WASM_BRIDGE__*/", escapeScriptTag(wasmBridgeScript))
  .replace("/*__UI_SCRIPT__*/", escapeScriptTag(uiScript));

await writeFile(distUiHtmlPath, uiHtml, "utf8");

const converterFiles = [
  "valkyrie-components-converter-figma.mjs",
  "valkyrie-components-converter-figma.uninstantiated.mjs",
  "valkyrie-components-converter-figma.wasm",
];

for (const file of converterFiles) {
  try {
    await cp(resolve(converterDistDir, file), resolve(distDir, file));
  } catch {
    process.stderr.write(
      `Missing converter artifact: ${file}. Run ../../gradlew :components:converter:figma:compileProductionExecutableKotlinWasmJs first.\n`,
    );
  }
}

if (!watch) {
  process.stdout.write("Built Figma plugin assets.\n");
}
