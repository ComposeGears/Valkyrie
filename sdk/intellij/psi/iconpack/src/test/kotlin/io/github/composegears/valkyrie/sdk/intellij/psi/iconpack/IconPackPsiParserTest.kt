package io.github.composegears.valkyrie.sdk.intellij.psi.iconpack

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.intellij.testFramework.runInEdtAndGet
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.IconPackTree
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.encode
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.iconPackOf
import io.github.composegears.valkyrie.sdk.generator.kt.iconpack.tree.navigate
import io.github.composegears.valkyrie.sdk.intellij.testfixtures.KotlinCodeInsightTest
import org.junit.jupiter.api.Test

class IconPackPsiParserTest : KotlinCodeInsightTest() {

    @Test
    fun `simple icon pack parser`() {
        runInEdtAndGet {
            val ktFile = loadKtFile("SimpleIconPack.kt")
            val iconPackInfo = IconPackPsiParser.parse(ktFile)

            assertThat(iconPackInfo).isNotNull().transform { packInfo ->
                assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
                assertThat(packInfo.iconPackTree.data).isEqualTo("SimpleIconPack")
                assertThat(packInfo.iconPackTree.children.size).isEqualTo(0)
            }
        }
    }

    @Test
    fun `nested icon pack parser`() {
        runInEdtAndGet {
            val ktFile = loadKtFile("NestedIconPack.kt")
            val iconPackInfo = IconPackPsiParser.parse(ktFile)

            assertThat(iconPackInfo).isNotNull().transform { packInfo ->
                assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
                assertThat(packInfo.iconPackTree.data).isEqualTo("NestedIconPack")
                assertThat(packInfo.iconPackTree.children.size).isEqualTo(5)
                assertThat(packInfo.iconPackTree.children.map { it.data }).containsExactly(
                    "Filled",
                    "Outlined",
                    "TwoTone",
                    "Sharp",
                    "Round",
                )
            }
        }
    }

    @Test
    fun `data object icon pack parser`() {
        runInEdtAndGet {
            val ktFile = loadKtFile("DataObjectIconPack.kt")
            val iconPackInfo = IconPackPsiParser.parse(ktFile)

            assertThat(iconPackInfo).isNotNull().transform { packInfo ->
                assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
                assertThat(packInfo.iconPackTree.data).isEqualTo("DataObjectIconPack")
                assertThat(packInfo.iconPackTree.children.size).isEqualTo(0)
            }
        }
    }

    @Test
    fun `deep nested icon pack parser`() {
        runInEdtAndGet {
            val ktFile = loadKtFile("DeepNestedIconPack.kt")
            val iconPackInfo = IconPackPsiParser.parse(ktFile)

            assertThat(iconPackInfo).isNotNull().transform { packInfo ->
                val tree = packInfo.iconPackTree

                assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
                tree.assertStructure("DeepNestedIconPack", listOf("Level1", "Branch", "Wide", "Single"))

                // Verify deep linear chain: Level1 -> Level2 -> Level3 -> Level4 -> Level5
                tree.navigate("Level1").assertStructure("Level1", 1)
                tree.navigate("Level1.Level2").assertStructure("Level2", 1)
                tree.navigate("Level1.Level2.Level3").assertStructure("Level3", 1)
                tree.navigate("Level1.Level2.Level3.Level4").assertStructure("Level4", 1)
                tree.navigate("Level1.Level2.Level3.Level4.Level5").assertStructure("Level5", 0)

                // Verify Branch with multiple sub-branches
                tree.navigate("Branch").assertStructure("Branch", listOf("Left", "Middle", "Right"))
                tree.navigate("Branch.Left").assertStructure("Left", 1)
                tree.navigate("Branch.Left.LeftDeep1").assertStructure("LeftDeep1", 1)
                tree.navigate("Branch.Left.LeftDeep1.LeftDeep2").assertStructure("LeftDeep2", 0)
                tree.navigate("Branch.Middle").assertStructure("Middle", 0)
                tree.navigate("Branch.Right").assertStructure("Right", listOf("RightDeep1", "RightDeep2"))

                // Verify wide tree with multiple children
                tree.navigate("Wide").assertStructure("Wide", listOf("Item1", "Item2", "Item3", "Item4", "Item5"))

                // Verify single leaf node
                tree.navigate("Single").assertStructure("Single", 0)

                // Verify toRawString produces correct paths
                val rawString = tree.encode()
                val expectedPaths = listOf(
                    "DeepNestedIconPack.Level1.Level2.Level3.Level4.Level5",
                    "DeepNestedIconPack.Branch.Left.LeftDeep1.LeftDeep2",
                    "DeepNestedIconPack.Branch.Middle",
                    "DeepNestedIconPack.Branch.Right.RightDeep1",
                    "DeepNestedIconPack.Branch.Right.RightDeep2",
                    "DeepNestedIconPack.Wide.Item1",
                    "DeepNestedIconPack.Wide.Item2",
                    "DeepNestedIconPack.Wide.Item3",
                    "DeepNestedIconPack.Wide.Item4",
                    "DeepNestedIconPack.Wide.Item5",
                    "DeepNestedIconPack.Single",
                )
                assertThat(rawString).isEqualTo(expectedPaths.joinToString(","))

                // Verify round-trip conversion (toRawString -> fromString)
                val reconstructed = iconPackOf(rawString)
                assertThat(reconstructed).isEqualTo(tree)
            }
        }
    }

    @Test
    fun `icon pack with license`() {
        runInEdtAndGet {
            val ktFile = loadKtFile("IconPackWithLicense.kt")
            val iconPackInfo = IconPackPsiParser.parse(ktFile)

            val expectedLicense = """
                /*
                 * This program is free software: you can redistribute it and/or modify
                 * it under the terms of the GNU General Public License as published by
                 * the Free Software Foundation, either version 3 of the License, or
                 * (at your option) any later version.
                 *
                 * This program is distributed in the hope that it will be useful,
                 * but WITHOUT ANY WARRANTY; without even the implied warranty of
                 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
                 * GNU General Public License for more details.
                 *
                 * You should have received a copy of the GNU General Public License
                 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
                 */
            """.trimIndent()

            assertThat(iconPackInfo).isNotNull().transform { packInfo ->
                assertThat(packInfo.packageName).isEqualTo("com.test")
                assertThat(packInfo.iconPackTree.data).isEqualTo("Symbols")
                assertThat(packInfo.iconPackTree.children.size).isEqualTo(0)
                assertThat(packInfo.license).isEqualTo(expectedLicense)
            }
        }
    }

    private fun IconPackTree.assertStructure(expectedName: String, expectedNestedCount: Int) {
        assertThat(data).isEqualTo(expectedName)
        assertThat(children.size).isEqualTo(expectedNestedCount)
    }

    private fun IconPackTree.assertStructure(expectedName: String, expectedNestedNames: List<String>) {
        assertThat(data).isEqualTo(expectedName)
        assertThat(children.map { it.data }).containsExactly(*expectedNestedNames.toTypedArray())
    }
}
