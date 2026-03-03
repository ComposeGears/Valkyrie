# Valkyrie Figma Plugin

This package contains a Figma plugin shell for exporting selected icons into Kotlin `ImageVector` source.

## Status

- UI + selection export flow implemented.
- Auto export is enabled by default and can be toggled off.
- Output format supports both backing property and lazy property generation.
- Plugin UI follows Figma light/dark theme tokens.
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
- `src/main/code.ts` - plugin main thread (selection and SVG export)
- `src/ui/ui.ts` - plugin UI entry and orchestration
- `src/ui/core/` - UI runtime primitives (dom, status, state, api, utils)
- `src/ui/controllers/` - UI request/selection lifecycle controllers
- `src/ui/features/` - conversion, rendering, settings, and bulk actions
- `src/ui/features/converterAdapter.ts` - runtime bridge to Wasm converter
- `src/shared/messages.ts` - typed message contracts between main and UI

## Runtime hookup

`pnpm build` reads these converter outputs:

- `valkyrie-sdk-figma-converter.mjs`
- `valkyrie-sdk-figma-converter.uninstantiated.mjs`
- `valkyrie-sdk-figma-converter.wasm`

Then build-time injection inlines a Wasm bridge and exposes:

- `window.ValkyrieFigmaWasmConverter.convertSvg(...)`
- `window.ValkyrieFigmaWasmConverter.normalizeIconName(...)`

This avoids external script loading issues in `figma.showUI(__html__)`.

If converter artifacts are missing, build prints warnings and the UI reports that the runtime is not loaded.

## UX notes

- Conversion uses request ids so stale responses do not overwrite newer runs.
- Bulk actions are disabled until at least one successful conversion exists.
- For a single converted icon, the code panel expands and increases code font size for readability.
