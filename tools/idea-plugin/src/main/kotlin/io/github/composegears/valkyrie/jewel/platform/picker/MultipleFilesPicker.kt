package io.github.composegears.valkyrie.jewel.platform.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.intellij.openapi.application.EDT
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.openapi.vfs.VirtualFile
import io.github.composegears.valkyrie.jewel.platform.LocalProject
import io.github.composegears.valkyrie.util.extension.isSvg
import io.github.composegears.valkyrie.util.extension.isXml
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberMultipleFilesPicker(): Picker<List<Path>> {
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
