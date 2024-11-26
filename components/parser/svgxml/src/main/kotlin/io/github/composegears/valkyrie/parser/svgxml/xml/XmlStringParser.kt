package io.github.composegears.valkyrie.parser.svgxml.xml

import io.github.composegears.valkyrie.ir.IrColor
import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrStrokeLineCap
import io.github.composegears.valkyrie.ir.IrStrokeLineJoin
import io.github.composegears.valkyrie.ir.IrVectorNode
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.END_DOCUMENT
import org.xmlpull.v1.XmlPullParser.END_TAG
import org.xmlpull.v1.XmlPullParser.START_TAG
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

internal object XmlStringParser {

    fun parse(text: String): IrImageVector {
        val parser = XmlPullParserFactory.newInstance().newPullParser().apply {
            setInput(text.byteInputStream(), null)
            seekToStartTag()
        }

        check(parser.name == "vector") { "The start tag must be <vector>!" }

        val width = parser.getAttributeValue(null, WIDTH).valueToFloat()
        val height = parser.getAttributeValue(null, HEIGHT).valueToFloat()
        val viewportWidth = parser.getAttributeValue(null, VIEWPORT_WIDTH).toFloat()
        val viewportHeight = parser.getAttributeValue(null, VIEWPORT_HEIGHT).toFloat()
        val autoMirrored = parser.getAttributeValue(null, AUTO_MIRRORED)?.toBooleanStrictOrNull() ?: false

        parser.next()

        val nodes = mutableListOf<IrVectorNode>()

        var currentGroup: IrVectorNode.IrGroup? = null

        while (!parser.isAtEnd()) {
            when (parser.eventType) {
                START_TAG -> {
                    when (parser.name) {
                        PATH -> {
                            val pathData = parser.getAttributeValue(
                                null,
                                PATH_DATA,
                            )
                            val fillAlpha = parser.getValueAsFloat(FILL_ALPHA)
                            val strokeAlpha = parser.getValueAsFloat(STROKE_ALPHA)
                            val fillType = when (parser.getAttributeValue(null, FILL_TYPE)) {
                                // evenOdd and nonZero are the only supported values here, where
                                // nonZero is the default if no values are defined.
                                EVEN_ODD -> IrPathFillType.EvenOdd
                                else -> IrPathFillType.NonZero
                            }
                            val strokeCap = parser.getAttributeValue(null, STROKE_LINE_CAP)
                                ?.let {
                                    IrStrokeLineCap.entries
                                        .find { strokeCap -> strokeCap.name.equals(it, ignoreCase = true) }
                                }
                            val strokeWidth = (parser.getAttributeValue(null, STROKE_WIDTH) ?: "0").valueToFloat()
                            val strokeJoin = parser.getAttributeValue(null, STROKE_LINE_JOIN)
                                ?.let {
                                    IrStrokeLineJoin.entries
                                        .find { strokeJoin -> strokeJoin.name.equals(it, ignoreCase = true) }
                                }
                            val strokeMiterLimit = parser.getValueAsFloat(STROKE_MITER_LIMIT)

                            val strokeColor = parser.getAttributeValue(null, STROKE_COLOR)?.let { IrColor(it) }
                            val fillColor = parser.getAttributeValue(null, FILL_COLOR)?.let { IrColor(it) }

                            val name = parser.getAttributeValue(null, NAME).orEmpty()

                            val path = IrVectorNode.IrPath(
                                name = name,
                                fill = when {
                                    fillColor != null && !fillColor.isTransparent() -> IrFill.Color(fillColor)
                                    else -> null
                                },
                                stroke = when {
                                    strokeColor != null && !strokeColor.isTransparent() -> IrStroke.Color(strokeColor)
                                    else -> null
                                },
                                strokeAlpha = strokeAlpha ?: 1f,
                                fillAlpha = fillAlpha ?: 1f,
                                strokeLineWidth = strokeWidth,
                                strokeLineCap = strokeCap ?: IrStrokeLineCap.Butt,
                                strokeLineJoin = strokeJoin ?: IrStrokeLineJoin.Miter,
                                strokeLineMiter = strokeMiterLimit ?: 4.0f,
                                pathFillType = fillType,
                                paths = PathParser.parsePathString(pathData),
                            )
                            if (currentGroup != null) {
                                currentGroup.paths.add(path)
                            } else {
                                nodes.add(path)
                            }
                        }
                        GROUP -> {
                            val name = parser.getAttributeValue(null, NAME).orEmpty()
                            val rotate = parser.getValueAsFloat(ROTATION) ?: 0f
                            val pivotX = parser.getValueAsFloat(PIVOT_X) ?: 0f
                            val pivotY = parser.getValueAsFloat(PIVOT_Y) ?: 0f
                            val scaleX = parser.getValueAsFloat(SCALE_X) ?: 1f
                            val scaleY = parser.getValueAsFloat(SCALE_Y) ?: 1f
                            val translateX = parser.getValueAsFloat(TRANSLATE_X) ?: 0f
                            val translateY = parser.getValueAsFloat(TRANSLATE_Y) ?: 0f

                            val group = IrVectorNode.IrGroup(
                                name = name,
                                rotate = rotate,
                                pivotX = pivotX,
                                pivotY = pivotY,
                                scaleX = scaleX,
                                scaleY = scaleY,
                                translationX = translateX,
                                translationY = translateY,
                                clipPathData = mutableListOf(),
                                paths = mutableListOf(),
                            )
                            currentGroup = group
                            nodes.add(group)
                        }
                        CLIP_PATH -> {
                            val pathData = parser.getAttributeValue(null, PATH_DATA)

                            if (pathData != null) {
                                currentGroup?.clipPathData?.addAll(PathParser.parsePathString(pathData))
                            }
                        }
                        GRADIENT -> {
                            val gradient = when (parser.getAttributeValue(null, TYPE)) {
                                LINEAR -> {
                                    val startX = parser.getValueAsFloat(START_X) ?: 0f
                                    val startY = parser.getValueAsFloat(START_Y) ?: 0f
                                    val endX = parser.getValueAsFloat(END_X) ?: 0f
                                    val endY = parser.getValueAsFloat(END_Y) ?: 0f
                                    IrFill.LinearGradient(
                                        startY = startY,
                                        startX = startX,
                                        endX = endX,
                                        endY = endY,
                                    )
                                }
                                RADIAL -> {
                                    val gradientRadius = parser.getValueAsFloat(GRADIENT_RADIUS) ?: 0f
                                    val centerX = parser.getValueAsFloat(CENTER_X) ?: 0f
                                    val centerY = parser.getValueAsFloat(CENTER_Y) ?: 0f
                                    IrFill.RadialGradient(
                                        radius = gradientRadius,
                                        centerX = centerX,
                                        centerY = centerY,
                                    )
                                }
                                else -> null
                            }

                            val lastPath = currentGroup?.paths?.removeLast() ?: nodes.removeLast()
                            if (lastPath as? IrVectorNode.IrPath != null && lastPath.fill == null) {
                                val gradientPath = lastPath.copy(fill = gradient)
                                if (currentGroup != null) {
                                    currentGroup.paths.add(gradientPath)
                                } else {
                                    nodes.add(gradientPath)
                                }
                            }
                        }
                        ITEM -> {
                            val offset = parser.getValueAsFloat(OFFSET) ?: 0f
                            val irColor = IrColor(hex = parser.getAttributeValue(null, COLOR))

                            val colorStop = IrFill.ColorStop(
                                offset = offset,
                                irColor = irColor,
                            )
                            val lastPath = (currentGroup?.paths?.last() ?: nodes.last()) as? IrVectorNode.IrPath
                            when (val fill = lastPath?.fill) {
                                is IrFill.LinearGradient -> fill.colorStops.add(colorStop)
                                is IrFill.RadialGradient -> fill.colorStops.add(colorStop)
                                else -> {}
                            }
                        }
                    }
                }
            }
            parser.next()
        }

        return IrImageVector(
            defaultWidth = width,
            defaultHeight = height,
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight,
            autoMirror = autoMirrored,
            nodes = nodes,
        )
    }
}

private fun String.valueToFloat() = removeSuffix("dp").toFloat()

/**
 * @return the float value for the attribute [name], or null if it couldn't be found
 */
private fun XmlPullParser.getValueAsFloat(name: String) = getAttributeValue(null, name)?.toFloatOrNull()

private fun XmlPullParser.seekToStartTag(): XmlPullParser {
    var type = next()
    while (type != START_TAG && type != END_DOCUMENT) {
        // Empty loop
        type = next()
    }
    if (type != START_TAG) {
        throw XmlPullParserException("No start tag found")
    }
    return this
}

private fun XmlPullParser.isAtEnd() = eventType == END_DOCUMENT || (depth < 1 && eventType == END_TAG)

// XML tag names
private const val CLIP_PATH = "clip-path"
private const val GROUP = "group"
private const val PATH = "path"
private const val GRADIENT = "gradient"
private const val ITEM = "item"

// XML  names
private const val LINEAR = "linear"
private const val RADIAL = "radial"

// Group XML attribute names
const val ROTATION = "android:rotation"
const val PIVOT_X = "android:pivotX"
const val PIVOT_Y = "android:pivotY"
const val SCALE_X = "android:scaleX"
const val SCALE_Y = "android:scaleY"
const val TRANSLATE_X = "android:translateX"
const val TRANSLATE_Y = "android:translateY"

// Path XML attribute names
private const val NAME = "android:name"
private const val PATH_DATA = "android:pathData"
private const val FILL_ALPHA = "android:fillAlpha"
private const val STROKE_ALPHA = "android:strokeAlpha"
private const val FILL_TYPE = "android:fillType"
private const val STROKE_LINE_CAP = "android:strokeLineCap"
private const val STROKE_WIDTH = "android:strokeWidth"
private const val STROKE_LINE_JOIN = "android:strokeLineJoin"
private const val STROKE_MITER_LIMIT = "android:strokeMiterLimit"
private const val STROKE_COLOR = "android:strokeColor"
private const val FILL_COLOR = "android:fillColor"

// Gradient XML attribute names
private const val TYPE = "android:type"
private const val START_Y = "android:startY"
private const val START_X = "android:startX"
private const val END_Y = "android:endY"
private const val END_X = "android:endX"
private const val GRADIENT_RADIUS = "android:gradientRadius"
private const val CENTER_X = "android:centerX"
private const val CENTER_Y = "android:centerY"

// Item XML attribute names
private const val OFFSET = "android:offset"
private const val COLOR = "android:color"

// Vector XML attribute names
private const val WIDTH = "android:width"
private const val HEIGHT = "android:height"
private const val VIEWPORT_WIDTH = "android:viewportWidth"
private const val VIEWPORT_HEIGHT = "android:viewportHeight"
private const val AUTO_MIRRORED = "android:autoMirrored"

// XML attribute values
private const val EVEN_ODD = "evenOdd"
