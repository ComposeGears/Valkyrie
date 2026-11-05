---
name: valkyrie-svg-xml-to-imagevector
description: Convert SVG/XML icons into Compose ImageVector Kotlin code using the Valkyrie CLI. Use whenever the user wants to convert, generate, or migrate icons (svg, xml, vector assets) to Compose ImageVector / Kotlin icon code, mentions Valkyrie, or asks to set up icon packs for Compose — even if they don't name the tool.
---

# Valkyrie CLI — SVG/XML → Compose ImageVector

Valkyrie converts SVG and Android `vector` XML files into Compose `ImageVector` Kotlin sources.

## Prerequisites

The CLI is a JVM application: its `valkyrie` launcher needs Java on `PATH` or `JAVA_HOME` — the
release zip does not bundle a runtime. Check availability with `command -v valkyrie`.
If missing, install it — pick the first option that works:

1. **Homebrew** (macOS/Linux; installs OpenJDK automatically):

   ```bash
   brew install ComposeGears/repo/valkyrie
   ```

2. **Direct download from the Homebrew formula** (works without brew, e.g. Linux/CI, or when a
   local brew is stale and may serve an older version). The formula is the source of truth for
   the latest release: fetch it and extract the `url` and `sha256` fields:

   ```bash
   curl -fsSL https://raw.githubusercontent.com/ComposeGears/homebrew-repo/main/Formula/valkyrie.rb
   ```

   Then download the zip from the `url` (pattern:
   `https://github.com/ComposeGears/Valkyrie/releases/download/cli-<version>/valkyrie-cli-<version>.zip`),
   verify it with `shasum -a 256` against the formula's `sha256`, unzip, and use `bin/valkyrie`
   from the unpacked directory. Installing to `~/.local/share/valkyrie` makes it reusable across
   sessions — but invoke it by full path or put that `bin` directory **first** on `PATH`,
   otherwise an older `valkyrie` earlier on `PATH` keeps running.

Notes:
- `valkyrie --version` prints JNA `WARNING:` lines on stderr — they are harmless, ignore them.
- Do NOT hand-edit generated `.kt` files. If output is wrong, adjust flags and re-run.
- **Version matters.** An outdated CLI (e.g. an old brew install) silently overwrites icons whose
  names collide, and lacks newer flags. Compare `valkyrie --version` with the version in the
  [formula](https://raw.githubusercontent.com/ComposeGears/homebrew-repo/main/Formula/valkyrie.rb).
  If it is older, update it: for a brew install run `brew update && brew upgrade ComposeGears/repo/valkyrie`,
  otherwise re-download via option 2. If an option is rejected, run `valkyrie <command> --help`
  and use the flags it lists.

## Command 1: convert icons

```bash
valkyrie svgxml2imagevector \
  --input-path <dir-with-svg-xml-or-single-file> \
  --output-path <output-dir> \
  --package-name <com.example.icons>
# append additional flags as needed, e.g. --iconpack-name MyIcons --nested-pack-name Filled
```

| Flag | Default | When to use |
|---|---|---|
| `--iconpack-name <Name>` | `""` | IconPack mode: name of the pack object icons belong to |
| `--nested-pack-name <Name>` | `""` | IconPack mode with hierarchy: nested section (e.g. `Filled`), output goes to `<output-path>/<name-lowercase>/` |
| `--flatpackage` | off | Put all icons into `<output-path>` directly, no nested folder |
| `--output-format` | `backing-property` | `lazy-property` for lazy `ImageVector` getter style |
| `--use-compose-colors` | `true` | Emit `Color.Black` instead of `Color(0xFF000000)` |
| `--generate-preview` | off | Add `@Preview` functions |
| `--explicit-mode` | off | Add explicit `public` modifier |
| `--trailing-comma` | off | Trailing commas in builder block and params |
| `--use-path-data-string` | off | `addPath` with pathData strings (worse runtime perf — avoid unless asked) |
| `--suppress-unused-receiver` | off | `@Suppress("UnusedReceiverParameter")` on extension properties |
| `--indent-size <n>` | `4` | Indentation width |
| `--auto-mirror <bool>` | from source | Force `autoMirror=true/false` |
| `-v` / `--verbose` | off | Per-file progress logs |

### Decision: Simple vs IconPack mode

- **Simple** (default): no `--iconpack-name`/`--nested-pack-name`. Each icon becomes a standalone top-level property. Use when the user just wants icons converted.
- **IconPack**: set `--iconpack-name` (and `--nested-pack-name` for `Pack.Filled/Outlined/...` hierarchy). Use when the user has an existing icon pack or asks for one; afterward generate the pack object with command 2.

### Rules

- **Extension must match the format**: SVG content in `.svg` files, Android `<vector>` XML in `.xml` files. A mismatch (e.g. vector XML named `.svg`) makes the CLI crash with a raw stack trace like `IllegalStateException: No <svg> tags found` — rename the files instead of debugging the stack trace.
- **Directory input is NOT recursive** — the CLI reads `*.svg`/`*.xml` from the top level only. If the user points at a nested tree, collect the files into a single flat directory first and convert in one run. Duplicate names are checked per run only, so converting several directories into the same output path can silently overwrite earlier files — if a single run is impossible, use a separate `--output-path` for each directory.
- Icon names are derived from file names (formatted to PascalCase). Duplicate names or macOS case-insensitive collisions make the CLI fail — resolve by renaming source files, never by editing output.
- If icons land next to Compose reserved names (`Color`, `Brush`, `Offset`), the CLI automatically switches those to fully-qualified imports; just mention it to the user.

## Command 2: generate the IconPack object

```bash
valkyrie iconpack \
  --output-path <output-dir> \
  --package-name <com.example.icons> \
  --iconpack 'MyIcons.Filled,MyIcons.Outlined'
# simple pack: pass --iconpack 'MyIcons' instead
# optional flags: --indent-size 4, --use-explicit-mode
```

Run this after converting icons in IconPack mode so imports resolve against a real pack object.
The command overwrites the existing `<Name>.kt` pack file with only the packs passed to `--iconpack` —
include every nested pack the user needs, and never keep hand-written declarations in that file.

## Post-generation

1. List the written files and show the user what was generated (paths + icon count).
2. Show one generated file's head (package, imports, first property) so the user can sanity-check style.

Generated code follows Valkyrie's own ktlint rules, but indentation comes from `--indent-size` —
it can fail the host project's ktlint `indent` rule if that value differs from the project's
`.editorconfig`. Do not run the host project's build or formatter unless the user asks —
compilation may be slow, and the project's formatter could override intentional flags like
`--indent-size` or `--trailing-comma`.
