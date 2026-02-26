# Plugin Changelog

## Unreleased

### Added

- Add export as file action for `Simple mode` and `ImageVector to XML` tool
- [IconPack] Add validation for exact duplicate icon names (e.g., `test-icon.svg` and `test_icon.svg` both produce
  `TestIcon.kt`)
- [IconPack] Add validation for case-insensitive duplicate icon names to prevent file overwrites on macOS/Windows
- [IconPack] Add automatic re-validation when `useFlatPackage` setting changes to detect new conflicts immediately
- [Web Import] Add `Bootstrap` icons provider
- [Web Import] Add persistent settings for font customization for all providers
- [Gutter] Add support for multiple icons in kt file
- [Web Import] Add `Remix` icons provider
- [Web Import] Add `Box` icons provider

### Fixed

- Fix an incorrect error message when the user cancels the save dialog during export

## 1.2.0 - 2026-02-20

### Added

- Add new tool to convert SVG into XML
- Add separate "Material pack" option

### Changed

- Rollback feature to use `Material` icon pack
- Update toolbar icons for code and icon preview

### Fixed

- Fix icon renaming in batch processing - changes now properly saved when focus is lost from TextField

## 1.1.0 - 2026-02-13

### Added

- Add option to generate ImageVector paths using pathData strings
- Support pathData strings in addPath/clipPathData in Preview

### Removed

- Remove support for `Material` icon pack from `androidx.compose.material.icons` package
- Remove support for `androidx.compose.desktop.ui.tooling.preview.Preview` annotation as
  `androidx.compose.ui.tooling.preview.Preview`
  became [multiplatform](https://kotlinlang.org/docs/multiplatform/whats-new-compose-110.html#unified-preview-annotation)
  in Compose 1.10.0

### Fixed

- Fix parsing of Android system colors (e.g., `@android:color/white`) in XML parser
- [PSI] Fix parsing `materialIcon` block with regular `path` calls
- [PSI] Fix ArithmeticException in ImageVectorIcon

## 1.0.0 - 2026-02-10

### Added

- Add a new mode for backward conversion from ImageVector to XML
- Implement fuzzi-search for web import feature
- Allow to disable icons preview in ProjectView
- Add Settings action on picker screens
- Add error handling for Simple mode and XML to ImageVector conversion
- Add `Lucide` icon provider into Web import feature

### Fixed

- Fix main screen description text being cut off or truncated
- Icons with gradient not drawing in gutter and autocomplete dialog
- Fix parsing material icon with multiple material paths
- Fix description text truncation on main screen

### Changed

- Min supported IntelliJ IDEA version 2025.3
- Full plugin redesign and migration to Jewel
- Reduce the plugin size by 10 times (from 128mb to 11mb)
- Rename "Export" to "Import" across IDEA plugin
- Update plugin icon

## 0.18.0 - 2025-11-06

### Added

- Add ability to import Material Symbols directly inside plugin and convert it into ImageVector

### Fixed

- Incorrect screen transition animation in certain cases
- Plugin not saved last opened screen after hide/unhide, always opens on launch screen

### Changed

- Simple mode will not generate `package` name anymore
- Enhance contrast of Snackbar colors for better visibility

## 0.17.4 - 2025-10-22

### Fixed

- Rollback Compose Multiplatform to 1.8.2 due to incompatibility with IntelliJ IDEA

## 0.17.3 - 2025-10-14

### Added

- Setup of changelog plugin
- Dynamically set the gutter icon's background to maintain contrast when its dominant color matches the IDE theme color

## 0.17.2 - 2025-10-11

### Fixed

- Handle aspect ratio for ImageVectorIcon rendering in gutter and auto-complete popup
- Fix PSI parsing for material icon without materialIcon block, introduce BuilderExpression

### Changed

- Move :extensions into :sdk:core:extensions
- Simplify navigation transitions across screens

## 0.17.1 - 2025-10-09

### Fixed

- Fix PSI parsing when nullable KtProperty on top of file

## 0.17.0 - 2025-10-09

### Added

- Image Vector Preview in Autocomplete Dialog
- Image Vector Preview in Gutter
- Add caching for icon creation in ImageVectorCompletionContributor
- Introduce ImageVectorIcon for HQ preview in CompletionContributor
- ImageVector to XML generator
- Add Clip Path Support (KMP)
- Add SDK core XML module to share VectorDrawable class

### Changed

- Gutter icon improvements
- Optimize ImageVectorIcon rendering by removing scale dependency and introducing a constant for icon size
- Refactor path utilities: move toPathString and toPathArgs to new PathNode

## 0.16.0 - 2025-07-24

### Changed

- No notable changes

## 0.15.0 - 2025-06-28

### Added

- Show export issues, add auto fix functionality
- Allow numbers in icon pack names
- Parse gradient startColor/endColor

### Fixed

- "useComposeColors" option not used in plugin
- Fix ImageVector preview with linear gradient
- "Auto resolve issues" cleared all processing icons
- Export issues should consider nested pack duplicates

## 0.14.0 - 2025-04-28

### Added

- [Export] Replace color hex with predefined Compose colors
- [Preview] Handle predefined Compose colors during parsing

### Fixed

- [IconPack] Preview action crash if icon pasted from clipboard
- Fix plugin not dynamic due to missing id

## 0.13.0 - 2025-03-08

### Added

- Introduce separate Preview annotation for AndroidX and Jetbrains package
