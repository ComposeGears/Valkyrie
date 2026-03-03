## Useful project gradle commands

## Common

### Verification

- run all tests: `./gradlew test`

- check code style: `./gradlew spotlessCheck`

- check ABI: `./gradlew checkLegacyAbi`

### Update

- apply formatting: `./gradlew spotlessApply`

- update ABI: `./gradlew updateLegacyAbi`

### Info

- create html test report: `./gradlew components:test:coverage:koverHtmlReport`

- print test coverage: `./gradlew components:test:coverage:koverLog`

## IDEA Plugin

### Development

Build plugin: `./gradlew buildPlugin`

Run plugin in IDE sandbox: `./gradlew runIde`

### Changelog

Update changelog: `./gradlew tools:idea-plugin:patchChangelog`

## WEB

- Run WASM: `./gradlew tools:compose-app:wasmJsBrowserDevelopmentRun`

## FIGMA Plugin (Simple mode)

- Build converter for Wasm executable: `./gradlew :components:converter:figma:compileProductionExecutableKotlinWasmJs`
- Install plugin package deps (pnpm): `pnpm install` (run in `tools/figma-plugin`)
- Build plugin assets: `pnpm build` (run in `tools/figma-plugin`)
- Build converter + plugin assets: `pnpm build:all` (run in `tools/figma-plugin`)
- Watch plugin assets: `pnpm watch` (run in `tools/figma-plugin`)
- Reload in Figma after build: `Plugins -> Development -> Reload plugins`
