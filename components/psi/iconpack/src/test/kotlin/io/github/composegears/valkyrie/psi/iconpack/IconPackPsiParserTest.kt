package io.github.composegears.valkyrie.psi.iconpack

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.intellij.openapi.project.Project
import com.intellij.testFramework.ProjectExtension
import com.intellij.testFramework.runInEdtAndGet
import io.github.composegears.valkyrie.resource.loader.ResourceLoader.getResourcePath
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class IconPackPsiParserTest {

    companion object {
        @RegisterExtension
        val projectExtension = ProjectExtension()
    }

    private val project: Project
        get() = projectExtension.project

    @Test
    fun `icon pack parser L1`() {
        val path = getResourcePath("IconPack_L1.kt")

        runInEdtAndGet {
            val iconPackInfo = IconPackPsiParser.extractIconPack(path, project)

            assertThat(iconPackInfo).isNotNull().transform { packInfo ->
                assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
                assertThat(packInfo.iconPack).isEqualTo("IconPack_L1")
                assertThat(packInfo.nestedPacks.size).isEqualTo(0)
            }
        }
    }

    @Test
    fun `icon pack parser L2`() {
        val path = getResourcePath("IconPack_L2.kt")

        runInEdtAndGet {
            val iconPackInfo = IconPackPsiParser.extractIconPack(path, project)

            assertThat(iconPackInfo).isNotNull().transform { packInfo ->
                assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
                assertThat(packInfo.iconPack).isEqualTo("IconPack_L2")
                assertThat(packInfo.nestedPacks.size).isEqualTo(5)
                assertThat(packInfo.nestedPacks).containsExactly("Filled", "Outlined", "TwoTone", "Sharp", "Round")
            }
        }
    }

    @Test
    fun `icon pack parser L3`() {
        val path = getResourcePath("IconPack_L3.kt")

        invokeAndWaitIfNeeded {
            val iconPackInfo = IconPackPsiParser.extractIconPack(path, project)

            assertThat(iconPackInfo).isNotNull().transform { packInfo ->
                assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
                assertThat(packInfo.iconPack).isEqualTo("IconPack_L3")
                assertThat(packInfo.nestedPacks.size).isEqualTo(5)
                assertThat(packInfo.nestedPacks).containsExactly("Filled", "Outlined", "TwoTone", "Sharp", "Round")
            }
        }
    }

    @Test
    fun `data object icon pack parser`() {
        val path = getResourcePath("DataObjectIconPack.kt")

        runInEdtAndGet {
            val iconPackInfo = IconPackPsiParser.extractIconPack(path, project)

            assertThat(iconPackInfo).isNotNull().transform { packInfo ->
                assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
                assertThat(packInfo.iconPack).isEqualTo("DataObjectIconPack")
                assertThat(packInfo.nestedPacks.size).isEqualTo(0)
            }
        }
    }
}
