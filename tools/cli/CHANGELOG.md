# CLI Changelog

## Unreleased

## 1.1.1 - 2026-02-26

### Fixed

- Fix `changelog` output command due to new format

### Changed

- Remove "Unreleased" section from changelog output

## 1.1.0 - 2026-02-26

### Added

- Add `--use-path-data-string` option to generate addPath with pathData strings instead of path builder calls
- Add validation for exact duplicate icon names (e.g., `test-icon.svg` and `test_icon.svg` both produce `TestIcon.kt`)
- Add validation for case-insensitive duplicate icon names to prevent file overwrites on macOS/Windows

### Removed

- Remove support for `androidx.compose.desktop.ui.tooling.preview.Preview` and corresponding `--preview-annotation-type`
  command as `androidx.compose.ui.tooling.preview.Preview`
  became [multiplatform](https://kotlinlang.org/docs/multiplatform/whats-new-compose-110.html#unified-preview-annotation)
  in Compose 1.10.0

### Fixed

- Fix parsing of Android system colors (e.g., `@android:color/white`) in XML parser

## 1.0.1 - 2025-11-20

- Limited changelog output to the last 5 releases
- Added `--show-all` option to display full changelog

## 1.0.0 - 2025-11-17

- Stabilize CLI tool
- Separate CLI versioning from IDEA Plugin project
- Add `changelog` command to display CLI changelog

## 0.17.3 - 2025-10-14

- Fixed executable permissions for the 'valkyrie' binary in CLI distribution

## 0.16.0 - 2025-07-24

- Use fully qualified imports if icon name conflicts with Compose qualifiers

## 0.15.0 - 2025-06-28

- Added `--auto-mirror` option to forcibly add "autoMirror=true" or "autoMirror=false" option to generated ImageVector

## 0.14.0 - 2025-04-28

- Migrate to hierarchical icon pack structure. CLI options `--iconpack-name` and `--nested-packs` removed in favour of
  `--iconpack`

| Old syntax example                                                       | New syntax example                                                 |
|--------------------------------------------------------------------------|--------------------------------------------------------------------|
| `./valkyrie --iconpack-name=ValkyrieIcons`                               | `./valkyrie --iconpack=ValkyrieIcons`                              |
| `./valkyrie --iconpack-name=ValkyrieIcons --nested-packs=Colored,Filled` | `./valkyrie --iconpack=ValkyrieIcons.Colored,ValkyrieIcons.Filled` |

- Added `--use-compose-colors` option to use predefined Compose colors instead of hex color codes (e.g. Color.Black
  instead of Color(0xFF000000))

## 0.13.0 - 2025-03-08

- Added `--preview-annotation-type` option to specify the type of Preview annotation (must be either 'androidx' or '
  jetbrains')

## 0.11.0 - 2024-11-25

- Initial release of Valkyrie CLI tool
