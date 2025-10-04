package io.github.composegears.valkyrie.sdk.core.xml

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlPolyChildren
import nl.adaptivity.xmlutil.serialization.XmlSerialName

private const val ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android"
private const val ANDROID_PREFIX = "android"

@XmlSerialName(value = "vector", prefix = ANDROID_PREFIX)
@Serializable
data class VectorDrawable(
    @XmlSerialName("name", ANDROID_NAMESPACE, ANDROID_PREFIX) val name: String? = null,
    @XmlSerialName("width", ANDROID_NAMESPACE, ANDROID_PREFIX) val widthInDp: String,
    @XmlSerialName("height", ANDROID_NAMESPACE, ANDROID_PREFIX) val heightInDp: String,
    @XmlSerialName("viewportWidth", ANDROID_NAMESPACE, ANDROID_PREFIX) val viewportWidth: Float,
    @XmlSerialName("viewportHeight", ANDROID_NAMESPACE, ANDROID_PREFIX) val viewportHeight: Float,
    @XmlSerialName("tint", ANDROID_NAMESPACE, ANDROID_PREFIX) val tint: String? = null,
    @XmlSerialName("autoMirrored", ANDROID_NAMESPACE, ANDROID_PREFIX) val autoMirrored: Boolean = false,

    @XmlPolyChildren([GROUP, PATH, CLIP_PATH])
    val children: List<@Polymorphic Child>,
) {
    companion object {
        const val GROUP = "group"
        const val PATH = "path"
        const val CLIP_PATH = "clip-path"
    }

    sealed interface Child

    @Serializable
    @SerialName(GROUP)
    @XmlSerialName(value = GROUP, prefix = ANDROID_PREFIX)
    data class Group(
        @XmlSerialName("name", ANDROID_NAMESPACE, ANDROID_PREFIX) val name: String? = null,
        @XmlSerialName("pivotX", ANDROID_NAMESPACE, ANDROID_PREFIX) val pivotX: Float? = null,
        @XmlSerialName("pivotY", ANDROID_NAMESPACE, ANDROID_PREFIX) val pivotY: Float? = null,
        @XmlSerialName("translateX", ANDROID_NAMESPACE, ANDROID_PREFIX) val translateX: Float? = null,
        @XmlSerialName("translateY", ANDROID_NAMESPACE, ANDROID_PREFIX) val translateY: Float? = null,
        @XmlSerialName("scaleX", ANDROID_NAMESPACE, ANDROID_PREFIX) val scaleX: Float? = null,
        @XmlSerialName("scaleY", ANDROID_NAMESPACE, ANDROID_PREFIX) val scaleY: Float? = null,
        @XmlSerialName("rotation", ANDROID_NAMESPACE, ANDROID_PREFIX) val rotation: Float? = null,

        @XmlPolyChildren([GROUP, PATH, CLIP_PATH])
        val children: List<@Polymorphic Child>,
    ) : Child

    @Serializable
    @SerialName(PATH)
    @XmlSerialName(value = PATH, prefix = ANDROID_PREFIX)
    data class Path(
        @XmlSerialName("name", ANDROID_NAMESPACE, ANDROID_PREFIX) val name: String? = null,
        @XmlSerialName("fillType", ANDROID_NAMESPACE, ANDROID_PREFIX) val fillType: String = "nonZero",
        @XmlSerialName("fillColor", ANDROID_NAMESPACE, ANDROID_PREFIX) val fillColor: String? = null,
        @XmlSerialName("pathData", ANDROID_NAMESPACE, ANDROID_PREFIX) val pathData: String,
        @XmlSerialName("fillAlpha", ANDROID_NAMESPACE, ANDROID_PREFIX) val alpha: Float = 1.0f,
        @XmlSerialName("strokeWidth", ANDROID_NAMESPACE, ANDROID_PREFIX) val strokeWidth: String? = null,
        @XmlSerialName("strokeLineCap", ANDROID_NAMESPACE, ANDROID_PREFIX) val strokeLineCap: String? = null,
        @XmlSerialName("strokeLineJoin", ANDROID_NAMESPACE, ANDROID_PREFIX) val strokeLineJoin: String? = null,
        @XmlSerialName("strokeColor", ANDROID_NAMESPACE, ANDROID_PREFIX) val strokeColor: String? = null,
        @XmlSerialName("strokeAlpha", ANDROID_NAMESPACE, ANDROID_PREFIX) val strokeAlpha: String? = null,
        @XmlSerialName("strokeMiterLimit", ANDROID_NAMESPACE, ANDROID_PREFIX) val strokeMiterLimit: String? = null,
    ) : Child

    @Serializable
    @SerialName(CLIP_PATH)
    @XmlSerialName(value = CLIP_PATH, prefix = ANDROID_PREFIX)
    data class ClipPath(
        @XmlSerialName("name", ANDROID_NAMESPACE, ANDROID_PREFIX) val name: String? = null,
        @XmlSerialName("pathData", ANDROID_NAMESPACE, ANDROID_PREFIX) val pathData: String,
    ) : Child
}
