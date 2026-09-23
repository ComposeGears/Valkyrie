# Valkyrie — AI Agent Guide

## Project Overview

Valkyrie converts SVG/XML icons to Compose `ImageVector` Kotlin code. It ships as four tools sharing the same core
pipeline: **IDEA/Android Studio plugin**, **CLI**, **Gradle plugin**, and a **WASM web app** (WIP).

## Architecture: Data Flow

```
SVG / XML file
    │
    ▼
components:parser  (jvm/svg, jvm/xml, kmp/xml, kmp/svg)
    │  produces ──▶  sdk:ir:core  (IrImageVector, IrVectorNode, IrPathNode, …)
    ▼
components:parser:unified  (SvgXmlParser — single entry point for both formats)
    │
    ▼
components:generator  (kmp/imagevector, jvm/imagevector, iconpack)
    │  produces ──▶  Kotlin source string
    ▼
tools:idea-plugin / tools:cli / tools:gradle-plugin
```

- **`sdk/ir/core`** — Intermediate Representation (IR). All domain types live here (`IrImageVector`, `IrVectorNode`,
  `IrFill`, `IrStroke`, etc.). Parsers produce IR; generators consume IR.
- **`components/parser/unified`** — `SvgXmlParser` is the unified entry point used by all tools.
- **`components/generator/kmp/imagevector`** — KMP-compatible generator (used by web/CLI/Gradle); JVM variant (
  `jvm/imagevector`) uses KotlinPoet.
- **`sdk/shared`** — `ValkyrieMode` enum (`Simple` | `IconPack`) controls generation style across all tools.
- **`build-logic/`** — Convention plugins (`valkyrie.jvm`, `valkyrie.kmp`, `valkyrie.abi`, `valkyrie.compose`,
  `valkyrie.kover`, `valkyrie.wasm.resources`) applied via `alias(libs.plugins.valkyrie.*)`.

## Module Taxonomy

| Prefix         | Purpose                                                                        |
|----------------|--------------------------------------------------------------------------------|
| `sdk/*`        | Reusable building blocks (IR, Compose UI, IntelliJ PSI, utils)                 |
| `components/*` | Core pipeline: parsers & generators                                            |
| `tools/*`      | End-user deliverables: idea-plugin, cli, gradle-plugin, compose-app            |

Public ABI snapshots (`<module>/api/*.api`) are tracked in 31 modules across all three prefixes, not just
`components/*` — any module applying `valkyrie.abi` fails `check` when its ABI changes.

## Developer Workflows

```bash
# Verify before committing (same set CI runs via `./gradlew build`)
./gradlew test               # JVM tests of every module
./gradlew spotlessCheck
./gradlew checkKotlinAbi     # ABI compatibility check; already part of `check`/`build`

# Fix formatting
./gradlew spotlessApply

# Update ABI snapshots after intentional API changes
./gradlew updateKotlinAbi

# KMP tests beyond JVM (wasmJs needs a local Chrome; usually not what you want to run)
./gradlew allTests

# IDEA plugin
./gradlew buildPlugin          # produces tools/idea-plugin/build/distributions/
./gradlew runIde               # launches sandbox IDE

# Web / WASM
./gradlew tools:compose-app:wasmJsBrowserDevelopmentRun

# Coverage
./gradlew :sdk:test:coverage:koverLog
./gradlew :sdk:test:coverage:koverHtmlReport
```

**Java 25+ is required** (`settings.gradle.kts` enforces this at configuration time). Use a full JDK, not JetBrains
JBR: JBR ships without `jlink`, so `tools:gradle-plugin` Android TestKit tests fail inside AGP's `JdkImageTransform`
when the Gradle daemon runs on JBR (happens when tests are launched from an IDE).

## Code Style

- **ktlint** via Spotless on all `src/**/*.kt`. Run `./gradlew spotlessApply` before pushing.
- Compose rules enforced: Material2 disallowed (`compose_disallow_material2 = true`), preview naming required (suffix
  strategy), lambda-param-event-trailing disabled.
- KotlinGradle files also linted.

## Convention Plugins (build-logic)

Opt-in building blocks applied via `alias(libs.plugins.valkyrie.*)`, not mutually exclusive:

- `valkyrie.jvm` — JVM-only library (Kotlin JVM, `stdlib` as `compileOnly`)
- `valkyrie.kmp` — Kotlin Multiplatform (JVM + wasmJs targets, `extraWarnings`, `-Xexpect-actual-classes`)
- `valkyrie.compose` — JetBrains Compose + Kotlin Compose compiler plugins, registers root `stability_config.conf`
- `valkyrie.wasm.resources` — serves Compose resources to the wasmJs dev server
- `valkyrie.abi` — binary-compatibility-validator: adds `checkKotlinAbi` to `check`
- `valkyrie.kover` — coverage

`tools/*` are end-user deliverables, so they declare Kotlin/AGP/application plugins directly and only borrow
`valkyrie.abi`, `valkyrie.kover` or `valkyrie.compose`.

## Key Files

- `sdk/ir/core/src/commonMain/.../IrImageVector.kt` — central domain model
- `components/parser/unified/src/commonMain/.../SvgXmlParser.kt` — unified parser entry point
- `components/generator/kmp/imagevector/src/commonMain/.../ImageVectorGenerator.kt` — KMP generator
- `sdk/shared/src/commonMain/.../ValkyrieMode.kt` — Simple vs IconPack mode
- `gradle/libs.versions.toml` — main version catalog; `gradle/cli.versions.toml`, `gradle/gradle.versions.toml`,
  `gradle/plugin.versions.toml` for tool-specific versions
- `tools/idea-plugin/CHANGELOG.md` — updated via `./gradlew tools:idea-plugin:patchChangelog`

## IDEA Plugin Specifics

- Targets IntelliJ IDEA 2026.2.3 (`sinceBuild = "262"`, `untilBuild` is unbounded).
- Bundled Kotlin/Coroutines/Compose are excluded from the plugin ZIP (classpath clash workaround).
- Platform tests disable the bundled Ultimate module (see the `doFirst` block in the root `build.gradle.kts`): IU
  2026.2.3 registers a core-classloader name that collides with a core interface.
- Signing credentials read from env vars: `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD`.
- Publish token: `PUBLISH_TOKEN`.

## Version Lock

The IDE plugin runs inside the JetBrains runtime, so library versions are ceilings set by the target platform, not
free choices (see the comments in `gradle/libs.versions.toml`):

- `intellijIdea("2026.2.3")` — floor comes from Jewel, which the Compose UI of the plugin is built on.
- `compose = "1.12.0"` — must match the Compose Multiplatform that Jewel of the target IDE was built against; read the
  [Jewel release notes](https://github.com/JetBrains/intellij-community/blob/master/platform/jewel/RELEASE%20NOTES.md)
  before bumping either side of this pair.
- `kotlin = "2.4.0"`, `coroutines = "1.10.2"` — stdlib/coroutines bundled in IJPL 2026.2.
- `xmlutil = "1.0.1"` — 1.0.2+ require kotlin-stdlib 2.4.10.
- `ktor = "3.4.3"` — 3.5.x+ require kotlinx-coroutines 1.11.0.

