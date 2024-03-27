/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.material.icons.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName

/**
 * Package names used for icon generation.
 */
enum class PackageNames(val packageName: String) {
    MaterialIconsPackage("androidx.compose.material.icons"),
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
object ClassNames {
    val Icons = PackageNames.MaterialIconsPackage.className("Icons")
    val ImageVector = PackageNames.VectorPackage.className("ImageVector")
    val PathFillType = PackageNames.GraphicsPackage.className("PathFillType", CompanionImportName)
    val StrokeCap = PackageNames.GraphicsPackage.className("StrokeCap", CompanionImportName)
    val StrokeJoin = PackageNames.GraphicsPackage.className("StrokeJoin", CompanionImportName)
    val Brush = PackageNames.GraphicsPackage.className("Brush", CompanionImportName)
    val Preview = PackageNames.PreviewPackage.className("Preview")
    val Composable = PackageNames.RuntimePackage.className("Composable")
}

/**
 * [MemberName]s used for icon generation.
 */
object MemberNames {
    val ImageVectorBuilder = MemberName(ClassNames.ImageVector, "Builder")

    val Path = MemberName(PackageNames.VectorPackage.packageName, "path")

    val EvenOdd = MemberName(ClassNames.PathFillType, "EvenOdd")
    val NonZero = MemberName(ClassNames.PathFillType, "NonZero")

    val Group = MemberName(PackageNames.VectorPackage.packageName, "group")

    val StrokeCapButt = MemberName(ClassNames.StrokeCap, "Butt")
    val StrokeCapRound = MemberName(ClassNames.StrokeCap, "Round")
    val StrokeCapSquare = MemberName(ClassNames.StrokeCap, "Square")

    val StrokeJoinMiter = MemberName(ClassNames.StrokeJoin, "Miter")
    val StrokeJoinRound = MemberName(ClassNames.StrokeJoin, "Round")
    val StrokeJoinBevel = MemberName(ClassNames.StrokeJoin, "Bevel")

    val Dp = MemberName(PackageNames.Unit.packageName, "dp")

    val Color = MemberName(PackageNames.GraphicsPackage.packageName, "Color")
    val SolidColor = MemberName(PackageNames.GraphicsPackage.packageName, "SolidColor")

    val LinearGradient = MemberName(ClassNames.Brush, "linearGradient")
    val RadialGradient = MemberName(ClassNames.Brush, "radialGradient")

    val Offset = MemberName(PackageNames.GeometryPackage.packageName, "Offset")
}

/**
 * @return the [ClassName] of the given [classNames] inside this package.
 */
fun PackageNames.className(vararg classNames: String) = ClassName(this.packageName, *classNames)

private const val CompanionImportName = "Companion"
