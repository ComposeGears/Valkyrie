package io.github.composegears.valkyrie.parser.jvm.svg

import java.io.StringReader
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import org.w3c.dom.Element

/**
 * Utility for manipulating SVG content using DOM-based XML parsing.
 *
 * This provides robust SVG modification capabilities that handle:
 * - Attribute spacing and quote variations
 * - Attribute ordering differences
 * - Multiple occurrences of attributes across nested elements
 */
object SvgManipulator {

    /**
     * Applies attribute modifications to SVG content.
     *
     * @param svgContent The original SVG content as a string
     * @param modifications Lambda that receives the root SVG element for modification
     * @return Modified SVG content, or original content if parsing fails
     */
    fun modifySvg(
        svgContent: String,
        modifications: (Element) -> Unit,
    ): String {
        return try {
            val factory = DocumentBuilderFactory.newInstance()
            factory.apply {
                setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)
                setFeature("http://xml.org/sax/features/external-general-entities", false)
                setFeature("http://xml.org/sax/features/external-parameter-entities", false)
            }
            val builder = factory.newDocumentBuilder()
            val document = builder.parse(org.xml.sax.InputSource(StringReader(svgContent)))

            val svgElement = document.documentElement
            modifications(svgElement)

            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
            val writer = StringWriter()
            transformer.transform(DOMSource(document), StreamResult(writer))
            writer.toString()
        } catch (e: Exception) {
            println("Failed to parse SVG for modification: ${e.message}")
            svgContent
        }
    }

    /**
     * Updates an attribute on all elements in the tree that have it.
     *
     * @param element Root element to start searching from
     * @param attributeName Name of the attribute to update
     * @param newValue New value for the attribute
     */
    fun updateAttributeRecursively(
        element: Element,
        attributeName: String,
        newValue: String,
    ) {
        if (element.hasAttribute(attributeName)) {
            element.setAttribute(attributeName, newValue)
        }

        val children = element.childNodes
        for (i in 0 until children.length) {
            val child = children.item(i)
            if (child is Element) {
                updateAttributeRecursively(child, attributeName, newValue)
            }
        }
    }

    /**
     * Updates an attribute on all elements that have a specific current value.
     *
     * @param element Root element to start searching from
     * @param attributeName Name of the attribute to update
     * @param currentValue Current value to match
     * @param newValue New value for the attribute
     */
    fun updateAttributeConditionally(
        element: Element,
        attributeName: String,
        currentValue: String,
        newValue: String,
    ) {
        if (element.hasAttribute(attributeName)) {
            val current = element.getAttribute(attributeName)
            if (current == currentValue) {
                element.setAttribute(attributeName, newValue)
            }
        }

        val children = element.childNodes
        for (i in 0 until children.length) {
            val child = children.item(i)
            if (child is Element) {
                updateAttributeConditionally(child, attributeName, currentValue, newValue)
            }
        }
    }
}
