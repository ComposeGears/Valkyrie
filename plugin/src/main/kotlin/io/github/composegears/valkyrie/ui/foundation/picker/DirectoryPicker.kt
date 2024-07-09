package io.github.composegears.valkyrie.ui.foundation.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.intellij.openapi.application.EDT
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.ui.foundation.theme.LocalProject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberDirectoryPicker(): Picker<String?> {
    val project = LocalProject.current

    return remember { DirectoryPicker(project = project) }
}

class DirectoryPicker(private val project: Project) : Picker<String?> {

    private val fileChooserDescriptor = FileChooserDescriptor(
        /* chooseFiles = */ false,
        /* chooseFolders = */ true,
        /* chooseJars = */ false,
        /* chooseJarsAsFiles = */ false,
        /* chooseJarContents = */ false,
        /* chooseMultiple = */ false
    )

    override suspend fun launch(): String? = withContext(Dispatchers.EDT) {
        FileChooser.chooseFile(fileChooserDescriptor, project, null)?.path
    }
}