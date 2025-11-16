# CLI Changelog

## [Unreleased]

- Separate CLI versioning from plugin versioning
- Add `changelog` command to print CLI changelog

## [0.17.3] - 2025-10-14

- Fixed executable permissions for the 'valkyrie' binary in CLI distribution

## [0.16.0] - 2025-07-24

- Use fully qualified imports if icon name conflicts with Compose qualifiers

## [0.15.0] - 2025-06-28

- Added `--auto-mirror` option to forcibly add "autoMirror=true" or "autoMirror=false" option to generated ImageVector

## [0.14.0] - 2025-04-28

- Migrate to hierarchical icon pack structure. CLI options `--iconpack-name` and `--nested-packs` removed in favour of
  `--iconpack`

| Old syntax example                                                       | New syntax example                                                 |
|--------------------------------------------------------------------------|--------------------------------------------------------------------|
| `./valkyrie --iconpack-name=ValkyrieIcons`                               | `./valkyrie --iconpack=ValkyrieIcons`                              |
| `./valkyrie --iconpack-name=ValkyrieIcons --nested-packs=Colored,Filled` | `./valkyrie --iconpack=ValkyrieIcons.Colored,ValkyrieIcons.Filled` |

- Added `--use-compose-colors` option to use predefined Compose colors instead of hex color codes (e.g. Color.Black
  instead of Color(0xFF000000))

## [0.13.0] - 2025-03-08

- Added `--preview-annotation-type` option to specify the type of Preview annotation (must be either 'androidx' or '
  jetbrains')

## [0.11.0] - 2024-11-25

- Initial release of Valkyrie CLI tool
