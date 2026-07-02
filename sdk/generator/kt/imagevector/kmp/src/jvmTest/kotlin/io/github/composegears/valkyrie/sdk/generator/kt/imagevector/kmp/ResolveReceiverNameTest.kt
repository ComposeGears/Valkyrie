package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.kmp

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.kmp.common.resolveReceiverName
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.testfixtures.DEFAULT_PACKAGE
import org.junit.jupiter.api.Test

class ResolveReceiverNameTest {

    @Test
    fun `no iconPackTree returns empty`() {
        val config = ImageVectorGeneratorConfig.simple(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
        )
        assertThat(config.resolveReceiverName()).isEqualTo("")
    }

    @Test
    fun `root-only returns root`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons"),
        )
        assertThat(config.resolveReceiverName()).isEqualTo("ValkyrieIcons")
    }

    @Test
    fun `one nested level returns root dot child`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Filled")
            },
        )
        assertThat(config.resolveReceiverName()).isEqualTo("ValkyrieIcons.Filled")
    }

    @Test
    fun `two nested levels returns full path`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Material") {
                    child("Rounded")
                }
            },
        )
        assertThat(config.resolveReceiverName()).isEqualTo("ValkyrieIcons.Material.Rounded")
    }
}
