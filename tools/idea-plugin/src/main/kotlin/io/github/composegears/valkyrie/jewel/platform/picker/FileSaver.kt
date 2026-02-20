package io.github.composegears.valkyrie.jewel.platform.picker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.fileChooser.FileSaverDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import io.github.composegears.valkyrie.jewel.platform.LocalProject
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberFileSaver(
    title: String,
    description: String,
): FileSaver {
    val project = LocalProject.current

    return remember {
        FileSaverImpl(
            project = project,
            title = title,
            description = description,
        )
    }
}

interface FileSaver {
    suspend fun save(fileName: String, content: String): SaveResult
}

sealed interface SaveResult {
    data object Success : SaveResult
    data object Cancelled : SaveResult
    data class Error(val message: String) : SaveResult
}

private class FileSaverImpl(
    private val project: Project,
    private val title: String,
    private val description: String,
) : FileSaver {

    override suspend fun save(fileName: String, content: String): SaveResult = withContext(Dispatchers.EDT) {
        val descriptor = FileSaverDescriptor(title, description)
        val dialog = FileChooserFactory.getInstance().createSaveFileDialog(descriptor, project)

        val fileWrapper = dialog.save(null as? Path, fileName)
            ?: return@withContext SaveResult.Cancelled

        return@withContext runCatching {
            ApplicationManager.getApplication().runWriteAction {
                val file = fileWrapper.file
                file.writeText(content)
                LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)
            }
            SaveResult.Success
        }.getOrElse {
            SaveResult.Error(message = it.message ?: "Unknown error")
        }
    }
}
