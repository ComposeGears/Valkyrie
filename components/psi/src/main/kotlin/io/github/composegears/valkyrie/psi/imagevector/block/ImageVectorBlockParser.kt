package io.github.composegears.valkyrie.psi.imagevector.block

import io.github.composegears.valkyrie.psi.extension.childrenOfType
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtCallExpression

data class KtImageVector(
    val name: String = "",
    val defaultWidth: Float = 0f,
    val defaultHeight: Float = 0f,
    val viewportWidth: Float = 0f,
    val viewportHeight: Float = 0f,
)

fun KtBlockExpression.parseImageVectorParams(): KtImageVector? {
    val imageVectorBuilderCall = childrenOfType<KtCallExpression>().firstOrNull {
        it.calleeExpression?.text == "Builder"
    } ?: return null

    var name = ""
    var defaultWidth = 0f
    var defaultHeight = 0f
    var viewportWidth = 0f
    var viewportHeight = 0f

    imageVectorBuilderCall.valueArguments
        .forEach { arg ->
            val argName = arg.getArgumentName()?.asName?.identifier
            val argValue = arg.getArgumentExpression()?.text

            when (argName) {
                "name" -> name = argValue?.removeSurrounding("\"").orEmpty()
                "defaultWidth" -> defaultWidth = parseValue(argValue)
                "defaultHeight" -> defaultHeight = parseValue(argValue)
                "viewportWidth" -> viewportWidth = parseValue(argValue)
                "viewportHeight" -> viewportHeight = parseValue(argValue)
            }
        }

    return KtImageVector(
        name = name,
        defaultWidth = defaultWidth,
        defaultHeight = defaultHeight,
        viewportWidth = viewportWidth,
        viewportHeight = viewportHeight,
    )
}

private fun parseValue(value: String?): Float {
    if (value == null) return 0f

    return value
        .replace("dp", "")
        .replace("f", "")
        .toFloatOrNull() ?: 0f
}
