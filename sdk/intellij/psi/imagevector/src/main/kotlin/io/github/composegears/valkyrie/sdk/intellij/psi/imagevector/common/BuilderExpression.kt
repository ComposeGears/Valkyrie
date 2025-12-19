package io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.common

import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.util.childrenOfType
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtStringTemplateExpression

@JvmInline
internal value class BuilderExpression(val callExpression: KtCallExpression)

internal fun KtBlockExpression.materialIconCall(): BuilderExpression? {
    val ktCallExpression = childrenOfType<KtCallExpression>().firstOrNull {
        it.calleeExpression?.text == "materialIcon"
    } ?: return null

    return BuilderExpression(ktCallExpression)
}

internal fun KtBlockExpression.builderExpression(): BuilderExpression? {
    val ktCallExpression = childrenOfType<KtCallExpression>().firstOrNull {
        it.calleeExpression?.text == "Builder"
    } ?: return null

    return BuilderExpression(ktCallExpression)
}

internal fun BuilderExpression.name(): String {
    val nameArgument = callExpression.valueArguments.find { arg ->
        arg?.getArgumentName()?.asName?.identifier == "name"
    }

    return nameArgument?.getArgumentExpression().safeAs<KtStringTemplateExpression>()
        ?.entries
        ?.firstOrNull()
        ?.text.orEmpty()
}

internal fun BuilderExpression.defaultWidth(defaultValue: Float): Float {
    return extractFloat("defaultWidth", defaultValue)
}

internal fun BuilderExpression.defaultHeight(defaultValue: Float): Float {
    return extractFloat("defaultHeight", defaultValue)
}

internal fun BuilderExpression.viewportWidth(defaultValue: Float): Float {
    return extractFloat("viewportWidth", defaultValue)
}

internal fun BuilderExpression.viewportHeight(defaultValue: Float): Float {
    return extractFloat("viewportHeight", defaultValue)
}

internal fun BuilderExpression.autoMirror(defaultValue: Boolean = false): Boolean {
    return extractBoolean("autoMirror", defaultValue)
}

private fun BuilderExpression.extractFloat(paramName: String, defaultValue: Float): Float {
    val argument = callExpression.valueArguments.find { arg ->
        arg?.getArgumentName()?.asName?.identifier == paramName
    }

    val valueText = argument?.getArgumentExpression()?.text ?: return defaultValue

    return valueText
        .removeSuffix(".dp")
        .removeSuffix("f")
        .removeSuffix("F")
        .toFloatOrNull() ?: defaultValue
}

private fun BuilderExpression.extractBoolean(paramName: String, defaultValue: Boolean): Boolean {
    val argument = callExpression.valueArguments.find { arg ->
        arg?.getArgumentName()?.asName?.identifier == paramName
    }

    val valueText = argument?.getArgumentExpression()?.text ?: return defaultValue
    return valueText.toBoolean()
}
