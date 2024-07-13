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
import io.github.composegears.valkyrie.ui.extension.isSvg
import io.github.composegears.valkyrie.ui.extension.isXml
import io.github.composegears.valkyrie.ui.extension.toFile
import io.github.composegears.valkyrie.ui.foundation.theme.LocalProject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun rememberMultipleFilesPicker(): Picker<List<File>> {
    if (LocalInspectionMode.current) return StubMultipleFilesPicker

    val project = LocalProject.current

    return remember {
        MultipleFilesPicker(
            project = project,
            filterCondition = { file ->
                val extension = file.extension

                extension != null && (extension.isSvg() || extension.isXml())
            }
        )
    }
}

private object StubMultipleFilesPicker : Picker<List<File>> {
    override suspend fun launch(): List<File> = emptyList()
}

private class MultipleFilesPicker(
    private val project: Project,
    filterCondition: Condition<VirtualFile> = Condition { true }
) : Picker<List<File>> {

    private val fileChooserDescriptor = FileChooserDescriptor(
        /* chooseFiles = */ true,
        /* chooseFolders = */ false,
        /* chooseJars = */ false,
        /* chooseJarsAsFiles = */ false,
        /* chooseJarContents = */ false,
        /* chooseMultiple = */ true
    ).withFileFilter(filterCondition)

    override suspend fun launch(): List<File> = withContext(Dispatchers.EDT) {
        FileChooser
            .chooseFiles(fileChooserDescriptor, project, null)
            .map(VirtualFile::toFile)
    }
}