## Useful project gradle commands

## Common

### Verification

- run all tests: `./gradlew test` (JVM; `./gradlew allTests` also runs wasmJs tests and needs a local Chrome)

- check code style: `./gradlew spotlessCheck`

- check ABI: `./gradlew checkKotlinAbi`

### Update

- apply formatting: `./gradlew spotlessApply`

- update ABI: `./gradlew updateKotlinAbi`

### Info

- create html test report: `./gradlew :sdk:test:coverage:koverHtmlReport`

- print test coverage: `./gradlew :sdk:test:coverage:koverLog`

## IDEA Plugin

### Development

Build plugin: `./gradlew buildPlugin`

Run plugin in IDE sandbox: `./gradlew runIde`

### Changelog

Update changelog: `./gradlew tools:idea-plugin:patchChangelog`

## WEB

- Run WASM: `./gradlew tools:compose-app:wasmJsBrowserDevelopmentRun`
