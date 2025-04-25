package io.github.composegears.valkyrie.parser.jvm.xml

import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrVectorNode
import io.github.composegears.valkyrie.parser.jvm.xml.ext.dpValueAsFloat
import io.github.composegears.valkyrie.parser.jvm.xml.ext.isAtEnd
import io.github.composegears.valkyrie.parser.jvm.xml.ext.seekToStartTag
import io.github.composegears.valkyrie.parser.jvm.xml.ext.valueAsBoolean
import io.github.composegears.valkyrie.parser.jvm.xml.ext.valueAsFillType
import io.github.composegears.valkyrie.parser.jvm.xml.ext.valueAsFloat
import io.github.composegears.valkyrie.parser.jvm.xml.ext.valueAsIrColor
import io.github.composegears.valkyrie.parser.jvm.xml.ext.valueAsPathData
import io.github.composegears.valkyrie.parser.jvm.xml.ext.valueAsString
import io.github.composegears.valkyrie.parser.jvm.xml.ext.valueAsStrokeCap
import io.github.composegears.valkyrie.parser.jvm.xml.ext.valueAsStrokeLineJoin
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.START_TAG
import org.xmlpull.v1.XmlPullParserFactory

object XmlToImageVectorParser {

    fun parse(text: String): IrImageVector {
        val parser = prepareParser(text)

        check(parser.name == "vector") { "The start tag must be <vector>!" }

        val attributes = parseVectorAttributes(parser)

        return IrImageVector(
            defaultWidth = attributes.width,
            defaultHeight = attributes.height,
            viewportWidth = attributes.viewportWidth,
            viewportHeight = attributes.viewportHeight,
            autoMirror = attributes.autoMirrored,
            nodes = parseNodes(parser),
        )
    }
}

private fun prepareParser(text: String): XmlPullParser {
    return XmlPullParserFactory.newInstance().newPullParser().apply {
        setInput(text.byteInputStream(), null)
        seekToStartTag()
    }
}

private fun parseVectorAttributes(parser: XmlPullParser): VectorAttributes {
    return VectorAttributes(
        width = parser.dpValueAsFloat(WIDTH) ?: 0f,
        height = parser.dpValueAsFloat(HEIGHT) ?: 0f,
        viewportWidth = parser.valueAsFloat(VIEWPORT_WIDTH) ?: 0f,
        viewportHeight = parser.valueAsFloat(VIEWPORT_HEIGHT) ?: 0f,
        autoMirrored = parser.valueAsBoolean(AUTO_MIRRORED) ?: false,
    )
}

private fun parseNodes(parser: XmlPullParser): List<IrVectorNode> {
    val nodes = mutableListOf<IrVectorNode>()
    var currentGroup: IrVectorNode.IrGroup? = null

    parser.next()

    while (!parser.isAtEnd()) {
        when (parser.eventType) {
            START_TAG -> {
                when (parser.name) {
                    PATH -> {
                        val path = parsePath(parser)
                        if (currentGroup != null) {
                            currentGroup.paths.add(path)
                        } else {
                            nodes.add(path)
                        }
                    }
                    GROUP -> {
                        val group = parseGroup(parser)
                        currentGroup = group
                        nodes.add(group)
                    }
                    CLIP_PATH -> {
                        currentGroup?.clipPathData?.addAll(parser.valueAsPathData())
                    }
                    GRADIENT -> handleGradient(parser, currentGroup, nodes)
                    ITEM -> handleItem(parser, currentGroup, nodes)
                }
            }
        }
        parser.next()
    }
    return nodes
}

private fun parsePath(parser: XmlPullParser): IrVectorNode.IrPath {
    val fillColor = parser.valueAsIrColor(FILL_COLOR)
    val strokeColor = parser.valueAsIrColor(STROKE_COLOR)

    return IrVectorNode.IrPath(
        name = parser.valueAsString(NAME).orEmpty(),
        fill = when {
            fillColor != null && !fillColor.isTransparent() -> IrFill.Color(fillColor)
            else -> null
        },
        stroke = when {
            strokeColor != null && !strokeColor.isTransparent() -> IrStroke.Color(strokeColor)
            else -> null
        },
        strokeAlpha = parser.valueAsFloat(STROKE_ALPHA) ?: 1f,
        fillAlpha = parser.valueAsFloat(FILL_ALPHA) ?: 1f,
        strokeLineWidth = parser.valueAsFloat(STROKE_WIDTH) ?: 0f,
        strokeLineCap = parser.valueAsStrokeCap(),
        strokeLineJoin = parser.valueAsStrokeLineJoin(),
        strokeLineMiter = parser.valueAsFloat(STROKE_MITER_LIMIT) ?: 4f,
        pathFillType = parser.valueAsFillType(),
        paths = parser.valueAsPathData(),
    )
}

private fun parseGroup(parser: XmlPullParser): IrVectorNode.IrGroup {
    return IrVectorNode.IrGroup(
        name = parser.valueAsString(NAME).orEmpty(),
        rotate = parser.valueAsFloat(ROTATION) ?: 0f,
        pivotX = parser.valueAsFloat(PIVOT_X) ?: 0f,
        pivotY = parser.valueAsFloat(PIVOT_Y) ?: 0f,
        scaleX = parser.valueAsFloat(SCALE_X) ?: 1f,
        scaleY = parser.valueAsFloat(SCALE_Y) ?: 1f,
        translationX = parser.valueAsFloat(TRANSLATE_X) ?: 0f,
        translationY = parser.valueAsFloat(TRANSLATE_Y) ?: 0f,
        paths = mutableListOf(),
        clipPathData = mutableListOf(),
    )
}

private fun handleGradient(
    parser: XmlPullParser,
    currentGroup: IrVectorNode.IrGroup?,
    nodes: MutableList<IrVectorNode>,
) {
    val gradient = parseGradient(parser) ?: return

    val lastPath = currentGroup?.paths?.removeLastOrNull() ?: nodes.removeLastOrNull() ?: return
    if (lastPath is IrVectorNode.IrPath && lastPath.fill == null) {
        val gradientPath = lastPath.copy(fill = gradient)
        if (currentGroup != null) {
            currentGroup.paths.add(gradientPath)
        } else {
            nodes.add(gradientPath)
        }
    }
}

private fun parseGradient(parser: XmlPullParser): IrFill? {
    return when (parser.valueAsString(TYPE)) {
        LINEAR -> {
            val startX = parser.valueAsFloat(START_X) ?: 0f
            val startY = parser.valueAsFloat(START_Y) ?: 0f
            val endX = parser.valueAsFloat(END_X) ?: 0f
            val endY = parser.valueAsFloat(END_Y) ?: 0f

            IrFill.LinearGradient(
                startY = startY,
                startX = startX,
                endY = endY,
                endX = endX,
            )
        }
        RADIAL -> {
            val radius = parser.valueAsFloat(GRADIENT_RADIUS) ?: 0f
            val centerX = parser.valueAsFloat(CENTER_X) ?: 0f
            val centerY = parser.valueAsFloat(CENTER_Y) ?: 0f

            IrFill.RadialGradient(
                radius = radius,
                centerX = centerX,
                centerY = centerY,
            )
        }
        else -> null
    }
}

private fun handleItem(parser: XmlPullParser, currentGroup: IrVectorNode.IrGroup?, nodes: MutableList<IrVectorNode>) {
    val offset = parser.valueAsFloat(OFFSET) ?: 0f
    val color = parser.valueAsIrColor(COLOR) ?: return
    val colorStop = IrFill.ColorStop(offset, color)

    val lastPath = (currentGroup?.paths?.last() ?: nodes.last()) as? IrVectorNode.IrPath
    when (val fill = lastPath?.fill) {
        is IrFill.LinearGradient -> fill.colorStops.add(colorStop)
        is IrFill.RadialGradient -> fill.colorStops.add(colorStop)
        else -> {}
    }
}

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
private const val FILL_ALPHA = "android:fillAlpha"
private const val STROKE_ALPHA = "android:strokeAlpha"
private const val STROKE_WIDTH = "android:strokeWidth"
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

private data class VectorAttributes(
    val width: Float,
    val height: Float,
    val viewportWidth: Float,
    val viewportHeight: Float,
    val autoMirrored: Boolean,
)
