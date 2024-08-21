package io.github.composegears.valkyrie.generator.imagevector

import androidx.compose.material.icons.generator.vector.Vector

data class ImageVectorGeneratorConfig(
  val packageName: String,
  val packName: String,
  val nestedPackName: String,
  val outputFormat: OutputFormat,
  val generatePreview: Boolean,
)

enum class OutputFormat(val key: String) {
  BackingProperty(key = "backing_property"),
  LazyProperty(key = "lazy_property"),
  ;

  companion object {
    fun from(key: String?) = OutputFormat.entries.find { it.key == key } ?: BackingProperty
  }
}

data class ImageVectorSpecOutput(
  val content: String,
  val name: String,
) {
  companion object {
    val empty = ImageVectorSpecOutput(
      content = "",
      name = "",
    )
  }
}

object ImageVectorGenerator {

  fun convert(
    vector: Vector,
    kotlinName: String,
    config: ImageVectorGeneratorConfig,
  ): ImageVectorSpecOutput = ImageVectorFileSpec(
    config = ImageVectorSpecConfig(
      iconPackage = config.packageName,
      iconPack = config.packName,
      iconName = kotlinName,
      iconNestedPack = config.nestedPackName,
      outputFormat = config.outputFormat,
      generatePreview = config.generatePreview,
    ),
  ).createFileFor(vector)
}
