package io.github.composegears.valkyrie.jewel.platform.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.intellij.openapi.application.EDT
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.jewel.platform.LocalProject
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberDirectoryPicker(): Picker<Path?> {
    val project = LocalProject.current

    return remember { DirectoryPicker(project = project) }
}

private class DirectoryPicker(private val project: Project) : Picker<Path?> {

    private val fileChooserDescriptor = FileChooserDescriptor(
        /* chooseFiles = */
        false,
        /* chooseFolders = */
        true,
        /* chooseJars = */
        false,
        /* chooseJarsAsFiles = */
        false,
        /* chooseJarContents = */
        false,
        /* chooseMultiple = */
        false,
    )

    override suspend fun launch(): Path? = withContext(Dispatchers.EDT) {
        FileChooser.chooseFile(fileChooserDescriptor, project, null)?.toNioPath()
    }
}
