import { build, context } from "esbuild";
import { mkdir, readFile, writeFile } from "node:fs/promises";
import { resolve } from "node:path";

const watch = process.argv.includes("--watch");
const root = resolve(process.cwd());
const srcDir = resolve(root, "src");
const mainDir = resolve(srcDir, "main");
const uiDir = resolve(srcDir, "ui");
const distDir = resolve(root, "dist");
const repoRoot = resolve(root, "../..");
const converterDistDir = resolve(
  repoRoot,
  "sdk/figma/converter/build/compileSync/wasmJs/main/productionExecutable/kotlin",
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
  // Figma's plugin sandbox uses a limited JS engine — target ES2017 to
  // ensure operators like ?? and ?. are compiled down.
  target: "es2017",
  entryPoints: [resolve(mainDir, "code.ts")],
  outfile: resolve(distDir, "code.js"),
  format: "iife",
  platform: "browser",
};

const srcUiHtmlPath = resolve(uiDir, "ui.html");
const distUiJsPath = resolve(distDir, "ui.js");
const distUiHtmlPath = resolve(distDir, "ui.html");

const escapeScriptTag = (text) => text.replaceAll("</script", "<\\/script");

async function buildWasmBridgeScript() {
  let wasmBridgeScript = "window.ValkyrieFigmaWasmConverter = undefined;";

  try {
    const uninstantiatedPath = resolve(converterDistDir, "valkyrie-sdk-figma-converter.uninstantiated.mjs");
    const wasmPath = resolve(converterDistDir, "valkyrie-sdk-figma-converter.wasm");
    const uninstantiated = await readFile(uninstantiatedPath, "utf8");
    const wasmBytes = await readFile(wasmPath);
    const wasmBase64 = wasmBytes.toString("base64");

    const instantiateMarker = "export async function instantiate";
    const fetchMarker = "fetch(new URL('./valkyrie-sdk-figma-converter.wasm',import.meta.url).href)";

    if (!uninstantiated.includes(instantiateMarker)) {
      throw new Error(`Failed to patch converter bridge: missing marker '${instantiateMarker}'.`);
    }
    if (!uninstantiated.includes(fetchMarker)) {
      throw new Error(`Failed to patch converter bridge: missing marker '${fetchMarker}'.`);
    }

    wasmBridgeScript = uninstantiated
      .replace(instantiateMarker, "async function instantiate")
      .replace(fetchMarker, `fetch('data:application/wasm;base64,${wasmBase64}')`)
      .concat(
        "\nconst __converter = (await instantiate({})).exports;\n" +
          "window.ValkyrieFigmaWasmConverter = { convertSvg: __converter.convertSvg, normalizeIconName: __converter.normalizeIconName };\n",
      );
  } catch (error) {
    if (error && typeof error === "object" && "code" in error && error.code === "ENOENT") {
      process.stderr.write(
        "Converter artifacts missing. Run ../../gradlew :sdk:figma:converter:compileProductionExecutableKotlinWasmJs first.\n",
      );
    } else {
      throw error;
    }
  }

  return wasmBridgeScript;
}

async function writeInlinedUiHtml() {
  const wasmBridgeScript = await buildWasmBridgeScript();
  const [uiHtml, uiScript] = await Promise.all([
    readFile(srcUiHtmlPath, "utf8"),
    readFile(distUiJsPath, "utf8"),
  ]);

  if (!uiHtml.includes("/*__WASM_BRIDGE__*/") || !uiHtml.includes("/*__UI_SCRIPT__*/")) {
    throw new Error("ui.html placeholders are missing: /*__WASM_BRIDGE__*/ and/or /*__UI_SCRIPT__*/");
  }

  const inlinedHtml = uiHtml
    .replace("/*__WASM_BRIDGE__*/", escapeScriptTag(wasmBridgeScript))
    .replace("/*__UI_SCRIPT__*/", escapeScriptTag(uiScript));

  await writeFile(distUiHtmlPath, inlinedHtml, "utf8");
}

const uiConfig = {
  ...sharedOptions,
  entryPoints: [resolve(uiDir, "ui.ts")],
  outfile: resolve(distDir, "ui.js"),
  format: "iife",
  platform: "browser",
  plugins: [
    {
      name: "inline-ui-html",
      setup(buildCtx) {
        buildCtx.onEnd(async (result) => {
          if (result.errors.length > 0) {
            return;
          }

          try {
            await writeInlinedUiHtml();
            if (watch) {
              process.stdout.write("Updated dist/ui.html\n");
            }
          } catch (error) {
            const message = `Failed to inline ui.html: ${String(error)}\n`;
            if (watch) {
              process.stderr.write(message);
              return;
            }
            throw error;
          }
        });
      },
    },
  ],
};

if (watch) {
  const [codeCtx, uiCtx] = await Promise.all([context(codeConfig), context(uiConfig)]);
  await Promise.all([codeCtx.watch(), uiCtx.watch()]);
} else {
  await Promise.all([build(codeConfig), build(uiConfig)]);
}

if (!watch) {
  process.stdout.write("Built Figma plugin assets.\n");
}
