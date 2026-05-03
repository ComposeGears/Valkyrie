package io.github.composegears.valkyrie.ui.screen.webimport.common.domain.font

@JvmInline
value class Codepoint(val value: Int) {
    init {
        require(value in 0..Character.MAX_CODE_POINT) { "Invalid Unicode codepoint: $value" }
    }

    fun toGlyphString(): String = String(Character.toChars(value))
}
