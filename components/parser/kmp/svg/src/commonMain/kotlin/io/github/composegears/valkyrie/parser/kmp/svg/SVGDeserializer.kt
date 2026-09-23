package io.github.composegears.valkyrie.parser.kmp.svg

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.serialization.DefaultXmlSerializationPolicy
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlConfig

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

    private val xmlConfig = XML(
        XmlConfig(
            XmlConfig.DefaultBuilder(
                repairNamespaces = true,
                xmlDeclMode = XmlDeclMode.None,
                policy = DefaultXmlSerializationPolicy {
                    autoPolymorphic = true
                    pedantic = false
                    ignoreUnknownChildren()
                },
            ),
        ),
        serializersModule = polyModule,
    )

    fun deserialize(content: String): SVG = xmlConfig.decodeFromString(content)
}
