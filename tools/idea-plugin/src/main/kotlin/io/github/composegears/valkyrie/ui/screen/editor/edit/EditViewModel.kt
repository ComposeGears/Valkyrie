package io.github.composegears.valkyrie.ui.screen.editor.edit

import com.composegears.tiamat.Saveable
import com.composegears.tiamat.SavedState
import com.composegears.tiamat.TiamatViewModel
import com.intellij.openapi.application.readAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiManager
import io.github.composegears.valkyrie.extensions.safeAs
import io.github.composegears.valkyrie.ir.compose.toComposeImageVector
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent
import io.github.composegears.valkyrie.ui.screen.editor.EditorType
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.listDirectoryEntries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.kotlin.psi.KtFile

class EditViewModel(
    val savedState: SavedState?,
    val editorType: EditorType,
) : TiamatViewModel(),
    Saveable {

    private val _state = MutableStateFlow<EditState>(EditState.Select)
    val state = _state.asStateFlow()

    override fun saveToSaveState(): SavedState {
        return mapOf()
    }

    fun pickerEvent(project: Project, events: PickerEvent) {
        when (events) {
            is PickerEvent.PickDirectory -> events.path.listDirectoryEntries().processFiles(project)
            is PickerEvent.PickFiles -> events.paths.processFiles(project)
            is PickerEvent.ClipboardText -> {}
        }
    }

    private fun List<Path>.processFiles(project: Project) = viewModelScope.launch(Dispatchers.Default) {
        val irFiles = readAction {
            this@processFiles
                .mapNotNull { getKtFile(project, it.absolutePathString()) }
                .mapNotNull { ImageVectorPsiParser.parseToIrImageVector(it) }
        }

        irFiles.map {
            it.toComposeImageVector()
        }

        println("size=${irFiles.size}")
    }
}

private fun getKtFile(project: Project, path: String): KtFile? {
    val virtualFile = LocalFileSystem.getInstance().findFileByPath(path)

    if (virtualFile == null) {
        println("File not found: $path")
        return null
    }

    return PsiManager.getInstance(project)
        .findFile(virtualFile)
        .safeAs<KtFile>()
}
