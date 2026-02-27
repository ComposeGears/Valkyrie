# Valkyrie Figma Plugin (Simple Mode)

This package contains a Figma plugin shell for exporting selected icons into Kotlin `ImageVector` source.

## Status

- UI + selection export flow implemented.
- Converter runtime is injected into `dist/ui.html` during build.
- Copy and download actions are both supported.

## Scripts

- `pnpm build:converter` - compile Kotlin/Wasm converter executable assets
- `pnpm build` - build plugin assets into `dist/`
- `pnpm build:all` - build converter + plugin assets
- `pnpm watch` - watch mode for development
- `pnpm typecheck` - TypeScript checks

## Rerun in Figma

1. Run `pnpm build:all`
2. In Figma desktop, open `Plugins -> Development -> Reload plugins`
3. Reopen `Valkyrie ImageVector Export`

## Files

- `manifest.json` - Figma plugin manifest
- `src/code.ts` - plugin main thread (selection and SVG export)
- `src/ui.ts` - plugin UI logic (conversion and result rendering)
- `src/converterAdapter.ts` - runtime bridge to Wasm converter

## Runtime hookup

`pnpm build` reads these converter outputs:

- `valkyrie-components-converter-figma.mjs`
- `valkyrie-components-converter-figma.uninstantiated.mjs`
- `valkyrie-components-converter-figma.wasm`

Then build-time injection inlines a Wasm bridge and exposes:

- `window.ValkyrieFigmaWasmConverter.convertSvg(...)`
- `window.ValkyrieFigmaWasmConverter.normalizeIconName(...)`

This avoids external script loading issues in `figma.showUI(__html__)`.

If converter artifacts are missing, build prints warnings and the UI reports that the runtime is not loaded.
