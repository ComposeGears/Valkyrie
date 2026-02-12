package io.github.composegears.valkyrie.generator.jvm.imagevector.util

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName

/**
 * Package names used for icon generation.
 */
internal enum class PackageNames(val packageName: String) {
    UiPackage("androidx.compose.ui"),
    GraphicsPackage(UiPackage.packageName + ".graphics"),
    VectorPackage(GraphicsPackage.packageName + ".vector"),
    GeometryPackage(UiPackage.packageName + ".geometry"),
    Unit(UiPackage.packageName + ".unit"),
    FoundationPackage("androidx.compose.foundation"),
    LayoutPackage(FoundationPackage.packageName + ".layout"),
    PreviewPackage(UiPackage.packageName + ".tooling.preview"),
    RuntimePackage("androidx.compose.runtime"),
}

/**
 * [ClassName]s used for icon generation.
 */
internal object ClassNames {
    val ImageVector = PackageNames.VectorPackage.className("ImageVector")
    val PathFillType = PackageNames.GraphicsPackage.className("PathFillType")
    val StrokeCap = PackageNames.GraphicsPackage.className("StrokeCap")
    val StrokeJoin = PackageNames.GraphicsPackage.className("StrokeJoin")
    val Brush = PackageNames.GraphicsPackage.className("Brush")
    val AndroidXPreview = PackageNames.PreviewPackage.className("Preview")
    val Composable = PackageNames.RuntimePackage.className("Composable")
    val LazyThreadSafetyMode = ClassName.bestGuess("kotlin.LazyThreadSafetyMode")
    val Suppress = ClassName.bestGuess("kotlin.Suppress")
}

/**
 * [MemberName]s used for icon generation.
 */
internal object MemberNames {
    val Path = MemberName(PackageNames.VectorPackage.packageName, "path")
    val AddPathNodes = MemberName(PackageNames.VectorPackage.packageName, "addPathNodes")

    val Group = MemberName(PackageNames.VectorPackage.packageName, "group")
    val PathData = MemberName(PackageNames.VectorPackage.packageName, "PathData")

    val Dp = MemberName(PackageNames.Unit.packageName, "dp")
    val Modifier = MemberName(PackageNames.UiPackage.packageName, "Modifier")
    val Padding = MemberName(PackageNames.LayoutPackage.packageName, "padding")
    val Box = MemberName(PackageNames.LayoutPackage.packageName, "Box")
    val Image = MemberName(PackageNames.FoundationPackage.packageName, "Image")

    val Color = MemberName(PackageNames.GraphicsPackage.packageName, "Color")
    val SolidColor = MemberName(PackageNames.GraphicsPackage.packageName, "SolidColor")

    val Offset = MemberName(PackageNames.GeometryPackage.packageName, "Offset")
}

/**
 * @return the [ClassName] of the given [classNames] inside this package.
 */
internal fun PackageNames.className(vararg classNames: String) = ClassName(this.packageName, *classNames)
