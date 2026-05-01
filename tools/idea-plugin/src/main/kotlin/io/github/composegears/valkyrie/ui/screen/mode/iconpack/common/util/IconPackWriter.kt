package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.util

import com.intellij.openapi.command.writeCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import io.github.composegears.valkyrie.generator.core.IconPack
import io.github.composegears.valkyrie.generator.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.generator.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.sdk.shared.ValkyrieMode
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.settings.updateNestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputFieldState

object IconPackWriter {

    suspend fun savePack(
        project: Project,
        inMemorySettings: InMemorySettings,
        inputFieldState: InputFieldState,
        writeToFile: Boolean = true,
    ) {
        inMemorySettings.update {
            packageName = inputFieldState.packageName.text
            iconPackPackage = inputFieldState.iconPackPackage.text
            iconPackName = inputFieldState.iconPackName.text
            updateNestedPack(inputFieldState.nestedPacks.map { it.inputFieldState.text })
            mode = ValkyrieMode.IconPack
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
                    license = inputFieldState.license.text.ifEmpty { null },
                ),
            )

            val fileName = "${iconPack.name}.kt"
            val content = iconPack.content

            writeTextFile(
                project = project,
                directoryPath = currentSettings.iconPackDestination,
                fileName = fileName,
                content = content,
            )
        }
    }

    private suspend fun writeTextFile(
        project: Project,
        directoryPath: String,
        fileName: String,
        content: String,
    ) {
        writeCommandAction(
            project = project,
            commandName = "Generate Icon Pack",
        ) {
            runCatching {
                val dir = VfsUtil.createDirectoryIfMissing(directoryPath) ?: return@writeCommandAction
                val file = dir.findChild(fileName) ?: dir.createChildData(IconPackWriter, fileName)

                VfsUtil.saveText(file, content)
            }.getOrElse {
                Logger.getInstance(IconPackWriter::class.java).error("Failed to save iconpack: ${it.message}")
            }
        }
    }
}
