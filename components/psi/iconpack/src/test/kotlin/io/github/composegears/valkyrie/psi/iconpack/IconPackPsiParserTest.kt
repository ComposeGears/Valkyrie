package io.github.composegears.valkyrie.psi.iconpack

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.intellij.openapi.application.invokeAndWaitIfNeeded
import com.intellij.openapi.project.Project
import com.intellij.testFramework.ProjectExtension
import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@Suppress("UnstableApiUsage")
class IconPackPsiParserTest {

    companion object {
        @RegisterExtension
        val projectExtension = ProjectExtension()
    }

    private val project: Project
        get() = projectExtension.project

    @Test
    fun `simple icon pack parser`() {
        val path = getResourcePath("SimpleIconPack.kt")

        invokeAndWaitIfNeeded {
            val iconPackInfo = IconPackPsiParser.extractIconPack(path, project)

            assertThat(iconPackInfo).isNotNull().transform { packInfo ->
                assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
                assertThat(packInfo.iconPack).isEqualTo("SimpleIconPack")
                assertThat(packInfo.nestedPacks.size).isEqualTo(0)
            }
        }
    }

    @Test
    fun `nested icon pack parser`() {
        val path = getResourcePath("NestedIconPack.kt")

        invokeAndWaitIfNeeded {
            val iconPackInfo = IconPackPsiParser.extractIconPack(path, project)

            assertThat(iconPackInfo).isNotNull().transform { packInfo ->
                assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
                assertThat(packInfo.iconPack).isEqualTo("NestedIconPack")
                assertThat(packInfo.nestedPacks.size).isEqualTo(5)
                assertThat(packInfo.nestedPacks).containsExactly("Filled", "Outlined", "TwoTone", "Sharp", "Round")
            }
        }
    }

    @Test
    fun `data object icon pack parser`() {
        val path = getResourcePath("DataObjectIconPack.kt")

        invokeAndWaitIfNeeded {
            val iconPackInfo = IconPackPsiParser.extractIconPack(path, project)

            assertThat(iconPackInfo).isNotNull().transform { packInfo ->
                assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
                assertThat(packInfo.iconPack).isEqualTo("DataObjectIconPack")
                assertThat(packInfo.nestedPacks.size).isEqualTo(0)
            }
        }
    }
}
