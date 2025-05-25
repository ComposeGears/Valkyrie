package io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.util

import com.intellij.openapi.application.ReadResult.Companion.writeAction
import com.intellij.openapi.vfs.VirtualFileManager
import io.github.composegears.valkyrie.extensions.writeToKt
import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.generator.jvm.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.generator.jvm.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.settings.updateNestedPack
import io.github.composegears.valkyrie.shared.Mode
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputFieldState

object IconPackWriter {

    fun savePack(
        inMemorySettings: InMemorySettings,
        inputFieldState: InputFieldState,
        writeToFile: Boolean = true,
    ) {
        inMemorySettings.update {
            packageName = inputFieldState.packageName.text
            iconPackPackage = inputFieldState.iconPackPackage.text
            iconPackName = inputFieldState.iconPackName.text
            updateNestedPack(inputFieldState.nestedPacks.map { it.inputFieldState.text })
            mode = Mode.IconPack
        }

        if (writeToFile) {
            val currentSettings = inMemorySettings.current

            val iconPack = IconPackGenerator.create(
                config = IconPackGeneratorConfig(
                    packageName = currentSettings.packageName,
                    iconPack = IconPack(
                        name = currentSettings.iconPackName,
                        nested = currentSettings.nestedPacks.map(::IconPack),
                    ),
                    useExplicitMode = currentSettings.useExplicitMode,
                    indentSize = currentSettings.indentSize,
                ),
            )
            iconPack.content.writeToKt(
                outputDir = currentSettings.iconPackDestination,
                nameWithoutExtension = iconPack.name,
            )
        }

        writeAction {
            VirtualFileManager.getInstance().syncRefresh()
        }
    }
}
