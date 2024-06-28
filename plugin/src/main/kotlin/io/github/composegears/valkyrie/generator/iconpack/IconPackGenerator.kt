package io.github.composegears.valkyrie.generator.iconpack

import io.github.composegears.valkyrie.generator.imagevector.ext.fileSpecBuilder
import io.github.composegears.valkyrie.generator.imagevector.ext.objectBuilder
import io.github.composegears.valkyrie.generator.imagevector.ext.removeDeadCode
import io.github.composegears.valkyrie.generator.imagevector.ext.setIndent
import io.github.composegears.valkyrie.processing.writter.FileWriter

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

fun main() {
    val config = IconPackGeneratorConfig(
        packageName = "com.example.icons",
        iconPackName = "IconPack",
        subPacks = listOf("SubPack1", "SubPack2")
    )
    val generator = IconPackGenerator(config)
    val generatedCode = generator.generate()

    println(generatedCode.content)

    val exportDirectory = "/Users/yahor/Work/plugin/Valkyrie/test"

    FileWriter.writeToFile(
        content = generatedCode.content,
        outDirectory = exportDirectory,
        fileName = generatedCode.name
    )
}