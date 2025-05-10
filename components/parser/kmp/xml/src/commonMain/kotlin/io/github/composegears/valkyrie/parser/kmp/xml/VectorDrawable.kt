package io.github.composegears.valkyrie.parser.kmp.xml

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlPolyChildren
import nl.adaptivity.xmlutil.serialization.XmlSerialName

private const val ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android"

@XmlSerialName("vector")
@Serializable
internal data class VectorDrawable(
    @XmlSerialName("name", ANDROID_NAMESPACE) val name: String? = null,
    @XmlSerialName("width", ANDROID_NAMESPACE) val widthInDp: String,
    @XmlSerialName("height", ANDROID_NAMESPACE) val heightInDp: String,
    @XmlSerialName("viewportWidth", ANDROID_NAMESPACE) val viewportWidth: Float,
    @XmlSerialName("viewportHeight", ANDROID_NAMESPACE) val viewportHeight: Float,
    @XmlSerialName("tint", ANDROID_NAMESPACE) val tint: String? = null,
    @XmlSerialName("autoMirrored", ANDROID_NAMESPACE) val autoMirrored: Boolean = false,

    @XmlPolyChildren([GROUP, PATH])
    val children: List<@Polymorphic Child>,
) {
    companion object {
        const val GROUP = "group"
        const val PATH = "path"
    }

    sealed interface Child

    @Serializable
    @SerialName(GROUP)
    @XmlSerialName(GROUP)
    data class Group(
        @XmlSerialName("name", ANDROID_NAMESPACE) val name: String? = null,
        @XmlSerialName("pivotX", ANDROID_NAMESPACE) val pivotX: Float? = null,
        @XmlSerialName("pivotY", ANDROID_NAMESPACE) val pivotY: Float? = null,
        @XmlSerialName("translateX", ANDROID_NAMESPACE) val translateX: Float? = null,
        @XmlSerialName("translateY", ANDROID_NAMESPACE) val translateY: Float? = null,
        @XmlSerialName("scaleX", ANDROID_NAMESPACE) val scaleX: Float? = null,
        @XmlSerialName("scaleY", ANDROID_NAMESPACE) val scaleY: Float? = null,
        @XmlSerialName("rotation", ANDROID_NAMESPACE) val rotation: Float? = null,

        @XmlPolyChildren([GROUP, PATH])
        val children: List<@Polymorphic Child>,
    ) : Child

    @Serializable
    @SerialName(PATH)
    @XmlSerialName(PATH)
    data class Path(
        @XmlSerialName("name", ANDROID_NAMESPACE) val name: String? = null,
        @XmlSerialName("fillType", ANDROID_NAMESPACE) val fillType: String = "nonZero",
        @XmlSerialName("fillColor", ANDROID_NAMESPACE) val fillColor: String? = null,
        @XmlSerialName("pathData", ANDROID_NAMESPACE) val pathData: String,
        @XmlSerialName("alpha", ANDROID_NAMESPACE) val alpha: Float = 1.0f,
        @XmlSerialName("strokeWidth", ANDROID_NAMESPACE) val strokeWidth: String? = null,
        @XmlSerialName("strokeLineCap", ANDROID_NAMESPACE) val strokeLineCap: String? = null,
        @XmlSerialName("strokeLineJoin", ANDROID_NAMESPACE) val strokeLineJoin: String? = null,
        @XmlSerialName("strokeColor", ANDROID_NAMESPACE) val strokeColor: String? = null,
        @XmlSerialName("strokeAlpha", ANDROID_NAMESPACE) val strokeAlpha: String? = null,
        @XmlSerialName("strokeMiterLimit", ANDROID_NAMESPACE) val strokeMiterLimit: String? = null,
    ) : Child
}
