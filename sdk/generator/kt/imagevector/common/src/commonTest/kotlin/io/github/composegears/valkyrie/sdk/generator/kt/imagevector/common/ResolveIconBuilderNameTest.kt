package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.shared.DEFAULT_PACKAGE
import kotlin.test.Test

class ResolveIconBuilderNameTest {

    @Test
    fun `no iconPackTree returns iconName`() {
        val config = ImageVectorGeneratorConfig.simple(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
        )
        assertThat(config.resolveIconBuilderName()).isEqualTo("Add")
    }

    @Test
    fun `root-only returns iconName`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons"),
        )
        assertThat(config.resolveIconBuilderName()).isEqualTo("Add")
    }

    @Test
    fun `one nested level returns child dot iconName`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Filled")
            },
        )
        assertThat(config.resolveIconBuilderName()).isEqualTo("Filled.Add")
    }

    @Test
    fun `two nested levels returns deepest child dot iconName`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Material") {
                    child("Rounded")
                }
            },
        )
        assertThat(config.resolveIconBuilderName()).isEqualTo("Rounded.Add")
    }
}
