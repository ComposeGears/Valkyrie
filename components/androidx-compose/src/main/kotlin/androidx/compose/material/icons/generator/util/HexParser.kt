package androidx.compose.material.icons.generator.util

private val hexRegex = "^[0-9a-fA-F]{3,8}".toRegex()

internal const val DEFAULT_COLOR = "FF000000"

internal fun String.toHexColor(): String {
    val hex = removePrefix("#")
    return if (hexRegex.matches(hex)) {
        when (hex.length) {
            3 -> "FF${expandShorthandHex(hex)}"
            6 -> "FF$hex"
            8 -> hex
            else -> DEFAULT_COLOR
        }
    } else {
        DEFAULT_COLOR
    }
}

private fun expandShorthandHex(hex: String): String {
    require(hex.length == 3) { "Expected a 3-character hex string" }
    return hex.map { "$it$it" }.joinToString("")
}
