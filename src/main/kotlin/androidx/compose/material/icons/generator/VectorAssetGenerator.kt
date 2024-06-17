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

import androidx.compose.material.icons.generator.vector.Fill
import androidx.compose.material.icons.generator.vector.VectorNode
import com.squareup.kotlinpoet.CodeBlock

/**
 * Recursively adds function calls to construct the given [vectorNode] and its children.
 */
fun CodeBlock.Builder.addRecursively(vectorNode: VectorNode) {
    when (vectorNode) {
        is VectorNode.Group -> {
            beginControlFlow("%M", MemberNames.Group)
            vectorNode.paths.forEach { path ->
                addRecursively(path)
            }
            endControlFlow()
        }
        is VectorNode.Path -> {
            addPath(vectorNode) {
                vectorNode.nodes.forEach { pathNode ->
                    addStatement(pathNode.asFunctionCall())
                }
            }
        }
    }
}

/**
 * Adds a function call to create the given [path], with [pathBody] containing the commands for
 * the path.
 */
private fun CodeBlock.Builder.addPath(
    path: VectorNode.Path,
    pathBody: CodeBlock.Builder.() -> Unit
) {
    val hasStrokeColor = path.strokeColorHex != null

    val parameterList = with(path) {
        listOfNotNull(
            "fill = ${getPathFill(path)}",
            "stroke = ${if (hasStrokeColor) "%M(%M(0x$strokeColorHex))" else "null"}",
            "fillAlpha = ${fillAlpha}f".takeIf { fillAlpha != 1f },
            "strokeAlpha = ${strokeAlpha}f".takeIf { strokeAlpha != 1f },
            "strokeLineWidth = ${strokeLineWidth.withMemberIfNotNull}",
            "strokeLineCap = %M",
            "strokeLineJoin = %M",
            "strokeLineMiter = ${strokeLineMiter}f",
            "pathFillType = %M"
        )
    }

    val parameters = parameterList.joinToString(prefix = "(", postfix = ")")

    val members: Array<Any> = listOfNotNull(
        MemberNames.Path,
        MemberNames.SolidColor.takeIf { hasStrokeColor },
        MemberNames.Color.takeIf { hasStrokeColor },
        path.strokeLineWidth.memberName,
        path.strokeLineCap.memberName,
        path.strokeLineJoin.memberName,
        path.fillType.memberName
    ).toMutableList().apply {
        var fillIndex = 1
        when (path.fill) {
            is Fill.Color -> {
                add(fillIndex, MemberNames.SolidColor)
                add(++fillIndex, MemberNames.Color)
            }
            is Fill.LinearGradient -> {
                add(fillIndex, MemberNames.LinearGradient)
                path.fill.colorStops.forEach { _ ->
                    add(++fillIndex, MemberNames.Color)
                }
                add(++fillIndex, MemberNames.Offset)
                add(++fillIndex, MemberNames.Offset)
            }
            is Fill.RadialGradient -> {
                add(fillIndex, MemberNames.RadialGradient)
                path.fill.colorStops.forEach { _ ->
                    add(++fillIndex, MemberNames.Color)
                }
                add(++fillIndex, MemberNames.Offset)
            }
            null -> {}
        }
    }.toTypedArray()

    beginControlFlow(
        "%M$parameters",
        *members
    )

    pathBody()
    endControlFlow()
}

private fun getPathFill(path: VectorNode.Path) = when (path.fill) {
    is Fill.Color -> "%M(%M(0x${path.fill.colorHex}))"
    is Fill.LinearGradient -> {
        with(path.fill) {
            "%M(" +
                    "${getGradientStops(path.fill.colorStops).toString().removeSurrounding("[", "]")}, " +
                    "start = %M(${startX}f,${startY}f), " +
                    "end = %M(${endX}f,${endY}f))"
        }
    }
    is Fill.RadialGradient -> {
        with(path.fill) {
            "%M(${getGradientStops(path.fill.colorStops).toString().removeSurrounding("[", "]")}, " +
                    "center = %M(${centerX}f,${centerY}f), " +
                    "radius = ${gradientRadius}f)"
        }
    }
    else -> "null"
}

private fun getGradientStops(
    stops: List<Pair<Float, String>>
) = stops.map { stop ->
    "${stop.first}f to %M(0x${stop.second})"
}

val GraphicUnit.withMemberIfNotNull: String get() = "${value}${if (memberName != null) ".%M" else "f"}"