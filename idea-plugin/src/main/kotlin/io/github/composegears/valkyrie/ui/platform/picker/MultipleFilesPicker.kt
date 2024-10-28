package io.github.composegears.valkyrie.ui.platform.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import com.intellij.openapi.application.EDT
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.openapi.vfs.VirtualFile
import io.github.composegears.valkyrie.ui.foundation.theme.LocalProject
import io.github.composegears.valkyrie.util.extension.isSvg
import io.github.composegears.valkyrie.util.extension.isXml
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberMultipleFilesPicker(): Picker<List<Path>> {
    if (LocalInspectionMode.current) return StubMultipleFilesPicker

    val project = LocalProject.current

    return remember {
        MultipleFilesPicker(
            project = project,
            filterCondition = {
                it.isSvg || it.isXml
            },
        )
    }
}

private object StubMultipleFilesPicker : Picker<List<Path>> {
    override suspend fun launch(): List<Path> = emptyList()
}

private class MultipleFilesPicker(
    private val project: Project,
    filterCondition: Condition<VirtualFile> = Condition { true },
) : Picker<List<Path>> {

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
        true,
    ).withFileFilter(filterCondition)

    override suspend fun launch(): List<Path> = withContext(Dispatchers.EDT) {
        FileChooser
            .chooseFiles(fileChooserDescriptor, project, null)
            .map(VirtualFile::toNioPath)
    }
}
