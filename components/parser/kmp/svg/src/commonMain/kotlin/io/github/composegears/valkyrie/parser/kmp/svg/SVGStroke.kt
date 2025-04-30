package io.github.composegears.valkyrie.parser.kmp.svg

import io.github.composegears.valkyrie.ir.IrColor

internal data class SVGStroke(
    val color: IrColor?,
    val alpha: Float,
    val width: Float,
    val cap: Cap,
    val join: Join,
    val miter: Float,
) {
    enum class Cap {
        Butt,
        Round,
        Square,
        ;

        companion object {
            operator fun invoke(value: String): Cap {
                val cap = when (value.lowercase()) {
                    "butt" -> Butt
                    "round" -> Round
                    "square" -> Square
                    else -> null
                }
                return requireNotNull(cap) {
                    "StrokeCap not supported. Was: $value. Must be in: $entries"
                }
            }
        }
    }

    enum class Join {
        Bevel,
        Miter,
        Round,
        ;

        companion object {
            operator fun invoke(value: String): Join {
                val join = when (value.lowercase()) {
                    "bevel" -> Bevel
                    "miter" -> Miter
                    "round" -> Round
                    else -> null
                }
                return requireNotNull(join) {
                    "StrokeJoin not supported. Was: $value. Must be in: $entries"
                }
            }
        }
    }
}
