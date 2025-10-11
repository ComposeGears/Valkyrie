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
        defaultPolicy {
            pedantic = false
            repairNamespaces = true
            encodeDefault = XmlSerializationPolicy.XmlEncodeDefault.NEVER
        }
    }

    fun serialize(vector: VectorDrawable): String = xmlConfig.encodeToString(vector)
}
