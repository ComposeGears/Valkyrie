package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.kmp.common

import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.pathSegments
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig

internal fun ImageVectorGeneratorConfig.resolveReceiverName(): String {
    val segments = iconPackTree?.pathSegments().orEmpty()
    return segments.joinToString(".")
}
