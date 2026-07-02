package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.squareup.kotlinpoet.ClassName
import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.spec.resolveIconPackClassName
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.testfixtures.DEFAULT_PACKAGE
import org.junit.jupiter.api.Test

class ResolveIconPackClassNameTest {

    @Test
    fun `resolveIconPackClassName - no iconPackTree returns null`() {
        val config = ImageVectorGeneratorConfig.simple(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
        )
        assertThat(config.resolveIconPackClassName()).isNull()
    }

    @Test
    fun `resolveIconPackClassName - root-only tree returns root ClassName`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons"),
        )
        assertThat(config.resolveIconPackClassName())
            .isEqualTo(ClassName(DEFAULT_PACKAGE, "ValkyrieIcons"))
    }

    @Test
    fun `resolveIconPackClassName - one nested level returns nested ClassName`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Filled")
            },
        )
        assertThat(config.resolveIconPackClassName())
            .isEqualTo(ClassName(DEFAULT_PACKAGE, "ValkyrieIcons").nestedClass("Filled"))
    }

    @Test
    fun `resolveIconPackClassName - two nested levels returns doubly-nested ClassName`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Material") { child("Rounded") }
            },
        )
        assertThat(config.resolveIconPackClassName())
            .isEqualTo(
                ClassName(DEFAULT_PACKAGE, "ValkyrieIcons")
                    .nestedClass("Material")
                    .nestedClass("Rounded"),
            )
    }

    @Test
    fun `resolveIconPackClassName - three nested levels returns triply-nested ClassName`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Material") {
                    child("Rounded") {
                        child("Sharp")
                    }
                }
            },
        )
        assertThat(config.resolveIconPackClassName())
            .isEqualTo(
                ClassName(DEFAULT_PACKAGE, "ValkyrieIcons")
                    .nestedClass("Material")
                    .nestedClass("Rounded")
                    .nestedClass("Sharp"),
            )
    }

    @Test
    fun `resolveIconPackClassName - uses iconPackPackage not packageName`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackPackage = "androidx.compose.material.icons",
            iconPackTree = buildTree("Icons") {
                child("Filled")
            },
        )
        assertThat(config.resolveIconPackClassName())
            .isEqualTo(
                ClassName("androidx.compose.material.icons", "Icons")
                    .nestedClass("Filled"),
            )
    }
}
