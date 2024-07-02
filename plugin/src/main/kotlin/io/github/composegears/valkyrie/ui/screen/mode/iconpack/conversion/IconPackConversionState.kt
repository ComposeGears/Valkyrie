package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion

import androidx.compose.ui.graphics.painter.Painter
import java.io.File

sealed interface IconPackConversionState {

    data object IconsPickering : IconPackConversionState

    data class BatchFilesProcessing(
        val iconsToProcess: List<BatchIcon> = emptyList(),
    ) : IconPackConversionState {

        data class BatchIcon(
            val iconPack: IconPack,
            val iconName: IconName,
            val extension: String,
            val painter: Painter?,
            val file: File,
        )

        @JvmInline
        value class IconName(val value: String)

        sealed interface IconPack {
            val packageName: String
            val currentNestedPack: String

            data class Single(
                val iconPackage: String,
                val iconPackName: String
            ) : IconPack {
                override val packageName = iconPackage
                override val currentNestedPack = ""
            }

            data class Nested(
                val iconPackage: String,
                val iconPackName: String,
                val nestedPacks: List<String>,
                override val currentNestedPack: String
            ) : IconPack {
                override val packageName = "$iconPackage.${currentNestedPack.lowercase()}"
            }
        }
    }
}