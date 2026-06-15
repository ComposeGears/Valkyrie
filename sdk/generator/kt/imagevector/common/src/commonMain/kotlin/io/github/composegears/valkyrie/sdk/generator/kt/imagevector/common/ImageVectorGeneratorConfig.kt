package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common

import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.IconPackTree

/**
 * Top-level configuration for the ImageVector generator.
 *
 * @property iconName Simple name of the icon (e.g. `"Add"`).
 * @property packageName Kotlin package for the generated file (e.g. `"com.example.icons"`).
 * @property iconPackPackage Package of the icon-pack object that will own the icon.
 * @property iconPackTree Optional hierarchy description used when generating icon-pack members.
 * @property codeStyle Formatting and Kotlin code-style options. Defaults to [CodeStyleConfig].
 * @property imageVector ImageVector content and output options. Defaults to [ImageVectorConfig].
 * @property fullyQualifiedImports Controls which Compose types are referenced by fully-qualified
 *   name instead of an import. Defaults to [FullyQualifiedImports].
 */
data class ImageVectorGeneratorConfig(
    val iconName: String,
    val packageName: String,
    val iconPackTree: IconPackTree? = null,
    val iconPackPackage: String = packageName,
    val codeStyle: CodeStyleConfig = CodeStyleConfig(),
    val imageVector: ImageVectorConfig = ImageVectorConfig(),
    val fullyQualifiedImports: FullyQualifiedImports = FullyQualifiedImports(),
) {
    companion object {
        /**
         * Creates a config for simple (no icon-pack) conversion.
         *
         * [iconPackPackage] is set to [packageName] and [iconPackTree] is left `null`.
         */
        fun simple(
            iconName: String,
            packageName: String = "",
            codeStyle: CodeStyleConfig = CodeStyleConfig(),
            imageVector: ImageVectorConfig = ImageVectorConfig(),
            fullyQualifiedImports: FullyQualifiedImports = FullyQualifiedImports(),
        ): ImageVectorGeneratorConfig = ImageVectorGeneratorConfig(
            iconName = iconName,
            packageName = packageName,
            iconPackPackage = packageName,
            codeStyle = codeStyle,
            imageVector = imageVector,
            fullyQualifiedImports = fullyQualifiedImports,
        )

        /**
         * Creates a config for icon-pack conversion.
         *
         * Uses the provided [iconPackTree] to describe icon-pack hierarchy.
         */
        fun iconPack(
            iconName: String,
            packageName: String,
            iconPackPackage: String = packageName,
            iconPackTree: IconPackTree,
            codeStyle: CodeStyleConfig = CodeStyleConfig(),
            imageVector: ImageVectorConfig = ImageVectorConfig(),
            fullyQualifiedImports: FullyQualifiedImports = FullyQualifiedImports(),
        ): ImageVectorGeneratorConfig = ImageVectorGeneratorConfig(
            iconName = iconName,
            packageName = packageName,
            iconPackPackage = iconPackPackage,
            iconPackTree = iconPackTree,
            codeStyle = codeStyle,
            imageVector = imageVector,
            fullyQualifiedImports = fullyQualifiedImports,
        )
    }
}

/**
 * Kotlin code-style options that affect how the generated source is formatted.
 *
 * @property useExplicitMode When `true`, explicit types are emitted where Kotlin would normally
 *   allow them to be inferred.
 * @property indentSize Number of spaces used for each indentation level. Must be greater than `0`.
 *   Defaults to `4`.
 */
data class CodeStyleConfig(
    val useExplicitMode: Boolean = false,
    val indentSize: Int = 4,
) {
    init {
        require(indentSize > 0) { "indentSize must be greater than 0" }
    }
}

/**
 * Options that control the shape and content of the generated `ImageVector`.
 *
 * @property outputFormat Determines the property pattern used for the generated vector
 *   (`BackingProperty` or `LazyProperty`).
 * @property useComposeColors When `true`, well-known colors are referenced via
 *   `androidx.compose.ui.graphics.Color` constants instead of raw ARGB literals.
 * @property generatePreview When `true`, a `@Preview` composable is appended to the file.
 * @property useFlatPackage When `true`, the icon is placed directly in [ImageVectorGeneratorConfig.packageName]
 *   without any sub-package derived from the icon-pack structure.
 * @property addTrailingComma When `true`, a trailing comma is added after the last argument in
 *   multi-line builder calls.
 * @property usePathDataString When `true`, path data is emitted as a raw string literal passed to
 *   `PathData` instead of individual path-builder DSL calls.
 * @property suppressUnusedReceiverWarning When `true`, a `@Suppress("UnusedReceiverParameter")`
 *   annotation is added to the icon property getter.
 */
data class ImageVectorConfig(
    val outputFormat: OutputFormat = OutputFormat.BackingProperty,
    val useComposeColors: Boolean = true,
    val generatePreview: Boolean = false,
    val useFlatPackage: Boolean = false,
    val addTrailingComma: Boolean = false,
    val usePathDataString: Boolean = false,
    val suppressUnusedReceiverWarning: Boolean = false,
)

/**
 * Selects which Compose types must be referenced by their fully-qualified name rather than via a
 * regular import. This is useful when the generated file is placed in a package that already
 * contains a conflicting declaration.
 *
 * @property brush When `true`, `androidx.compose.ui.graphics.Brush` is fully qualified.
 * @property color When `true`, `androidx.compose.ui.graphics.Color` is fully qualified.
 * @property offset When `true`, `androidx.compose.ui.geometry.Offset` is fully qualified.
 */
data class FullyQualifiedImports(
    val brush: Boolean = false,
    val color: Boolean = false,
    val offset: Boolean = false,
) {
    companion object {
        /**
         * Simple Compose type names that can be requested as fully-qualified references when
         * building a [FullyQualifiedImports] instance from external configuration.
         */
        val reservedComposeTypeNames = setOf("Brush", "Color", "Offset")
    }
}

/**
 * Describes the supported property patterns for an emitted `ImageVector`.
 *
 * @property key Stable external identifier used by CLI and Gradle configuration.
 */
enum class OutputFormat(val key: String) {
    /** Classic pattern using a private backing field and a public property. */
    BackingProperty(key = "backing_property"),

    /** Pattern using `by lazy { … }` for deferred initialisation. */
    LazyProperty(key = "lazy_property"),
    ;

    companion object {
        /**
         * Resolves an [OutputFormat] from its external [key].
         *
         * Throws [IllegalStateException] when [key] does not match any known format.
         */
        fun from(key: String): OutputFormat = fromOrNull(key)
            ?: error("Unsupported outputFormat '$key'. Supported values: ${entries.joinToString { "'${it.key}'" }}.")

        /**
         * Resolves an [OutputFormat] from its external [key], or returns `null` when [key] is not
         * supported.
         */
        fun fromOrNull(key: String?): OutputFormat? = entries.find { it.key == key }
    }
}

/**
 * Result produced by the ImageVector generator.
 *
 * @property content Generated Kotlin source code as a plain string.
 * @property name Simple name of the generated icon (matches [ImageVectorGeneratorConfig.iconName]).
 */
data class ImageVectorOutput(
    val content: String,
    val name: String,
)
