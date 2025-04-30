package io.github.composegears.valkyrie.parser.kmp.svg

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import nl.adaptivity.xmlutil.ExperimentalXmlUtilApi
import nl.adaptivity.xmlutil.serialization.UnknownChildHandler
import nl.adaptivity.xmlutil.serialization.XML

internal object SVGDeserializer {

    private val polyModule = SerializersModule {
        polymorphic(baseClass = SVG.Child::class) {
            subclass(SVG.Circle::class)
            subclass(SVG.Group::class)
            subclass(SVG.Ellipse::class)
            subclass(SVG.Path::class)
            subclass(SVG.Polygon::class)
            subclass(SVG.Rectangle::class)
        }
    }

    @OptIn(ExperimentalXmlUtilApi::class)
    private val xmlConfig = XML(serializersModule = polyModule) {
        autoPolymorphic = true
        defaultPolicy {
            pedantic = false
            repairNamespaces = true
            unknownChildHandler = UnknownChildHandler { _, _, _, _, _ -> emptyList() }
        }
    }

    fun deserialize(content: String): SVG = xmlConfig.decodeFromString(content)
}
