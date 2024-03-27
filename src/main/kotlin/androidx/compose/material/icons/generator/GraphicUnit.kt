package androidx.compose.material.icons.generator

import com.squareup.kotlinpoet.MemberName

sealed class GraphicUnit {
    abstract val value: Float
    abstract val memberName: MemberName?
}

class Pixel(override val value: Float) : GraphicUnit() {
    override val memberName: MemberName? = null
}

class Dp(override val value: Float) : GraphicUnit() {
    override val memberName: MemberName? = MemberNames.Dp
}

fun rawAsGraphicUnit(raw: String): GraphicUnit {
    val isStrokeDp = raw.endsWith("dp")
    return when {
        isStrokeDp -> Dp(raw.removeSuffix("dp").toFloat())
        else -> Pixel(raw.toFloat())
    }
}