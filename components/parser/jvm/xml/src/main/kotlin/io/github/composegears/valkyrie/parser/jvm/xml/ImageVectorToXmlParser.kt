package io.github.composegears.valkyrie.parser.jvm.xml

import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrVectorNode
import io.github.composegears.valkyrie.parser.common.toPathString
import java.io.StringWriter
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

object ImageVectorToXmlParser {

    // XML namespace
    private const val ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android"
    private const val ANDROID_PREFIX = "android"

    // XML tag names
    private const val VECTOR = "vector"
    private const val GROUP = "group"
    private const val PATH = "path"

    // Vector XML attribute names
    private const val NAME = "name"
    private const val WIDTH = "width"
    private const val HEIGHT = "height"
    private const val VIEWPORT_WIDTH = "viewportWidth"
    private const val VIEWPORT_HEIGHT = "viewportHeight"
    private const val AUTO_MIRRORED = "autoMirrored"

    // Group XML attribute names
    private const val PIVOT_X = "pivotX"
    private const val PIVOT_Y = "pivotY"
    private const val TRANSLATE_X = "translateX"
    private const val TRANSLATE_Y = "translateY"
    private const val SCALE_X = "scaleX"
    private const val SCALE_Y = "scaleY"
    private const val ROTATION = "rotation"

    // Path XML attribute names
    private const val PATH_DATA = "pathData"
    private const val FILL_TYPE = "fillType"
    private const val FILL_COLOR = "fillColor"
    private const val FILL_ALPHA = "fillAlpha"
    private const val STROKE_COLOR = "strokeColor"
    private const val STROKE_ALPHA = "strokeAlpha"
    private const val STROKE_WIDTH = "strokeWidth"
    private const val STROKE_LINE_CAP = "strokeLineCap"
    private const val STROKE_LINE_JOIN = "strokeLineJoin"
    private const val STROKE_MITER_LIMIT = "strokeMiterLimit"

    fun parse(imageVector: IrImageVector): String {
        val stringWriter = StringWriter()
        val xmlOutputFactory = XMLOutputFactory.newInstance()
        val xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter)

        try {
            writeVectorDrawable(xmlStreamWriter, imageVector)
            xmlStreamWriter.flush()
        } finally {
            xmlStreamWriter.close()
        }

        // Format the XML with proper indentation
        return formatXml(stringWriter.toString())
    }

    private fun formatXml(xmlString: String): String {
        return try {
            val transformerFactory = TransformerFactory.newInstance()
            val transformer = transformerFactory.newTransformer().apply {
                setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
                setOutputProperty(OutputKeys.INDENT, "yes")
                setOutputProperty(OutputKeys.ENCODING, "UTF-8")
                setOutputProperty(OutputKeys.VERSION, "1.0")
                setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "")
            }
            val source = StreamSource(java.io.StringReader(xmlString))
            val result = StreamResult(StringWriter())

            transformer.transform(source, result)
            result.writer.toString()
        } catch (_: Exception) {
            xmlString
        }
    }

    private fun writeVectorDrawable(writer: XMLStreamWriter, imageVector: IrImageVector) {
        writer.apply {
            writeStartElement(VECTOR)
            writeNamespace(ANDROID_PREFIX, ANDROID_NAMESPACE)

            if (imageVector.name.isNotEmpty()) {
                writeAttribute(ANDROID_NAMESPACE, NAME, imageVector.name)
            }

            writeAttribute(ANDROID_NAMESPACE, WIDTH, formatDpValue(imageVector.defaultWidth))
            writeAttribute(ANDROID_NAMESPACE, HEIGHT, formatDpValue(imageVector.defaultHeight))
            writeAttribute(ANDROID_NAMESPACE, VIEWPORT_WIDTH, formatFloatValue(imageVector.viewportWidth))
            writeAttribute(ANDROID_NAMESPACE, VIEWPORT_HEIGHT, formatFloatValue(imageVector.viewportHeight))

            if (imageVector.autoMirror) {
                writeAttribute(ANDROID_NAMESPACE, AUTO_MIRRORED, "true")
            }

            imageVector.nodes.forEach { node ->
                writeNode(writer, node)
            }

            writeEndElement() // vector
            writeEndDocument()
        }
    }

    private fun writeNode(writer: XMLStreamWriter, node: IrVectorNode) {
        when (node) {
            is IrVectorNode.IrGroup -> writeGroup(writer, node)
            is IrVectorNode.IrPath -> writePath(writer, node)
        }
    }

    private fun writeGroup(writer: XMLStreamWriter, group: IrVectorNode.IrGroup) {
        writer.writeStartElement(GROUP)

        with(group) {
            if (name.isNotEmpty()) {
                writer.writeAttribute(ANDROID_NAMESPACE, NAME, name)
            }

            if (pivotX != 0f) {
                writer.writeAttribute(ANDROID_NAMESPACE, PIVOT_X, formatFloatValue(pivotX))
            }
            if (pivotY != 0f) {
                writer.writeAttribute(ANDROID_NAMESPACE, PIVOT_Y, formatFloatValue(pivotY))
            }
            if (translationX != 0f) {
                writer.writeAttribute(ANDROID_NAMESPACE, TRANSLATE_X, formatFloatValue(translationX))
            }
            if (translationY != 0f) {
                writer.writeAttribute(ANDROID_NAMESPACE, TRANSLATE_Y, formatFloatValue(translationY))
            }
            if (scaleX != 1f) {
                writer.writeAttribute(ANDROID_NAMESPACE, SCALE_X, formatFloatValue(scaleX))
            }
            if (scaleY != 1f) {
                writer.writeAttribute(ANDROID_NAMESPACE, SCALE_Y, formatFloatValue(scaleY))
            }
            if (rotate != 0f) {
                writer.writeAttribute(ANDROID_NAMESPACE, ROTATION, formatFloatValue(rotate))
            }

            nodes.forEach { node ->
                writeNode(writer, node)
            }
        }

        writer.writeEndElement() // group
    }

    private fun writePath(writer: XMLStreamWriter, path: IrVectorNode.IrPath) {
        writer.writeStartElement(PATH)

        if (path.name.isNotEmpty()) {
            writer.writeAttribute(ANDROID_NAMESPACE, NAME, path.name)
        }

        // Path data is required
        writer.writeAttribute(ANDROID_NAMESPACE, PATH_DATA, path.paths.toPathString(::formatFloatValue))

        // Fill type
        when (path.pathFillType) {
            IrPathFillType.EvenOdd -> writer.writeAttribute(ANDROID_NAMESPACE, FILL_TYPE, "evenOdd")
            else -> {} // nonZero is default
        }

        // Fill color and alpha
        when (val fill = path.fill) {
            is IrFill.Color -> {
                writer.writeAttribute(ANDROID_NAMESPACE, FILL_COLOR, fill.irColor.toHexColor())
                if (path.fillAlpha != 1f) {
                    writer.writeAttribute(ANDROID_NAMESPACE, FILL_ALPHA, formatFloatValue(path.fillAlpha))
                }
            }
            else -> {} // No fill color for gradients or null
        }

        // Stroke properties
        when (val stroke = path.stroke) {
            is IrStroke.Color -> {
                writer.writeAttribute(ANDROID_NAMESPACE, STROKE_COLOR, stroke.irColor.toHexColor())
            }
            else -> {} // No stroke
        }
        if (path.strokeLineWidth != 0f) {
            writer.writeAttribute(ANDROID_NAMESPACE, STROKE_WIDTH, formatFloatValue(path.strokeLineWidth))
        }
        if (path.strokeAlpha != 1f) {
            writer.writeAttribute(ANDROID_NAMESPACE, STROKE_ALPHA, formatFloatValue(path.strokeAlpha))
        }
        if (path.strokeLineCap.name != "Butt") {
            writer.writeAttribute(ANDROID_NAMESPACE, STROKE_LINE_CAP, path.strokeLineCap.name.lowercase())
        }
        if (path.strokeLineJoin.name != "Miter") {
            writer.writeAttribute(ANDROID_NAMESPACE, STROKE_LINE_JOIN, path.strokeLineJoin.name.lowercase())
        }
        if (path.strokeLineMiter != 4f) {
            writer.writeAttribute(ANDROID_NAMESPACE, STROKE_MITER_LIMIT, formatFloatValue(path.strokeLineMiter))
        }

        writer.writeEndElement() // path
    }

    private fun formatDpValue(value: Float): String {
        return if (value.isFinite()) "${formatFloatValue(value)}dp" else "0dp"
    }

    private fun formatFloatValue(v: Float): String {
        if (!v.isFinite()) return "0"
        val scaled = kotlin.math.round(v * 1000f) / 1000f
        val raw = scaled.toString()
        return raw.trimEnd('0').trimEnd('.')
    }
}
