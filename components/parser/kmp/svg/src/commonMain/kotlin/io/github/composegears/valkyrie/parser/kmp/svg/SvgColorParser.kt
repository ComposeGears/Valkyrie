package io.github.composegears.valkyrie.parser.kmp.svg

import io.github.composegears.valkyrie.ir.IrColor

object SvgColorParser {
    fun parse(colorValue: String): IrColor? {
        if (colorValue == "none") return null
        return KeywordColorParser.parse(colorValue) ?: IrColor(colorValue)
    }
}
