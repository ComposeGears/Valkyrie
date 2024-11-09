package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion

import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.parser.svgxml.util.IconType

sealed interface IconPackConversionState {

    data object IconsPickering : IconPackConversionState

    sealed interface BatchProcessing : IconPackConversionState {

        data class IconPackCreationState(
            val icons: List<BatchIcon>,
            val exportEnabled: Boolean,
        ) : BatchProcessing

        data object ImportValidationState : BatchProcessing
        data object ExportingState : BatchProcessing
    }
}

sealed interface BatchIcon {
    val iconName: IconName

    data class Broken(override val iconName: IconName) : BatchIcon

    data class Valid(
        override val iconName: IconName,
        val iconPack: IconPack,
        val iconType: IconType,
        val irImageVector: IrImageVector,
    ) : BatchIcon
}

@JvmInline
value class IconName(val value: String)

sealed interface IconPack {
    val iconPackage: String
    val currentNestedPack: String

    data class Single(
        override val iconPackage: String,
        val iconPackName: String,
    ) : IconPack {
        override val currentNestedPack = ""
    }

    data class Nested(
        override val iconPackage: String,
        val iconPackName: String,
        val nestedPacks: List<String>,
        override val currentNestedPack: String,
    ) : IconPack
}
