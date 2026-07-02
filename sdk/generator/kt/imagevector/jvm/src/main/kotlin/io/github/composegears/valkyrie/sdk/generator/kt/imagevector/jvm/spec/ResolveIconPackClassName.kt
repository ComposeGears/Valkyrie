package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.spec

import com.squareup.kotlinpoet.ClassName
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.pathSegments
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig

internal fun ImageVectorGeneratorConfig.resolveIconPackClassName(): ClassName? {
    val tree = iconPackTree ?: return null
    val segments = tree.pathSegments()
    return segments.drop(1).fold(ClassName(iconPackPackage, segments.first())) { cls, name ->
        cls.nestedClass(name)
    }
}
