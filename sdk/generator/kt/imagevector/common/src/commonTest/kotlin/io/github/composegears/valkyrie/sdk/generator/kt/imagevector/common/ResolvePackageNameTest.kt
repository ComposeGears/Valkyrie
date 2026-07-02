package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common

import io.github.composegears.valkyrie.sdk.core.tree.buildTree
import io.github.composegears.valkyrie.sdk.core.tree.child
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.shared.DEFAULT_PACKAGE
import kotlin.test.Test
import kotlin.test.assertEquals

class ResolvePackageNameTest {

    @Test
    fun `no iconPackTree returns packageName`() {
        val config = ImageVectorGeneratorConfig.simple(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
        )
        assertEquals(DEFAULT_PACKAGE, config.resolvePackageName())
    }

    @Test
    fun `root-only tree returns packageName`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons"),
        )
        assertEquals(DEFAULT_PACKAGE, config.resolvePackageName())
    }

    @Test
    fun `one nested level appends lowercase segment`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Filled")
            },
        )
        assertEquals("${DEFAULT_PACKAGE}.filled", config.resolvePackageName())
    }

    @Test
    fun `two nested levels appends both lowercase segments`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Material") {
                    child("Rounded")
                }
            },
        )
        assertEquals("${DEFAULT_PACKAGE}.material.rounded", config.resolvePackageName())
    }

    @Test
    fun `useFlatPackage always returns packageName`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Filled")
            },
            imageVector = ImageVectorConfig(useFlatPackage = true),
        )
        assertEquals(DEFAULT_PACKAGE, config.resolvePackageName())
    }

    @Test
    fun `useFlatPackage with deep nesting still returns packageName`() {
        val config = ImageVectorGeneratorConfig.iconPack(
            iconName = "Add",
            packageName = DEFAULT_PACKAGE,
            iconPackTree = buildTree("ValkyrieIcons") {
                child("Material") {
                    child("Rounded")
                }
            },
            imageVector = ImageVectorConfig(useFlatPackage = true),
        )
        assertEquals(DEFAULT_PACKAGE, config.resolvePackageName())
    }
}
