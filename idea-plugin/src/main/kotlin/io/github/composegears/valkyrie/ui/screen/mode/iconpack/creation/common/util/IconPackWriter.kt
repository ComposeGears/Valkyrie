package io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.util

import com.intellij.openapi.application.ReadResult.Companion.writeAction
import com.intellij.openapi.vfs.VirtualFileManager
import io.github.composegears.valkyrie.generator.iconpack.IconPackGenerator
import io.github.composegears.valkyrie.generator.iconpack.IconPackGeneratorConfig
import io.github.composegears.valkyrie.processing.writter.FileWriter
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.domain.model.Mode
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputFieldState

object IconPackWriter {

  fun savePack(
    inMemorySettings: InMemorySettings,
    inputFieldState: InputFieldState,
  ) {
    inMemorySettings.updatePackageName(inputFieldState.packageName.text)
    inMemorySettings.updateIconPackName(inputFieldState.iconPackName.text)
    inMemorySettings.updateNestedPack(inputFieldState.nestedPacks.map { it.inputFieldState.text })
    inMemorySettings.updateMode(Mode.IconPack)

    val currentSettings = inMemorySettings.current

    val iconPack = IconPackGenerator.create(
      config = IconPackGeneratorConfig(
        packageName = currentSettings.packageName,
        iconPackName = currentSettings.iconPackName,
        subPacks = currentSettings.nestedPacks,
      ),
    )

    FileWriter.writeToFile(
      content = iconPack.content,
      outDirectory = currentSettings.iconPackDestination,
      fileName = iconPack.name,
    )

    writeAction {
      VirtualFileManager.getInstance().syncRefresh()
    }
  }
}
