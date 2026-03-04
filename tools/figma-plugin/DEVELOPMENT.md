# Figma Plugin Development

## Build and run

- Build converter for Wasm executable: `../../gradlew -p ../../ :sdk:figma:converter:compileProductionExecutableKotlinWasmJs`
- Install plugin package deps: `pnpm install`
- Build plugin assets: `pnpm build`
- Build converter + plugin assets: `pnpm build:all`
- Watch plugin assets: `pnpm watch`

## Reload in Figma

- Reload in Figma after build: `Plugins -> Development -> Hot Reload Plugin`
