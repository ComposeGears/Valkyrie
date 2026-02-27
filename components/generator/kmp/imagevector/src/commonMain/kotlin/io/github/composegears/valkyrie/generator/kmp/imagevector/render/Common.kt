package io.github.composegears.valkyrie.generator.kmp.imagevector.render

import io.github.composegears.valkyrie.generator.kmp.imagevector.ImageVectorRenderConfig

internal fun ImageVectorRenderConfig.resolvePackageName(): String = when {
    iconNestedPack.isEmpty() -> iconPackage
    useFlatPackage -> iconPackage
    else -> "$iconPackage.${iconNestedPack.lowercase()}"
}

internal fun ImageVectorRenderConfig.resolveReceiverName(): String = when {
    iconPack.isEmpty() -> ""
    iconNestedPack.isEmpty() -> iconPack
    else -> "$iconPack.$iconNestedPack"
}

internal fun ImageVectorRenderConfig.resolveIconBuilderName(): String = when {
    iconNestedPack.isEmpty() -> iconName
    else -> "$iconNestedPack.$iconName"
}
