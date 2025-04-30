package io.github.composegears.valkyrie.parser.kmp.svg

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlPolyChildren
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Suppress("ktlint")
private const val svgNamespace = "http://www.w3.org/2000/svg"

@Serializable
@SerialName("svg")
@XmlSerialName("svg", namespace = svgNamespace)
internal data class SVG(
    @SerialName("width") val width: String? = null,
    @SerialName("height") val height: String? = null,
    @SerialName("viewBox") val viewBox: String? = null,
    @SerialName("fill") val fill: String? = null,
    @XmlPolyChildren([GROUP, PATH, CIRCLE, RECTANGLE, ELLIPSE, POLYGON])
    val children: List<@Polymorphic Child> = emptyList(),
) {

    companion object {
        const val GROUP = "g"
        const val PATH = "path"
        const val CIRCLE = "circle"
        const val RECTANGLE = "rect"
        const val ELLIPSE = "ellipse"
        const val POLYGON = "polygon"
    }

    sealed interface Child {
        val id: String?

        // Stroke related values
        val strokeColor: String?
        val strokeWidth: String?
        val strokeLineCap: String?
        val strokeLineJoin: String?
        val strokeAlpha: String?
        val strokeMiter: String?
    }

    @Serializable
    @SerialName(GROUP)
    @XmlSerialName(GROUP)
    data class Group(
        @SerialName("id") override val id: String? = null,
        @SerialName("transform") val transform: String? = null,
        @XmlPolyChildren([GROUP, PATH, CIRCLE, RECTANGLE, ELLIPSE, POLYGON])
        val children: List<@Polymorphic Child> = emptyList(),

        @SerialName("stroke") override val strokeColor: String? = null,
        @SerialName("stroke-width") override val strokeWidth: String? = null,
        @SerialName("stroke-linecap") override val strokeLineCap: String? = null,
        @SerialName("stroke-linejoin") override val strokeLineJoin: String? = null,
        @SerialName("stroke-opacity") override val strokeAlpha: String? = null,
        @SerialName("stroke-miterlimit") override val strokeMiter: String? = null,
    ) : Child

    @Serializable
    @SerialName(PATH)
    @XmlSerialName(PATH)
    data class Path(
        @SerialName("id") override val id: String? = null,
        @SerialName("d") val pathData: String,
        @SerialName("fill") val fill: String? = null,
        @SerialName("fill-rule") val fillRule: String = "nonzero",
        @SerialName("fill-opacity") val fillAlpha: String? = null,

        @SerialName("stroke") override val strokeColor: String? = null,
        @SerialName("stroke-width") override val strokeWidth: String? = null,
        @SerialName("stroke-linecap") override val strokeLineCap: String? = null,
        @SerialName("stroke-linejoin") override val strokeLineJoin: String? = null,
        @SerialName("stroke-opacity") override val strokeAlpha: String? = null,
        @SerialName("stroke-miterlimit") override val strokeMiter: String? = null,
    ) : Child

    @Serializable
    @SerialName(CIRCLE)
    @XmlSerialName(CIRCLE)
    data class Circle(
        @SerialName("id") override val id: String? = null,
        @SerialName("cx") val centerX: String,
        @SerialName("cy") val centerY: String,
        @SerialName("r") val radius: String,
        @SerialName("fill") val fill: String? = null,
        @SerialName("fill-rule") val fillRule: String? = null,
        @SerialName("fill-opacity") val fillAlpha: String? = null,

        @SerialName("stroke") override val strokeColor: String? = null,
        @SerialName("stroke-width") override val strokeWidth: String? = null,
        @SerialName("stroke-linecap") override val strokeLineCap: String? = null,
        @SerialName("stroke-linejoin") override val strokeLineJoin: String? = null,
        @SerialName("stroke-opacity") override val strokeAlpha: String? = null,
        @SerialName("stroke-miterlimit") override val strokeMiter: String? = null,
    ) : Child

    @Serializable
    @SerialName(RECTANGLE)
    @XmlSerialName(RECTANGLE)
    data class Rectangle(
        @SerialName("id") override val id: String? = null,
        @SerialName("width") val width: String? = null,
        @SerialName("height") val height: String? = null,
        @SerialName("x") val x: String = "0",
        @SerialName("y") val y: String = "0",
        @SerialName("rx") val horizontalCornerRadius: String? = null,
        @SerialName("ry") val verticalCornerRadius: String? = null,
        @SerialName("fill") val fill: String? = null,

        @SerialName("stroke") override val strokeColor: String? = null,
        @SerialName("stroke-width") override val strokeWidth: String? = null,
        @SerialName("stroke-linecap") override val strokeLineCap: String? = null,
        @SerialName("stroke-linejoin") override val strokeLineJoin: String? = null,
        @SerialName("stroke-opacity") override val strokeAlpha: String? = null,
        @SerialName("stroke-miterlimit") override val strokeMiter: String? = null,
    ) : Child

    @Serializable
    @SerialName(ELLIPSE)
    @XmlSerialName(ELLIPSE)
    data class Ellipse(
        @SerialName("id") override val id: String? = null,
        @SerialName("cx") val centerX: String = "0",
        @SerialName("cy") val centerY: String = "0",
        @SerialName("rx") val radiusX: String? = null,
        @SerialName("ry") val radiusY: String? = null,
        @SerialName("fill") val fill: String? = null,

        @SerialName("stroke") override val strokeColor: String? = null,
        @SerialName("stroke-width") override val strokeWidth: String? = null,
        @SerialName("stroke-linecap") override val strokeLineCap: String? = null,
        @SerialName("stroke-linejoin") override val strokeLineJoin: String? = null,
        @SerialName("stroke-opacity") override val strokeAlpha: String? = null,
        @SerialName("stroke-miterlimit") override val strokeMiter: String? = null,
    ) : Child

    @Serializable
    @SerialName(POLYGON)
    @XmlSerialName(POLYGON)
    data class Polygon(
        @SerialName("id") override val id: String? = null,
        @SerialName("points") val points: String = "",
        @SerialName("fill") val fill: String? = null,
        @SerialName("fill-rule") val fillType: String? = null,
        @SerialName("fill-opacity") val fillAlpha: String? = null,

        @SerialName("stroke") override val strokeColor: String? = null,
        @SerialName("stroke-width") override val strokeWidth: String? = null,
        @SerialName("stroke-linecap") override val strokeLineCap: String? = null,
        @SerialName("stroke-linejoin") override val strokeLineJoin: String? = null,
        @SerialName("stroke-opacity") override val strokeAlpha: String? = null,
        @SerialName("stroke-miterlimit") override val strokeMiter: String? = null,
    ) : Child
}
