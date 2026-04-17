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
  `valkyrie.kover`) applied via `alias(libs.plugins.valkyrie.*)`.

## Module Taxonomy

| Prefix         | Purpose                                                             |
|----------------|---------------------------------------------------------------------|
| `sdk/*`        | Reusable building blocks (IR, Compose UI, IntelliJ PSI, utils)      |
| `components/*` | Core pipeline: parsers & generators; have public ABI tracked        |
| `tools/*`      | End-user deliverables: idea-plugin, cli, gradle-plugin, compose-app |

## Developer Workflows

```bash
# Verify before committing
./gradlew test
./gradlew spotlessCheck
./gradlew checkLegacyAbi      # ABI compatibility check for components/*

# Fix formatting
./gradlew spotlessApply

# Update ABI snapshots after intentional API changes
./gradlew updateLegacyAbi

# IDEA plugin
./gradlew buildPlugin          # produces tools/idea-plugin/build/distributions/
./gradlew runIde               # launches sandbox IDE

# Web / WASM
./gradlew tools:compose-app:wasmJsBrowserDevelopmentRun

# Coverage
./gradlew components:test:coverage:koverLog
./gradlew components:test:coverage:koverHtmlReport
```

**Java 21+ is required** (`settings.gradle.kts` enforces this at configuration time).

## Code Style

- **ktlint** via Spotless on all `src/**/*.kt`. Run `./gradlew spotlessApply` before pushing.
- Compose rules enforced: Material2 disallowed (`compose_disallow_material2 = true`), preview naming required (suffix
  strategy), lambda-param-event-trailing disabled.
- KotlinGradle files also linted.

## Convention Plugins (build-logic)

Every module picks exactly one of:

- `alias(libs.plugins.valkyrie.jvm)` — JVM-only library
- `alias(libs.plugins.valkyrie.kmp)` — Kotlin Multiplatform (JVM + wasmJs targets)
- `alias(libs.plugins.valkyrie.compose)` — adds Compose compiler

Plus optional: `valkyrie.abi` (ABI tracking), `valkyrie.kover` (coverage).

## Key Files

- `sdk/ir/core/src/commonMain/.../IrImageVector.kt` — central domain model
- `components/parser/unified/src/commonMain/.../SvgXmlParser.kt` — unified parser entry point
- `components/generator/kmp/imagevector/src/commonMain/.../ImageVectorGenerator.kt` — KMP generator
- `sdk/shared/src/commonMain/.../ValkyrieMode.kt` — Simple vs IconPack mode
- `gradle/libs.versions.toml` — main version catalog; `gradle/cli.versions.toml`, `gradle/gradle.versions.toml`,
  `gradle/plugin.versions.toml` for tool-specific versions
- `tools/idea-plugin/CHANGELOG.md` — updated via `./gradlew tools:idea-plugin:patchChangelog`

## IDEA Plugin Specifics

- Targets IntelliJ IDEA 2025.3.3 (`sinceBuild = "253.31033.145"`, `untilBuild` is unbounded).
- Bundled Kotlin/Coroutines/Compose are excluded from the plugin ZIP (classpath clash workaround).
- Signing credentials read from env vars: `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD`.
- Publish token: `PUBLISH_TOKEN`.

