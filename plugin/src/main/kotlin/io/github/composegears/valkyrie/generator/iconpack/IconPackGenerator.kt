package io.github.composegears.valkyrie.generator.iconpack

import io.github.composegears.valkyrie.generator.imagevector.ext.fileSpecBuilder
import io.github.composegears.valkyrie.generator.imagevector.ext.objectBuilder
import io.github.composegears.valkyrie.generator.imagevector.ext.removeDeadCode
import io.github.composegears.valkyrie.generator.imagevector.ext.setIndent

data class IconPackGeneratorConfig(
    val packageName: String,
    val iconPackName: String,
    val subPacks: List<String>
)

data class IconPackGeneratorResult(
    val content: String,
    val name: String
)

class IconPackGenerator(private val config: IconPackGeneratorConfig) {

    fun generate(): IconPackGeneratorResult {
        val iconPackSpec = objectBuilder(name = config.iconPackName) {
            config.subPacks.forEach { icon ->
                addType(objectBuilder(name = icon))
            }
        }
        val fileSpec = fileSpecBuilder(
            packageName = config.packageName,
            fileName = config.iconPackName
        ) {
            addType(iconPackSpec)
            setIndent()
        }
        return IconPackGeneratorResult(
            content = fileSpec.removeDeadCode(),
            name = fileSpec.name
        )
    }
}