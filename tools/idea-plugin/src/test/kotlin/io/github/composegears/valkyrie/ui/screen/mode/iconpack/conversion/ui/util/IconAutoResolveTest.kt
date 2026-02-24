package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import io.github.composegears.valkyrie.parser.unified.model.IconType
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.BatchIcon
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconSource
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util.BatchIconsIssuesResolver.resolve
import io.github.composegears.valkyrie.util.IR_STUB
import kotlin.test.Test

class IconAutoResolveTest {

    private val singlePack = IconPack.Single(
        iconPackage = "com.example.icons",
        iconPackName = "ValkyrieIcons",
    )
    private val nestedPack = IconPack.Nested(
        iconPackage = "com.example.icons",
        iconPackName = "ExampleNestedPack",
        nestedPacks = listOf("Outlined", "Filled"),
        currentNestedPack = "Outlined",
    )

    private fun validIcon(name: String, pack: IconPack): BatchIcon.Valid {
        return BatchIcon.Valid(
            iconName = IconName(name),
            iconPack = pack,
            iconType = IconType.SVG,
            irImageVector = IR_STUB,
        )
    }

    private fun brokenIcon(name: String, source: IconSource) = BatchIcon.Broken(
        iconName = IconName(name),
        iconSource = source,
    )

    @Test
    fun `empty list returns empty result`() {
        val icons = emptyList<BatchIcon>()
        assertThat(resolve(batchIcons = icons)).isEmpty()
    }

    @Test
    fun `broken icons are filtered out`() {
        val icons = listOf(
            brokenIcon("BrokenIcon", IconSource.File),
            brokenIcon("AnotherBroken", IconSource.Clipboard),
        )
        assertThat(resolve(batchIcons = icons)).isEmpty()
    }

    @Test
    fun `empty icon name is replaced with default name`() {
        val icons = listOf(validIcon("", singlePack))
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName }).containsExactly(IconName("IconName"))
    }

    @Test
    fun `spaces are removed from icon name`() {
        val icons = listOf(validIcon("Icon With Space", singlePack))
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName }).containsExactly(IconName("IconWithSpace"))
    }

    @Test
    fun `single, resolve exact duplicates with numeric suffix`() {
        val icons = listOf(
            validIcon("TestIcon", singlePack),
            validIcon("TestIcon", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("TestIcon"), IconName("TestIcon1"))
    }

    @Test
    fun `single, resolve three exact duplicates`() {
        val icons = listOf(
            validIcon("TestIcon", singlePack),
            validIcon("TestIcon", singlePack),
            validIcon("TestIcon", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("TestIcon"), IconName("TestIcon1"), IconName("TestIcon2"))
    }

    @Test
    fun `single, resolve case-insensitive duplicates`() {
        val icons = listOf(
            validIcon("TestIcon", singlePack),
            validIcon("Testicon", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName }).containsExactly(IconName("TestIcon"), IconName("Testicon1"))
    }

    @Test
    fun `single, no changes when no issues`() {
        val icons = listOf(
            validIcon("IconA", singlePack),
            validIcon("IconB", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("IconA"), IconName("IconB"))
    }

    @Test
    fun `single, resolve mixed issues`() {
        val icons = listOf(
            validIcon("", singlePack),
            validIcon("Icon With Space", singlePack),
            brokenIcon("BrokenIcon", IconSource.File),
            validIcon("Duplicate", singlePack),
            validIcon("Duplicate", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(
                IconName("IconName"),
                IconName("IconWithSpace"),
                IconName("Duplicate"),
                IconName("Duplicate1"),
            )
    }

    @Test
    fun `nested, resolve duplicates within same nested pack`() {
        val icons = listOf(
            validIcon("Test", nestedPack),
            validIcon("Test", nestedPack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("Test"), IconName("Test1"))
    }

    @Test
    fun `nested, allow duplicates across different nested packs`() {
        val icons = listOf(
            validIcon("Test", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Test", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        val resolved = resolve(batchIcons = icons, useFlatPackage = false)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("Test"), IconName("Test"))
    }

    @Test
    fun `nested with flatPackage, resolve duplicates across different nested packs`() {
        val icons = listOf(
            validIcon("Test", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Test", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        val resolved = resolve(batchIcons = icons, useFlatPackage = true)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("Test"), IconName("Test1"))
    }

    @Test
    fun `nested with flatPackage, resolve case-insensitive duplicates across different nested packs`() {
        val icons = listOf(
            validIcon("TestIcon", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Testicon", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        val resolved = resolve(batchIcons = icons, useFlatPackage = true)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("TestIcon"), IconName("Testicon1"))
    }

    @Test
    fun `nested without flatPackage, allow case-insensitive names in different nested packs`() {
        val icons = listOf(
            validIcon("TestIcon", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Testicon", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        val resolved = resolve(batchIcons = icons, useFlatPackage = false)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("TestIcon"), IconName("Testicon"))
    }

    @Test
    fun `multiple empty names get resolved as duplicates`() {
        val icons = listOf(
            validIcon("", singlePack),
            validIcon("", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("IconName"), IconName("IconName1"))
    }

    @Test
    fun `suffix avoids collision with existing names`() {
        val icons = listOf(
            validIcon("Test", singlePack),
            validIcon("Test", singlePack),
            validIcon("Test1", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("Test"), IconName("Test2"), IconName("Test1"))
    }

    @Test
    fun `spaces removed causing duplicate are resolved`() {
        val icons = listOf(
            validIcon("My Icon", singlePack),
            validIcon("MyIcon", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("MyIcon"), IconName("MyIcon1"))
    }

    @Test
    fun `empty name collides with existing IconName`() {
        val icons = listOf(
            validIcon("", singlePack),
            validIcon("IconName", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("IconName"), IconName("IconName1"))
    }

    @Test
    fun `three case-insensitive variants`() {
        val icons = listOf(
            validIcon("TestIcon", singlePack),
            validIcon("Testicon", singlePack),
            validIcon("TESTICON", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("TestIcon"), IconName("Testicon1"), IconName("TESTICON2"))
    }

    @Test
    fun `exact duplicates combined with case-insensitive duplicate`() {
        val icons = listOf(
            validIcon("Test", singlePack),
            validIcon("Test", singlePack),
            validIcon("test", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("Test"), IconName("Test1"), IconName("test2"))
    }

    @Test
    fun `single valid icon is returned unchanged`() {
        val icons = listOf(validIcon("OnlyIcon", singlePack))
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName }).containsExactly(IconName("OnlyIcon"))
    }

    @Test
    fun `order is preserved after resolving`() {
        val icons = listOf(
            validIcon("Charlie", singlePack),
            validIcon("Alpha", singlePack),
            validIcon("Bravo", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("Charlie"), IconName("Alpha"), IconName("Bravo"))
    }

    @Test
    fun `mixed broken and valid preserves valid order`() {
        val icons = listOf(
            brokenIcon("First", IconSource.File),
            validIcon("Second", singlePack),
            brokenIcon("Third", IconSource.Clipboard),
            validIcon("Fourth", singlePack),
        )
        val resolved = resolve(batchIcons = icons)
        assertThat(resolved.map { it.iconName })
            .containsExactly(IconName("Second"), IconName("Fourth"))
    }
}
