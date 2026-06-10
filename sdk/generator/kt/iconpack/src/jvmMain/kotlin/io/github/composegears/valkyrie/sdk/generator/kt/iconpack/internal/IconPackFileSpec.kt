package io.github.composegears.valkyrie.sdk.generator.kt.iconpack.internal

import com.squareup.kotlinpoet.TypeSpec
import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.IconPackSpecOutput
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.asBlockComment
import io.github.composegears.valkyrie.sdk.generator.kt.poet.fileSpecBuilder
import io.github.composegears.valkyrie.sdk.generator.kt.poet.objectBuilder
import io.github.composegears.valkyrie.sdk.generator.kt.poet.removeExplicitModeCode
import io.github.composegears.valkyrie.sdk.generator.kt.poet.setIndent

internal class IconPackFileSpec(private val config: IconPackGeneratorConfig) {

    fun createSpec(): IconPackSpecOutput {
        val iconPackName = config.iconPack.data

        val iconPackSpec = objectBuilder(name = iconPackName) {
            config.iconPack.children.forEach { pack ->
                addType(createNestedObjectSpec(pack))
            }
        }
        val fileSpec = fileSpecBuilder(
            packageName = config.packageName,
            fileName = iconPackName,
        ) {
            addType(iconPackSpec)
            setIndent(config.indentSize)
        }
        val generatedContent = when {
            config.useExplicitMode -> fileSpec.toString()
            else -> fileSpec.removeExplicitModeCode()
        }
        val content = if (config.license != null) {
            "${config.license.asBlockComment()}\n\n$generatedContent"
        } else {
            generatedContent
        }
        return IconPackSpecOutput(
            content = content,
            name = fileSpec.name,
        )
    }

    private fun createNestedObjectSpec(pack: IconPack): TypeSpec {
        return objectBuilder(name = pack.data) {
            pack.children.forEach { nestedPack ->
                addType(createNestedObjectSpec(nestedPack))
            }
        }
    }
}
