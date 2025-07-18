package io.github.composegears.valkyrie.generator.iconpack

import com.squareup.kotlinpoet.TypeSpec
import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.generator.jvm.ext.fileSpecBuilder
import io.github.composegears.valkyrie.generator.jvm.ext.objectBuilder
import io.github.composegears.valkyrie.generator.jvm.ext.removeExplicitModeCode
import io.github.composegears.valkyrie.generator.jvm.ext.setIndent

internal class IconPackFileSpec(private val config: IconPackGeneratorConfig) {

    fun createSpec(): IconPackSpecOutput {
        val iconPackName = config.iconPack.name

        val iconPackSpec = objectBuilder(name = iconPackName) {
            config.iconPack.nested.forEach { pack ->
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
        return IconPackSpecOutput(
            content = when {
                config.useExplicitMode -> fileSpec.toString()
                else -> fileSpec.removeExplicitModeCode()
            },
            name = fileSpec.name,
        )
    }

    private fun createNestedObjectSpec(pack: IconPack): TypeSpec {
        return objectBuilder(name = pack.name) {
            pack.nested.forEach { nestedPack ->
                addType(createNestedObjectSpec(nestedPack))
            }
        }
    }
}
