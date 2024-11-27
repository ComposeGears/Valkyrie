package io.github.composegears.valkyrie.parser.svgxml.xml.ext

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParser.END_DOCUMENT
import org.xmlpull.v1.XmlPullParser.END_TAG
import org.xmlpull.v1.XmlPullParser.START_TAG
import org.xmlpull.v1.XmlPullParserException

internal fun XmlPullParser.dpValueAsFloat(name: String): Float? {
    return getAttribute(name)
        ?.removeSuffix("dp")
        ?.toFloatOrNull()
}

internal fun XmlPullParser.valueAsBoolean(name: String): Boolean? = getAttribute(name)?.toBooleanStrictOrNull()

internal fun XmlPullParser.valueAsString(name: String): String? = getAttribute(name)

internal fun XmlPullParser.valueAsFloat(name: String): Float? = getAttribute(name)?.toFloatOrNull()

internal fun XmlPullParser.getAttribute(name: String): String? = getAttributeValue(null, name)

internal fun XmlPullParser.seekToStartTag(): XmlPullParser {
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

internal fun XmlPullParser.isAtEnd() = eventType == END_DOCUMENT || (depth < 1 && eventType == END_TAG)
