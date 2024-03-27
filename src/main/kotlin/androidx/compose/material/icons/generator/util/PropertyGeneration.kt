package androidx.compose.material.icons.generator.util

import androidx.compose.material.icons.generator.ClassNames
import com.squareup.kotlinpoet.*

/**
 * @return The private backing property that is used to cache the VectorAsset for a given
 * icon once created.
 *
 * @param name the name of this property
 */
internal fun backingPropertySpec(name: String, type: TypeName): PropertySpec {
    val nullableVectorAsset = type.copy(nullable = true)
    return PropertySpec.builder(name = name, type = nullableVectorAsset)
        .mutable()
        .addModifiers(KModifier.PRIVATE)
        .initializer("null")
        .build()
}

internal inline fun FunSpec.Builder.withBackingProperty(
    backingProperty: PropertySpec,
    block: FunSpec.Builder.() -> Unit
): FunSpec.Builder = apply {
    addCode(buildCodeBlock {
        beginControlFlow("if (%N != null)", backingProperty)
        addStatement("return %N!!", backingProperty)
        endControlFlow()
    })
        .apply(block)
        .addStatement("return %N!!", backingProperty)
}