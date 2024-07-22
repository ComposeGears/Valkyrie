package io.github.composegears.valkyrie.generator.iconpack

import io.github.composegears.valkyrie.generator.ext.fileSpecBuilder
import io.github.composegears.valkyrie.generator.ext.objectBuilder
import io.github.composegears.valkyrie.generator.ext.removeDeadCode
import io.github.composegears.valkyrie.generator.ext.setIndent

internal class IconPackFileSpec(private val config: IconPackGeneratorConfig) {

    fun createSpec(): IconPackSpecOutput {
        val iconPackSpec = objectBuilder(name = config.iconPackName) {
            config.subPacks.forEach { icon ->
                addType(objectBuilder(name = icon))
            }
        }
        val fileSpec = fileSpecBuilder(
            packageName = config.packageName,
            fileName = config.iconPackName,
        ) {
            addType(iconPackSpec)
            setIndent()
        }
        return IconPackSpecOutput(
            content = fileSpec.removeDeadCode(),
            name = fileSpec.name,
        )
    }
}
