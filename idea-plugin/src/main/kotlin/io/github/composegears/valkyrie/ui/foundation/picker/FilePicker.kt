package io.github.composegears.valkyrie.ui.foundation.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import com.intellij.openapi.application.EDT
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.openapi.vfs.VirtualFile
import io.github.composegears.valkyrie.parser.isSvg
import io.github.composegears.valkyrie.parser.isXml
import io.github.composegears.valkyrie.ui.foundation.theme.LocalProject
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberKtFilePicker(): Picker<Path?> {
  if (LocalInspectionMode.current) return StubFilePicker

  val project = LocalProject.current

  return remember {
    FilePicker(
      project = project,
      filterCondition = { vf -> vf.extension.isKt },
    )
  }
}

@Composable
fun rememberFilePicker(): Picker<Path?> {
  if (LocalInspectionMode.current) return StubFilePicker

  val project = LocalProject.current

  return remember {
    FilePicker(
      project = project,
      filterCondition = { vf ->
        val extension = vf.extension
        extension.isSvg || extension.isXml
      },
    )
  }
}

private object StubFilePicker : Picker<Path?> {
  override suspend fun launch(): Path? = null
}

private class FilePicker(
  private val project: Project,
  filterCondition: Condition<VirtualFile> = Condition { true },
) : Picker<Path?> {

  private val fileChooserDescriptor = FileChooserDescriptor(
    /* chooseFiles = */
    true,
    /* chooseFolders = */
    false,
    /* chooseJars = */
    false,
    /* chooseJarsAsFiles = */
    false,
    /* chooseJarContents = */
    false,
    /* chooseMultiple = */
    false,
  ).withFileFilter(filterCondition)

  override suspend fun launch(): Path? = withContext(Dispatchers.EDT) {
    FileChooser
      .chooseFile(fileChooserDescriptor, project, null)
      ?.toNioPath()
  }
}

private inline val String?.isKt: Boolean get() = equals(other = "kt", ignoreCase = true)
