import { build, context } from "esbuild";
import { cp, mkdir, readFile, rm, writeFile } from "node:fs/promises";
import { resolve } from "node:path";

const watch = process.argv.includes("--watch");
const root = resolve(process.cwd());
const srcDir = resolve(root, "src");
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
  entryPoints: [resolve(srcDir, "code.ts")],
  outfile: resolve(distDir, "code.js"),
  format: "iife",
  platform: "browser",
};

const srcUiHtmlPath = resolve(srcDir, "ui.html");
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

    wasmBridgeScript = uninstantiated
      .replace("export async function instantiate", "async function instantiate")
      .replace(
        "fetch(new URL('./valkyrie-sdk-figma-converter.wasm',import.meta.url).href)",
        `fetch('data:application/wasm;base64,${wasmBase64}')`,
      )
      .concat(
        "\nconst __converter = (await instantiate({})).exports;\n" +
          "window.ValkyrieFigmaWasmConverter = { convertSvg: __converter.convertSvg, normalizeIconName: __converter.normalizeIconName };\n",
      );
  } catch {
    process.stderr.write(
      "Converter artifacts missing. Run ../../gradlew :sdk:figma:converter:compileProductionExecutableKotlinWasmJs first.\n",
    );
  }

  return wasmBridgeScript;
}

const wasmBridgeScript = await buildWasmBridgeScript();

async function writeInlinedUiHtml() {
  const [uiHtml, uiScript] = await Promise.all([
    readFile(srcUiHtmlPath, "utf8"),
    readFile(distUiJsPath, "utf8"),
  ]);

  const inlinedHtml = uiHtml
    .replace("/*__WASM_BRIDGE__*/", escapeScriptTag(wasmBridgeScript))
    .replace("/*__UI_SCRIPT__*/", escapeScriptTag(uiScript));

  await writeFile(distUiHtmlPath, inlinedHtml, "utf8");
}

const uiConfig = {
  ...sharedOptions,
  entryPoints: [resolve(srcDir, "ui.ts")],
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
            process.stderr.write(`Failed to inline ui.html: ${String(error)}\n`);
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

const converterFiles = [
  "valkyrie-sdk-figma-converter.mjs",
  "valkyrie-sdk-figma-converter.uninstantiated.mjs",
  "valkyrie-sdk-figma-converter.wasm",
];

const staleConverterFiles = [
  "valkyrie-components-converter-figma.mjs",
  "valkyrie-components-converter-figma.uninstantiated.mjs",
  "valkyrie-components-converter-figma.wasm",
];

await Promise.all(staleConverterFiles.map((file) => rm(resolve(distDir, file), { force: true })));

for (const file of converterFiles) {
  try {
    await cp(resolve(converterDistDir, file), resolve(distDir, file));
  } catch {
    process.stderr.write(
      `Missing converter artifact: ${file}. Run ../../gradlew :sdk:figma:converter:compileProductionExecutableKotlinWasmJs first.\n`,
    );
  }
}

if (!watch) {
  process.stdout.write("Built Figma plugin assets.\n");
}
