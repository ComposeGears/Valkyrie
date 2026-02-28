# Gradle plugin changelog

## Unreleased

## 0.4.0 - 2026-02-26

### Added

- Add `usePathDataString` configuration option in `imageVector` block to generate addPath with pathData strings instead
  of path builder calls
- Add validation for exact duplicate icon names (e.g., `test-icon.svg` and `test_icon.svg` both produce `TestIcon.kt`)
- Add validation for case-insensitive duplicate icon names to prevent file overwrites on macOS/Windows
- Add nested pack aware validation that correctly handles `useFlatPackage` mode - when enabled, duplicates are detected
  across all nested packs since they write to the same output folder

### Changed

- Reduce gradle plugin size from 46mb to 10mb

### Removed

- Remove support for `androidx.compose.desktop.ui.tooling.preview.Preview` and corresponding `previewAnnotationType`
  configuration property as `androidx.compose.ui.tooling.preview.Preview`
  became [multiplatform](https://kotlinlang.org/docs/multiplatform/whats-new-compose-110.html#unified-preview-annotation)
  in Compose 1.10.0

### Fixed

- Fix parsing of Android system colors (e.g., `@android:color/white`) in XML parser

## 0.3.0 - 2025-12-11

### Added

- Automatically handle full qualified imports for icons that conflict with reserved Compose qualified names (`Brush`,
  `Color`, `Offset`)
- Add `autoMirror` configuration support at root, icon pack, and nested pack levels to control RTL (right-to-left)
  layout behavior for generated ImageVectors

### Changed

- Generated files are now placed in a `kotlin` subdirectory within each source set output directory (e.g.,
  `build/generated/valkyrie/{sourceSetName}/kotlin` instead of `build/generated/valkyrie/{sourceSetName}`)

## 0.2.0 - 2025-12-08

### Added

- Introduce separate code style configuration for generated code

## 0.1.0 - 2025-11-30

- Initial release of Valkyrie Gradle plugin
