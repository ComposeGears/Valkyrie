package io.github.composegears.valkyrie.generator.iconpack

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class IconPackGeneratorTest {

  @Test
  fun `generate icon pack`() {
    val result = IconPackGenerator.create(
      config = IconPackGeneratorConfig(
        packageName = "io.github.composegears.valkyrie.icons",
        iconPackName = "ValkyrieIcons",
        subPacks = emptyList(),
      ),
    )

    val expectedContent = """
            package io.github.composegears.valkyrie.icons

            object ValkyrieIcons

    """.trimIndent()

    assertThat(result.content).isEqualTo(expectedContent)
    assertThat(result.name).isEqualTo("ValkyrieIcons")
  }

  @Test
  fun `generate nested packs`() {
    val result = IconPackGenerator.create(
      config = IconPackGeneratorConfig(
        packageName = "io.github.composegears.valkyrie.icons",
        iconPackName = "ValkyrieIcons",
        subPacks = listOf("Filled", "Colored"),
      ),
    )

    val expectedContent = """
            package io.github.composegears.valkyrie.icons

            object ValkyrieIcons {
                object Filled

                object Colored
            }

    """.trimIndent()

    assertThat(result.content).isEqualTo(expectedContent)
    assertThat(result.name).isEqualTo("ValkyrieIcons")
  }
}
