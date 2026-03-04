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
