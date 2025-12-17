package io.github.composegears.valkyrie.sdk.intellij.psi.iconpack

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.sdk.intellij.testfixtures.KotlinLightTestCase
import org.junit.Test

class IconPackPsiParserTest : KotlinLightTestCase() {

    @Test
    fun `simple icon pack parser`() {
        val ktFile = loadKtFile("SimpleIconPack.kt")
        val iconPackInfo = IconPackPsiParser.parse(ktFile)

        assertThat(iconPackInfo).isNotNull().transform { packInfo ->
            assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
            assertThat(packInfo.iconPack.name).isEqualTo("SimpleIconPack")
            assertThat(packInfo.iconPack.nested.size).isEqualTo(0)
        }
    }

    @Test
    fun `nested icon pack parser`() {
        val ktFile = loadKtFile("NestedIconPack.kt")
        val iconPackInfo = IconPackPsiParser.parse(ktFile)

        assertThat(iconPackInfo).isNotNull().transform { packInfo ->
            assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
            assertThat(packInfo.iconPack.name).isEqualTo("NestedIconPack")
            assertThat(packInfo.iconPack.nested.size).isEqualTo(5)
            assertThat(packInfo.iconPack.nested.map { it.name }).containsExactly(
                "Filled",
                "Outlined",
                "TwoTone",
                "Sharp",
                "Round",
            )
        }
    }

    @Test
    fun `data object icon pack parser`() {
        val ktFile = loadKtFile("DataObjectIconPack.kt")
        val iconPackInfo = IconPackPsiParser.parse(ktFile)

        assertThat(iconPackInfo).isNotNull().transform { packInfo ->
            assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
            assertThat(packInfo.iconPack.name).isEqualTo("DataObjectIconPack")
            assertThat(packInfo.iconPack.nested.size).isEqualTo(0)
        }
    }

    @Test
    fun `deep nested icon pack parser`() {
        val ktFile = loadKtFile("DeepNestedIconPack.kt")
        val iconPackInfo = IconPackPsiParser.parse(ktFile)

        assertThat(iconPackInfo).isNotNull().transform { packInfo ->
            assertThat(packInfo.packageName).isEqualTo("io.github.composegears.valkyrie.psi")
            packInfo.iconPack.assertStructure(
                expectedName = "DeepNestedIconPack",
                expectedNestedCount = 4,
                expectedNestedNames = listOf("Level1", "Branch", "Wide", "Single"),
            )

            // Verify deep linear chain: Level1 -> Level2 -> Level3 -> Level4 -> Level5
            packInfo.iconPack.navigate("Level1").assertStructure("Level1", 1)
            packInfo.iconPack.navigate("Level1.Level2").assertStructure("Level2", 1)
            packInfo.iconPack.navigate("Level1.Level2.Level3").assertStructure("Level3", 1)
            packInfo.iconPack.navigate("Level1.Level2.Level3.Level4").assertStructure("Level4", 1)
            packInfo.iconPack.navigate("Level1.Level2.Level3.Level4.Level5").assertStructure("Level5", 0)

            // Verify Branch with multiple sub-branches
            packInfo.iconPack.navigate("Branch").assertStructure(
                expectedName = "Branch",
                expectedNestedCount = 3,
                expectedNestedNames = listOf("Left", "Middle", "Right"),
            )
            packInfo.iconPack.navigate("Branch.Left").assertStructure("Left", 1)
            packInfo.iconPack.navigate("Branch.Left.LeftDeep1").assertStructure("LeftDeep1", 1)
            packInfo.iconPack.navigate("Branch.Left.LeftDeep1.LeftDeep2").assertStructure("LeftDeep2", 0)
            packInfo.iconPack.navigate("Branch.Middle").assertStructure("Middle", 0)
            packInfo.iconPack.navigate("Branch.Right")
                .assertStructure(
                    expectedName = "Right",
                    expectedNestedCount = 2,
                    expectedNestedNames = listOf("RightDeep1", "RightDeep2"),
                )

            // Verify wide tree with multiple children
            packInfo.iconPack.navigate("Wide")
                .assertStructure(
                    expectedName = "Wide",
                    expectedNestedCount = 5,
                    expectedNestedNames = listOf("Item1", "Item2", "Item3", "Item4", "Item5"),
                )

            // Verify single leaf node
            packInfo.iconPack.navigate("Single").assertStructure("Single", 0)

            // Verify toRawString produces correct paths
            val rawString = IconPack.toRawString(packInfo.iconPack)
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
            val reconstructed = IconPack.fromString(rawString)
            assertThat(reconstructed).isEqualTo(packInfo.iconPack)
        }
    }

    private fun IconPack.navigate(path: String): IconPack {
        val parts = path.split('.')
        var current = this

        parts.forEach { part ->
            current = current.nested.first { it.name == part }
        }

        return current
    }

    private fun IconPack.assertStructure(
        expectedName: String,
        expectedNestedCount: Int,
        expectedNestedNames: List<String> = emptyList(),
    ) {
        assertThat(name).isEqualTo(expectedName)
        assertThat(nested.size).isEqualTo(expectedNestedCount)
        if (expectedNestedNames.isNotEmpty()) {
            assertThat(nested.map { it.name }).containsExactly(*expectedNestedNames.toTypedArray())
        }
    }
}
