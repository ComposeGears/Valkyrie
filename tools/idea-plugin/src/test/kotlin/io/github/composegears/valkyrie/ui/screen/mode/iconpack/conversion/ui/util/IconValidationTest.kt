package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.parser.unified.model.IconType
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.BatchIcon
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconSource
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util.BatchIconsValidator.toMessageText
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util.BatchIconsValidator.validate
import io.github.composegears.valkyrie.util.IR_STUB
import kotlin.test.Test

class IconValidationTest {

    private val singlePack = IconPack.Single(
        iconPackage = "com.example.icons",
        iconPackName = "ValkyrieIcons",
    )
    private val nestedPack = IconPack.Nested(
        iconPackage = "com.example.icons",
        iconPackName = "ExampleNestedPack",
        nestedPacks = listOf("Outlined,Filled"),
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
    fun `single, detect empty icon names`() {
        val icons = listOf(
            validIcon("SampleIcon", singlePack),
            validIcon("", singlePack),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(mapOf(ValidationError.IconNameEmpty to listOf(IconName(""))))
    }

    @Test
    fun `single, detect icons with spaces`() {
        val icons = listOf(
            validIcon("SampleIcon", singlePack),
            validIcon("Icon With Space", singlePack),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(mapOf(ValidationError.IconNameContainsSpace to listOf(IconName("Icon With Space"))))
    }

    @Test
    fun `simple, detect failed to parse file icons`() {
        val icons = listOf(brokenIcon("BrokenFileIcon", IconSource.File))
        assertThat(validate(batchIcons = icons))
            .isEqualTo(mapOf(ValidationError.FailedToParseFile to listOf(IconName("BrokenFileIcon"))))
    }

    @Test
    fun `simple, detect failed to parse clipboard icons`() {
        val icons = listOf(
            brokenIcon("BrokenClipboardIcon", IconSource.Clipboard),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(mapOf(ValidationError.FailedToParseClipboard to listOf(IconName("BrokenClipboardIcon"))))
    }

    @Test
    fun `simple, detect duplicate icons`() {
        val icons = listOf(
            validIcon("DuplicateIcon", singlePack),
            validIcon("DuplicateIcon", singlePack),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(mapOf(ValidationError.HasDuplicates to listOf(IconName("DuplicateIcon"))))
    }

    @Test
    fun `simple, detect multiple issues`() {
        val icons = listOf(
            validIcon("", singlePack),
            validIcon("Icon With Space", singlePack),
            brokenIcon("BrokenFileIcon", IconSource.File),
            validIcon("DuplicateIcon", singlePack),
            validIcon("DuplicateIcon", singlePack),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(
                mapOf(
                    ValidationError.IconNameEmpty to listOf(IconName("")),
                    ValidationError.IconNameContainsSpace to listOf(IconName("Icon With Space")),
                    ValidationError.FailedToParseFile to listOf(IconName("BrokenFileIcon")),
                    ValidationError.HasDuplicates to listOf(IconName("DuplicateIcon")),
                ),
            )
    }

    @Test
    fun `nested, detect multiple issues`() {
        val icons = listOf(
            validIcon("", nestedPack),
            validIcon("Icon With Space", nestedPack),
            validIcon("DuplicateIcon", nestedPack),
            validIcon("DuplicateIcon", nestedPack),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(
                mapOf(
                    ValidationError.IconNameEmpty to listOf(IconName("")),
                    ValidationError.IconNameContainsSpace to listOf(IconName("Icon With Space")),
                    ValidationError.HasDuplicates to listOf(IconName("DuplicateIcon")),
                ),
            )
    }

    @Test
    fun `nested, detect duplicates within same nested pack`() {
        val icons = listOf(
            validIcon("Test", nestedPack),
            validIcon("Test", nestedPack),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(mapOf(ValidationError.HasDuplicates to listOf(IconName("Test"))))
    }

    @Test
    fun `nested, allow duplicates for different currentNestedPack`() {
        val icons = listOf(
            validIcon("Test", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Test", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        assertThat(validate(batchIcons = icons)).isEqualTo(emptyMap())
    }

    @Test
    fun `nested, detects duplicates across different nested packs`() {
        val icons = listOf(
            validIcon("Test", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Test", nestedPack.copy(currentNestedPack = "Outlined")),
            validIcon("Test", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(mapOf(ValidationError.HasDuplicates to listOf(IconName("Test"))))
    }

    @Test
    fun `nested with flatPackage, detect exact duplicates across different nested packs`() {
        // When useFlatPackage = true, Test.kt from "Filled" and Test.kt from "Outlined"
        // would overwrite each other since they go to the same folder
        val icons = listOf(
            validIcon("Test", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Test", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        assertThat(validate(batchIcons = icons, useFlatPackage = true))
            .isEqualTo(mapOf(ValidationError.HasDuplicates to listOf(IconName("Test"))))
    }

    @Test
    fun `nested without flatPackage, allow exact duplicates in different nested packs`() {
        // Without useFlatPackage, Filled/Test.kt and Outlined/Test.kt are in different folders
        val icons = listOf(
            validIcon("Test", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Test", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        assertThat(validate(batchIcons = icons, useFlatPackage = false)).isEqualTo(emptyMap())
    }

    @Test
    fun `single, detect case-insensitive duplicates on macOS file system`() {
        // On macOS with case-insensitive file system, TestIcon.kt and Testicon.kt collide
        val icons = listOf(
            validIcon("TestIcon", singlePack),
            validIcon("Testicon", singlePack),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(mapOf(ValidationError.HasCaseInsensitiveDuplicates to listOf(IconName("TestIcon"), IconName("Testicon"))))
    }

    @Test
    fun `nested, detect case-insensitive duplicates on macOS file system`() {
        // On macOS with case-insensitive file system, TestIcon.kt and Testicon.kt collide
        val icons = listOf(
            validIcon("TestIcon", nestedPack),
            validIcon("Testicon", nestedPack),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(mapOf(ValidationError.HasCaseInsensitiveDuplicates to listOf(IconName("TestIcon"), IconName("Testicon"))))
    }

    @Test
    fun `nested, allow same case-insensitive names in different nested packs`() {
        val icons = listOf(
            validIcon("TestIcon", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Testicon", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        assertThat(validate(batchIcons = icons)).isEqualTo(emptyMap())
    }

    @Test
    fun `nested with flatPackage, detect case-insensitive duplicates across different nested packs`() {
        // When useFlatPackage = true, all icons go to the same folder
        // So TestIcon.kt and Testicon.kt would collide even if they're in different nested packs
        val icons = listOf(
            validIcon("TestIcon", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Testicon", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        assertThat(validate(batchIcons = icons, useFlatPackage = true))
            .isEqualTo(mapOf(ValidationError.HasCaseInsensitiveDuplicates to listOf(IconName("TestIcon"), IconName("Testicon"))))
    }

    @Test
    fun `nested with flatPackage, allow case-insensitive names in different icon packs`() {
        // Even with useFlatPackage = true, different icon packs have separate folders
        val icons = listOf(
            validIcon("TestIcon", nestedPack.copy(iconPackName = "ValkyrieIcons", currentNestedPack = "Filled")),
            validIcon("Testicon", nestedPack.copy(iconPackName = "AnotherIconPack", currentNestedPack = "Outlined")),
        )
        assertThat(validate(batchIcons = icons, useFlatPackage = true)).isEqualTo(emptyMap())
    }

    @Test
    fun `empty list returns empty map`() {
        assertThat(validate(batchIcons = emptyList())).isEqualTo(emptyMap())
    }

    @Test
    fun `all valid icons with unique names return empty map`() {
        val icons = listOf(
            validIcon("IconA", singlePack),
            validIcon("IconB", singlePack),
            validIcon("IconC", singlePack),
        )
        assertThat(validate(batchIcons = icons)).isEqualTo(emptyMap())
    }

    @Test
    fun `single, exact and case-insensitive duplicates reported separately`() {
        val icons = listOf(
            validIcon("Test", singlePack),
            validIcon("Test", singlePack),
            validIcon("test", singlePack),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(
                mapOf(
                    ValidationError.HasDuplicates to listOf(IconName("Test")),
                    ValidationError.HasCaseInsensitiveDuplicates to listOf(IconName("Test"), IconName("test")),
                ),
            )
    }

    @Test
    fun `multiple broken file icons are accumulated`() {
        val icons = listOf(
            brokenIcon("Broken1", IconSource.File),
            brokenIcon("Broken2", IconSource.File),
            brokenIcon("Broken3", IconSource.Clipboard),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(
                mapOf(
                    ValidationError.FailedToParseFile to listOf(IconName("Broken1"), IconName("Broken2")),
                    ValidationError.FailedToParseClipboard to listOf(IconName("Broken3")),
                ),
            )
    }

    @Test
    fun `multiple empty names and multiple names with spaces are accumulated`() {
        val icons = listOf(
            validIcon("", singlePack),
            validIcon("", singlePack),
            validIcon("Has Space", singlePack),
            validIcon("Also Space", singlePack),
        )
        assertThat(validate(batchIcons = icons))
            .isEqualTo(
                mapOf(
                    ValidationError.IconNameEmpty to listOf(IconName(""), IconName("")),
                    ValidationError.IconNameContainsSpace to listOf(IconName("Has Space"), IconName("Also Space")),
                    ValidationError.HasDuplicates to listOf(IconName("")),
                ),
            )
    }

    @Test
    fun `toMessageText, single broken file`() {
        val errors = mapOf(
            ValidationError.FailedToParseFile to listOf(IconName("BadFile")),
        )
        assertThat(errors.toMessageText())
            .isEqualTo("• Failed to parse: \"BadFile\"")
    }

    @Test
    fun `toMessageText, single broken clipboard`() {
        val errors = mapOf(
            ValidationError.FailedToParseClipboard to listOf(IconName("ClipIcon")),
        )
        assertThat(errors.toMessageText())
            .isEqualTo("• Failed to parse some icon from clipboard")
    }

    @Test
    fun `toMessageText, single empty name`() {
        val errors = mapOf(
            ValidationError.IconNameEmpty to listOf(IconName("")),
        )
        assertThat(errors.toMessageText())
            .isEqualTo("• Contains icon with empty name")
    }

    @Test
    fun `toMessageText, multiple empty names uses plural`() {
        val errors = mapOf(
            ValidationError.IconNameEmpty to listOf(IconName(""), IconName("")),
        )
        assertThat(errors.toMessageText())
            .isEqualTo("• Contains icons with empty name")
    }

    @Test
    fun `toMessageText, single space name`() {
        val errors = mapOf(
            ValidationError.IconNameContainsSpace to listOf(IconName("My Icon")),
        )
        assertThat(errors.toMessageText())
            .isEqualTo("• Contains icon with space in name: \"My Icon\"")
    }

    @Test
    fun `toMessageText, multiple space names uses plural`() {
        val errors = mapOf(
            ValidationError.IconNameContainsSpace to listOf(IconName("My Icon"), IconName("Other Icon")),
        )
        assertThat(errors.toMessageText())
            .isEqualTo("• Contains icons with space in name: \"My Icon\", \"Other Icon\"")
    }

    @Test
    fun `toMessageText, single duplicate`() {
        val errors = mapOf(
            ValidationError.HasDuplicates to listOf(IconName("Dup")),
        )
        assertThat(errors.toMessageText())
            .isEqualTo("• Contains duplicate icon: \"Dup\"")
    }

    @Test
    fun `toMessageText, multiple duplicates uses plural`() {
        val errors = mapOf(
            ValidationError.HasDuplicates to listOf(IconName("Dup1"), IconName("Dup2")),
        )
        assertThat(errors.toMessageText())
            .isEqualTo("• Contains duplicate icons: \"Dup1\", \"Dup2\"")
    }

    @Test
    fun `toMessageText, case-insensitive duplicates`() {
        val errors = mapOf(
            ValidationError.HasCaseInsensitiveDuplicates to listOf(IconName("TestIcon"), IconName("Testicon")),
        )
        assertThat(errors.toMessageText())
            .isEqualTo("• Contains icons that collide on case-insensitive file systems (macOS/Windows): \"TestIcon\", \"Testicon\"")
    }

    @Test
    fun `toMessageText, multiple errors joined with newline`() {
        val errors = mapOf(
            ValidationError.IconNameEmpty to listOf(IconName("")),
            ValidationError.HasDuplicates to listOf(IconName("Dup")),
        )
        assertThat(errors.toMessageText())
            .isEqualTo(
                "• Contains icon with empty name\n" +
                    "• Contains duplicate icon: \"Dup\"",
            )
    }
}
