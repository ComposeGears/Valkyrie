package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common

import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.shared.DEFAULT_PACKAGE
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ImageVectorGeneratorConfigTest {

    @Test
    fun `branching iconPackTree is rejected`() {
        val error = assertFailsWith<IllegalArgumentException> {
            ImageVectorGeneratorConfig.iconPack(
                iconName = "Add",
                packageName = DEFAULT_PACKAGE,
                iconPackTree = buildTree("ValkyrieIcons") {
                    child("Filled")
                    child("Outlined")
                },
            )
        }

        assertEquals(
            "iconPackTree must be a linear chain (each node has at most one child), " +
                "but node 'ValkyrieIcons' has 2 children",
            error.message,
        )
    }

    @Test
    fun `deep branching is rejected at the correct node`() {
        val error = assertFailsWith<IllegalArgumentException> {
            ImageVectorGeneratorConfig.iconPack(
                iconName = "Add",
                packageName = DEFAULT_PACKAGE,
                iconPackTree = buildTree("ValkyrieIcons") {
                    child("Material") {
                        child("Filled")
                        child("Outlined")
                    }
                },
            )
        }

        assertEquals(
            "iconPackTree must be a linear chain (each node has at most one child), " +
                "but node 'Material' has 2 children",
            error.message,
        )
    }

    @Test
    fun `linear iconPackTree is accepted`() {
        ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Filled")
            },
        )
    }

    @Test
    fun `5-level linear chain is accepted`() {
        ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Level1") {
                    child("Level2") {
                        child("Level3") {
                            child("Level4") {
                                child("Level5")
                            }
                        }
                    }
                }
            },
        )
    }
}
