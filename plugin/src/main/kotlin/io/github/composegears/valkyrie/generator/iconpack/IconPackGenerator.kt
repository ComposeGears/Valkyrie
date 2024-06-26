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

class IconPackGenerator(private val config: IconPackGeneratorConfig) {

    fun generate(): String {
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

        return fileSpec.removeDeadCode()
    }
}

fun main() {
    val config = IconPackGeneratorConfig(
        packageName = "com.example.icons",
        iconPackName = "IconPack",
        subPacks = listOf("SubPack1", "SubPack2")
    )

    val generator = IconPackGenerator(config)
    val generatedCode = generator.generate()

    println(generatedCode)
}