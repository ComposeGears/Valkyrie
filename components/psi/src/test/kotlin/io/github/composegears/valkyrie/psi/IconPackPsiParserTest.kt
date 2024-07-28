package io.github.composegears.valkyrie.psi

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import org.junit.jupiter.api.Test

class IconPackPsiParserTest {

    @Test
    fun `simple icon pack parser`() {
        val path = getResourcePath("SimpleIconPack.kt")

        val iconPackInfo = IconPackPsiParser.extractIconPack(path)

        assertThat(iconPackInfo).isNotNull().transform { packInfo ->
            assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
            assertThat(packInfo.iconPack).isEqualTo("SimpleIconPack")
            assertThat(packInfo.nestedPacks.size).isEqualTo(0)
        }
    }

    @Test
    fun `nested icon pack parser`() {
        val path = getResourcePath("NestedIconPack.kt")

        val iconPackInfo = IconPackPsiParser.extractIconPack(path)

        assertThat(iconPackInfo).isNotNull().transform { packInfo ->
            assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
            assertThat(packInfo.iconPack).isEqualTo("NestedIconPack")
            assertThat(packInfo.nestedPacks.size).isEqualTo(5)
            assertThat(packInfo.nestedPacks).containsExactly("Filled", "Outlined", "TwoTone", "Sharp", "Round")
        }
    }

    @Test
    fun `data object icon pack parser`() {
        val path = getResourcePath("DataObjectIconPack.kt")

        val iconPackInfo = IconPackPsiParser.extractIconPack(path)

        assertThat(iconPackInfo).isNotNull().transform { packInfo ->
            assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
            assertThat(packInfo.iconPack).isEqualTo("DataObjectIconPack")
            assertThat(packInfo.nestedPacks.size).isEqualTo(0)
        }
    }
}
