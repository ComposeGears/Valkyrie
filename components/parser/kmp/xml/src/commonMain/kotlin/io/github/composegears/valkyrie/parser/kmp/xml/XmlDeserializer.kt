package io.github.composegears.valkyrie.parser.kmp.xml

import io.github.composegears.valkyrie.sdk.core.xml.VectorDrawable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.serialization.DefaultXmlSerializationPolicy
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlConfig

internal object XmlDeserializer {
    private val baseModule = SerializersModule {
        polymorphic(VectorDrawable.Child::class) {
            subclass(VectorDrawable.Group::class)
            subclass(VectorDrawable.Path::class)
            subclass(VectorDrawable.ClipPath::class)
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
        serializersModule = baseModule,
    )

    fun deserialize(content: String): VectorDrawable = xmlConfig.decodeFromString(content)
}
