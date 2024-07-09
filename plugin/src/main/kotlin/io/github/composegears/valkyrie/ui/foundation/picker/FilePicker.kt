package io.github.composegears.valkyrie.ui.foundation.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.intellij.openapi.application.EDT
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.openapi.vfs.VirtualFile
import io.github.composegears.valkyrie.ui.extension.isSvg
import io.github.composegears.valkyrie.ui.extension.isXml
import io.github.composegears.valkyrie.ui.extension.toFile
import io.github.composegears.valkyrie.ui.foundation.theme.LocalProject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun rememberFilePicker(): Picker<File?> {
    val project = LocalProject.current

    return remember {
        FilePicker(
            project = project,
            filterCondition = { file ->
                val extension = file.extension

                extension != null && (extension.isSvg() || extension.isXml())
            }
        )
    }
}

class FilePicker(
    private val project: Project,
    filterCondition: Condition<VirtualFile> = Condition { true }
) : Picker<File?> {

    private val fileChooserDescriptor = FileChooserDescriptor(
        /* chooseFiles = */ true,
        /* chooseFolders = */ false,
        /* chooseJars = */ false,
        /* chooseJarsAsFiles = */ false,
        /* chooseJarContents = */ false,
        /* chooseMultiple = */ false
    ).withFileFilter(filterCondition)

    override suspend fun launch() = withContext(Dispatchers.EDT) {
        FileChooser
            .chooseFile(fileChooserDescriptor, project, null)
            ?.toFile()
    }
}