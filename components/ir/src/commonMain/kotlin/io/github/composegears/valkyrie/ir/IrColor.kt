package io.github.composegears.valkyrie.ir

import kotlin.jvm.JvmInline

@JvmInline
value class IrColor(val argb: Int) {

    companion object {
        val nameToArgb = mapOf(
            "Black" to IrColor(0xFF000000),
            "DarkGray" to IrColor(0xFF444444),
            "Gray" to IrColor(0xFF888888),
            "LightGray" to IrColor(0xFFCCCCCC),
            "White" to IrColor(0xFFFFFFFF),
            "Red" to IrColor(0xFFFF0000),
            "Green" to IrColor(0xFF00FF00),
            "Blue" to IrColor(0xFF0000FF),
            "Yellow" to IrColor(0xFFFFFF00),
            "Cyan" to IrColor(0xFF00FFFF),
            "Magenta" to IrColor(0xFFFF00FF),
            "Transparent" to IrColor(0x00000000),
        )

        private val argbToName = nameToArgb.entries.associate { (name, value) -> value to name }
    }

    constructor(hex: String) : this(HexParser.toColorInt(hex))
    constructor(long: Long) : this(long.toUInt().toInt())

    val alpha: UByte get() = this[0]
    val red: UByte get() = this[1]
    val green: UByte get() = this[2]
    val blue: UByte get() = this[3]

    fun toHexLiteral(): String = toHex("0x")

    // NOTE: IrColor could be a named color with an alpha value less than 0xff.
    //       If no color is found in the first try, look for the same color with full alpha.
    fun toName(): String? = argbToName[this]
        ?: argbToName[IrColor(0xFF000000 or (argb.toLong() and 0x00FFFFFF))]

    fun toHexColor(): String = toHex("#")

    fun isTransparent(): Boolean = (argb ushr 24) == 0

    private fun toHex(prefix: String): String {
        return "$prefix${argb.toUInt().toString(16).padStart(8, '0').uppercase()}"
    }

    private operator fun get(channel: Int): UByte {
        require(channel < 4)
        return ((argb shr (3 - channel) * 8) and 0xFF).toUByte()
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
