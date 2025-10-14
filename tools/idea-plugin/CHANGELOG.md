# Plugin Changelog

## [Unreleased]

### Added

- Setup of changelog plugin
- Dynamically set the gutter icon's background to maintain contrast when its dominant color matches the IDE theme color

## [0.17.2](https://github.com/ComposeGears/Valkyrie/releases/tag/0.17.2) - 2025-10-11

### Fixed

- Handle aspect ratio for ImageVectorIcon rendering in gutter and auto-complete popup
- Fix PSI parsing for material icon without materialIcon block, introduce BuilderExpression

### Changed

- Move :extensions into :sdk:core:extensions
- Simplify navigation transitions across screens

## [0.17.1](https://github.com/ComposeGears/Valkyrie/releases/tag/0.17.1) - 2025-10-09

### Fixed

- Fix PSI parsing when nullable KtProperty on top of file

## [0.17.0](https://github.com/ComposeGears/Valkyrie/releases/tag/0.17.0) - 2025-10-09

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

## [0.16.0](https://github.com/ComposeGears/Valkyrie/releases/tag/0.16.0) - 2025-07-24

### Changed

- No notable changes

## [0.15.0](https://github.com/ComposeGears/Valkyrie/releases/tag/0.15.0) - 2025-06-28

### Added

- Show export issues, add auto fix functionality
- Allow numbers in icon pack names
- Parse gradient startColor/endColor

### Fixed

- "useComposeColors" option not used in plugin
- Fix ImageVector preview with linear gradient
- "Auto resolve issues" cleared all processing icons
- Export issues should consider nested pack duplicates

## [0.14.0](https://github.com/ComposeGears/Valkyrie/releases/tag/0.14.0) - 2025-04-28

### Added

- [Export] Replace color hex with predefined Compose colors
- [Preview] Handle predefined Compose colors during parsing

### Fixed

- [IconPack] Preview action crash if icon pasted from clipboard
- Fix plugin not dynamic due to missing id

## [0.13.0](https://github.com/ComposeGears/Valkyrie/releases/tag/0.13.0) - 2025-03-08

### Added

- Introduce separate Preview annotation for AndroidX and Jetbrains package
