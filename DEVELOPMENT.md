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

## Plugin

### Development

Build plugin: `./gradlew buildPlugin`

Run plugin in IDE sandbox: `./gradlew runIde`

### Changelog

Print current changelog: `./gradlew getChangelog`

Update changelog with new version: `./gradlew patchChangelog`

## CLI

Build CLI: `./gradlew buildCLI`

Changelog: `./gradlew tools:cli:patchChangelog`

## Gradle Plugin

Test plugin locally: `./gradlew :tools:gradle-plugin:publishToMavenLocal`

## WEB

- Run WASM: `./gradlew tools:compose-app:wasmJsBrowserDevelopmentRun`
