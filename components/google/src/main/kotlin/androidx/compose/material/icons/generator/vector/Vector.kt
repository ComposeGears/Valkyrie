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

package androidx.compose.material.icons.generator.vector

import androidx.compose.material.icons.generator.GraphicUnit
import java.awt.LinearGradientPaint

/**
 * Simplified representation of a vector, with root [nodes].
 *
 * [nodes] may either be a singleton list of the root group, or a list of root paths / groups if
 * there are multiple top level declaration.
 */
class Vector(
    val width: GraphicUnit,
    val height: GraphicUnit,
    val viewportWidth: Float,
    val viewportHeight: Float,
    val nodes: List<VectorNode>
)

/**
 * Simplified vector node representation, as the total set of properties we need to care about
 * for Material icons is very limited.
 */
sealed class VectorNode {
    class Group(val paths: MutableList<Path> = mutableListOf()) : VectorNode()
    data class Path(
        val fill: Fill?,
        val strokeColorHex: String?,
        val strokeAlpha: Float,
        val fillAlpha: Float,
        val strokeLineWidth: GraphicUnit,
        val strokeLineCap: StrokeCap,
        val strokeLineJoin: StrokeJoin,
        val strokeLineMiter: Float,
        val fillType: FillType,
        val nodes: List<PathNode>
    ) : VectorNode()
}

sealed class Fill {
    data class Color(val colorHex: String) : Fill()
    data class LinearGradient(
        val startY: Float,
        val startX: Float,
        val endY: Float,
        val endX: Float,
        val colorStops: MutableList<Pair<Float,String>> = mutableListOf()
    ) : Fill()
    data class RadialGradient(
        val gradientRadius: Float,
        val centerX: Float,
        val centerY: Float,
        val colorStops: MutableList<Pair<Float,String>> = mutableListOf()
    ): Fill()
}