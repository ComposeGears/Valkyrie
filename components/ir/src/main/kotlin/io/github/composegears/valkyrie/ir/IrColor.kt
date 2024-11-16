package io.github.composegears.valkyrie.ir

@JvmInline
value class IrColor(val argb: Int) {

    constructor(hex: String) : this(HexParser.toColorInt(hex))

    fun toHexLiteral(): String = toHex("0x")

    fun toHexColor(): String = toHex("#")

    fun isTransparent(): Boolean = (argb ushr 24) == 0

    private fun toHex(prefix: String): String {
        return "$prefix${argb.toUInt().toString(16).padStart(8, '0').uppercase()}"
    }
}

private object HexParser {

    private const val DEFAULT_COLOR = "FF000000"
    private val hexRegex = "^[0-9a-fA-F]{3,8}".toRegex()

    fun toColorInt(value: String): Int {
        val hex = value.removePrefix("#").removePrefix("0x")

        val normalizedHex = if (hexRegex.matches(hex)) {
            when (hex.length) {
                3 -> "FF${expandShorthandHex(hex)}"
                6 -> "FF$hex"
                8 -> hex
                else -> DEFAULT_COLOR
            }
        } else {
            DEFAULT_COLOR
        }

        return normalizedHex.toUInt(16).toInt()
    }

    private fun expandShorthandHex(hex: String): String {
        require(hex.length == 3) { "Expected a 3-character hex string" }
        return hex.map { "$it$it" }.joinToString("")
    }
}