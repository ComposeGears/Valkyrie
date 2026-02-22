package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.parser.unified.model.IconType
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.BatchIcon
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconSource
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError
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
        assertThat(icons.checkImportIssues())
            .isEqualTo(mapOf(ValidationError.IconNameEmpty to listOf(IconName(""))))
    }

    @Test
    fun `single, detect icons with spaces`() {
        val icons = listOf(
            validIcon("SampleIcon", singlePack),
            validIcon("Icon With Space", singlePack),
        )
        assertThat(icons.checkImportIssues())
            .isEqualTo(mapOf(ValidationError.IconNameContainsSpace to listOf(IconName("Icon With Space"))))
    }

    @Test
    fun `simple, detect failed to parse file icons`() {
        val icons = listOf(brokenIcon("BrokenFileIcon", IconSource.File))
        assertThat(icons.checkImportIssues())
            .isEqualTo(mapOf(ValidationError.FailedToParseFile to listOf(IconName("BrokenFileIcon"))))
    }

    @Test
    fun `simple, detect failed to parse clipboard icons`() {
        val icons = listOf(
            brokenIcon("BrokenClipboardIcon", IconSource.Clipboard),
        )
        assertThat(icons.checkImportIssues())
            .isEqualTo(mapOf(ValidationError.FailedToParseClipboard to listOf(IconName("BrokenClipboardIcon"))))
    }

    @Test
    fun `simple, detect duplicate icons`() {
        val icons = listOf(
            validIcon("DuplicateIcon", singlePack),
            validIcon("DuplicateIcon", singlePack),
        )
        assertThat(icons.checkImportIssues())
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
        assertThat(icons.checkImportIssues())
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
        assertThat(icons.checkImportIssues())
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
        assertThat(icons.checkImportIssues())
            .isEqualTo(mapOf(ValidationError.HasDuplicates to listOf(IconName("Test"))))
    }

    @Test
    fun `nested, allow duplicates for different currentNestedPack`() {
        val icons = listOf(
            validIcon("Test", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Test", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        assertThat(icons.checkImportIssues()).isEqualTo(emptyMap())
    }

    @Test
    fun `nested, detects duplicates across different nested packs`() {
        val icons = listOf(
            validIcon("Test", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Test", nestedPack.copy(currentNestedPack = "Outlined")),
            validIcon("Test", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        assertThat(icons.checkImportIssues())
            .isEqualTo(mapOf(ValidationError.HasDuplicates to listOf(IconName("Test"))))
    }

    @Test
    fun `single, detect case-insensitive duplicates on macOS file system`() {
        // On macOS with case-insensitive file system, TestIcon.kt and Testicon.kt collide
        val icons = listOf(
            validIcon("TestIcon", singlePack),
            validIcon("Testicon", singlePack),
        )
        assertThat(icons.checkImportIssues())
            .isEqualTo(mapOf(ValidationError.HasCaseInsensitiveDuplicates to listOf(IconName("TestIcon"), IconName("Testicon"))))
    }

    @Test
    fun `nested, detect case-insensitive duplicates on macOS file system`() {
        // On macOS with case-insensitive file system, TestIcon.kt and Testicon.kt collide
        val icons = listOf(
            validIcon("TestIcon", nestedPack),
            validIcon("Testicon", nestedPack),
        )
        assertThat(icons.checkImportIssues())
            .isEqualTo(mapOf(ValidationError.HasCaseInsensitiveDuplicates to listOf(IconName("TestIcon"), IconName("Testicon"))))
    }

    @Test
    fun `nested, allow same case-insensitive names in different nested packs`() {
        val icons = listOf(
            validIcon("TestIcon", nestedPack.copy(currentNestedPack = "Filled")),
            validIcon("Testicon", nestedPack.copy(currentNestedPack = "Outlined")),
        )
        assertThat(icons.checkImportIssues()).isEqualTo(emptyMap())
    }
}
