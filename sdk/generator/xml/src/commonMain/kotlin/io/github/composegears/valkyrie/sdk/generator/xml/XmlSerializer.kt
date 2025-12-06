package io.github.composegears.valkyrie.sdk.generator.xml

import io.github.composegears.valkyrie.sdk.core.xml.VectorDrawable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlSerializationPolicy

internal object XmlSerializer {
    private val baseModule = SerializersModule {
        polymorphic(VectorDrawable.Child::class) {
            subclass(VectorDrawable.Group::class)
            subclass(VectorDrawable.Path::class)
            subclass(VectorDrawable.ClipPath::class)
        }
    }

    @OptIn(ExperimentalXmlUtilApi::class)
    private val xmlConfig = XML(baseModule) {
        autoPolymorphic = true
        indent = 4
        defaultPolicy {
            pedantic = false
            repairNamespaces = true
            encodeDefault = XmlSerializationPolicy.XmlEncodeDefault.NEVER
        }
    }

    fun serialize(vector: VectorDrawable): String {
        val rawXml = xmlConfig.encodeToString(vector)
        val formattedXml = formatXmlAttributes(rawXml)
        return hoistAaptNamespace(formattedXml)
    }

    /**
     * Moves xmlns:aapt namespace declaration from individual aapt:attr elements to the root vector element.
     */
    private fun hoistAaptNamespace(xml: String): String {
        if (!xml.contains("<aapt:attr")) {
            return xml
        }

        var result = xml.replace(Regex("""xmlns:aapt="http://schemas\.android\.com/aapt"\s*"""), "")

        result = result.replaceFirst(
            Regex("""(<vector[^>]*xmlns:android="http://schemas\.android\.com/apk/res/android")"""),
            """$1 xmlns:aapt="http://schemas.android.com/aapt"""",
        )

        return result
    }

    /**
     * Formats XML to place attributes on separate lines for better readability.
     */
    private fun formatXmlAttributes(xml: String): String = buildString {
        var indentLevel = 0

        xml.lineSequence().forEach { line ->
            val trimmed = line.trim()
            if (trimmed.isEmpty()) return@forEach

            when {
                // Closing tag
                trimmed.startsWith("</") -> {
                    indentLevel = maxOf(0, indentLevel - 1)
                    appendIndentedLine(trimmed, indentLevel)
                }
                // Opening tag (not closing, not declaration)
                trimmed.startsWith("<") && !trimmed.startsWith("<?") -> {
                    val shouldFormat = shouldFormatTag(trimmed)

                    if (shouldFormat) {
                        append(formatTag(trimmed, indentLevel))
                    } else {
                        appendIndentedLine(trimmed, indentLevel)
                    }

                    if (!trimmed.endsWith("/>") && !trimmed.endsWith("?>")) {
                        indentLevel++
                    }
                }
                else -> appendIndentedLine(trimmed, indentLevel)
            }
        }
    }

    private fun shouldFormatTag(tag: String): Boolean {
        val attributeCount = tag.count { it == '=' }
        return attributeCount > 2 || (attributeCount > 1 && tag.length > 100)
    }

    private fun StringBuilder.appendIndentedLine(text: String, level: Int) {
        append("    ".repeat(level))
        append(text)
        append('\n')
    }

    /**
     * Formats a tag by placing each attribute on a new line.
     */
    private fun formatTag(tag: String, indentLevel: Int): String = buildString {
        val isSelfClosing = tag.endsWith("/>")
        val tagContent = tag.removeSuffix("/>").removeSuffix(">")

        val parts = parseTagParts(tagContent)
        if (parts.isEmpty()) {
            appendIndentedLine(tag, indentLevel)
            return@buildString
        }

        val baseIndent = "    ".repeat(indentLevel)
        val attrIndent = "    ".repeat(indentLevel + 1)

        append(baseIndent)
        append(parts.first())
        append('\n')

        val attributes = parts.drop(1)
        attributes.dropLast(1).forEach { attr ->
            append(attrIndent)
            append(attr)
            append('\n')
        }

        // Last attribute with closing bracket on same line
        if (attributes.isNotEmpty()) {
            append(attrIndent)
            append(attributes.last())
            append(if (isSelfClosing) "/>" else ">")
            append('\n')
        } else {
            append(if (isSelfClosing) "/>" else ">")
            append('\n')
        }
    }

    /**
     * Splits a tag into parts (tag name + attributes) while preserving quoted values.
     */
    private fun parseTagParts(tagContent: String): List<String> {
        val parts = mutableListOf<String>()
        var current = StringBuilder()
        var inQuotes = false

        tagContent.forEach { char ->
            when (char) {
                '"' -> {
                    inQuotes = !inQuotes
                    current.append(char)
                }
                ' ' if !inQuotes -> {
                    if (current.isNotEmpty()) {
                        parts.add(current.toString())
                        current = StringBuilder()
                    }
                }
                else -> current.append(char)
            }
        }

        if (current.isNotEmpty()) {
            parts.add(current.toString())
        }

        return parts
    }
}
