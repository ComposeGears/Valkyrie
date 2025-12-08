# Gradle plugin changelog

## Unreleased

### Added

- Automatically handle full qualified imports for icons that conflict with reserved Compose qualified names (`Brush`,
  `Color`, `Offset`)

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
