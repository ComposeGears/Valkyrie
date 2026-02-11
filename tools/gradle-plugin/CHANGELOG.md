# Gradle plugin changelog

## Unreleased

### Added

- Add `usePathDataString` configuration option in `imageVector` block to generate addPath with pathData strings instead of path builder calls

## [0.3.0] - 2025-12-11

### Added

- Automatically handle full qualified imports for icons that conflict with reserved Compose qualified names (`Brush`,
  `Color`, `Offset`)
- Add `autoMirror` configuration support at root, icon pack, and nested pack levels to control RTL (right-to-left)
  layout behavior for generated ImageVectors

The `autoMirror` parameter controls whether icons should automatically flip horizontally when used in RTL layouts.
This is particularly useful for directional icons like arrows, chevrons, or navigation elements.

Configuration can be set at three levels with override hierarchy:

1. **Root level** - applies to all icons across the project
2. **Icon pack level** - overrides root level for all icons in the pack
3. **Nested pack level** - overrides both icon pack and root level for icons in the nested pack

Example configuration:

```kotlin
valkyrie {
  packageName = "com.example.app.icons"

  // Force all icons to support RTL by default
  autoMirror = true

  iconPack {
    name = "ValkyrieIcons"
    targetSourceSet = "commonMain"

    // Override: icons in this pack won't auto-mirror
    autoMirror = false

    nested {
      name = "Navigation"
      sourceFolder = "navigation"
      // Override: navigation icons should auto-mirror for RTL
      autoMirror = true
    }

    nested {
      name = "Logos"
      sourceFolder = "logos"
      // Logos inherit autoMirror = false from icon pack level
    }
  }
}
```

When `autoMirror` is not specified at any level, the original value from the source icon file will be preserved.

### Changed

- Generated files are now placed in a `kotlin` subdirectory within each source set output directory (e.g.,
  `build/generated/valkyrie/{sourceSetName}/kotlin` instead of `build/generated/valkyrie/{sourceSetName}`)

## [0.2.0] - 2025-12-08

### Added

- Introduce separate code style configuration for generated code

Breaking changes:

1. `useExplicitMode` and `indentSize` moved from `imageVector` into `codeStyle` block because they will be reused for
   icon pack generation as well

```kotlin
valkyrie {
// Optional: Code style configuration for generated code
  codeStyle {
    // Add explicit `public` modifier to generated declarations (default: false)
    useExplicitMode = false

    // Number of spaces used for each level of indentation in generated code (default: 4)
    indentSize = 4
  }
}
```

- Introduce iconpack generation and configuration

Breaking changes:

1. `iconPackName` and `nestedPackName` options removed and replaced them with a dedicated `iconPack`
   and `nested` blocks
2. `useFlatPackage` moved into `iconPack` block

For more details,
see [documentation](https://github.com/ComposeGears/Valkyrie/tree/gradle-plugin-0.2.0?tab=readme-ov-file#gradle-plugin).

For simple icon pack generation, you can use the following configuration:

```kotlin
valkyrie {
  packageName = "com.example.app.icons"

  iconPack {
    name = "ValkyrieIcons"
    targetSourceSet = "commonMain" // icon pack object will be generated in commonMain source set
  }
}
```

For nested icon packs, you can define multiple `nested` icon packs within the `iconPack` block:

```kotlin
valkyrie {
  packageName = "com.example.app.icons"

  iconPack {
    name = "ValkyrieIcons"
    targetSourceSet = "commonMain" // icon pack object will be generated in commonMain source set

    nested {
      name = "Outlined"
      sourceFolder = "outlined"
    }

    nested {
      name = "Filled"
      sourceFolder = "filled"
    }
  }
}
```

## [0.1.0] - 2025-11-30

- Initial release of Valkyrie Gradle plugin
