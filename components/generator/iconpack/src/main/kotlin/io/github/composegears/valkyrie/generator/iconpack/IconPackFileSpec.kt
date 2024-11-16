package io.github.composegears.valkyrie.generator.iconpack

import io.github.composegears.valkyrie.generator.ext.fileSpecBuilder
import io.github.composegears.valkyrie.generator.ext.objectBuilder
import io.github.composegears.valkyrie.generator.ext.removeExplicitModeCode
import io.github.composegears.valkyrie.generator.ext.setIndent

internal class IconPackFileSpec(private val config: IconPackGeneratorConfig) {

    fun createSpec(): IconPackSpecOutput {
        val iconPackSpec = objectBuilder(name = config.iconPackName) {
            config.nestedPacks.forEach { icon ->
                addType(objectBuilder(name = icon))
            }
        }
        val fileSpec = fileSpecBuilder(
            packageName = config.packageName,
            fileName = config.iconPackName,
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
}
