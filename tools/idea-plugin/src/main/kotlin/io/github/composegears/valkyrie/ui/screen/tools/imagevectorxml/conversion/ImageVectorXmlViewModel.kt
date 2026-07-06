package io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intellij.openapi.application.readAction
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.sdk.intellij.psi.imagevector.ImageVectorPsiParser
import io.github.composegears.valkyrie.sdk.ir.xml.toVectorXmlString
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorSource
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlAction
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlEvent
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlEvent.CopyInClipboard
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlEvent.ExportXmlFile
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlParams
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlParams.PathSource
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlParams.TextSource
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.ImageVectorXmlState
import io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model.XmlContent
import io.github.composegears.valkyrie.util.extension.PsiKtFileFactory
import io.github.composegears.valkyrie.util.extension.resolveKtFile
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.psi.KtFile

class ImageVectorXmlViewModel(
    private val project: Project,
    savedState: SavedStateHandle,
    params: ImageVectorXmlParams,
) : ViewModel() {

    private val _state = savedState.getMutableStateFlow<ImageVectorXmlState>(
        key = "ImageVectorXmlViewModel",
        initialValue = ImageVectorXmlState.Initial,
    )
    val state = _state.asStateFlow()

    private val _events = Channel<ImageVectorXmlEvent>()
    val events = _events.receiveAsFlow()

    init {
        if (_state.value is ImageVectorXmlState.Initial) {
            when (params) {
                is PathSource -> convertFromPath(params.path)
                is TextSource -> convertFromText(params.kotlinCode)
            }
        }
    }

    fun onAction(action: ImageVectorXmlAction) {
        val state = _state.value.safeAs<ImageVectorXmlState.Content>() ?: return

        viewModelScope.launch {
            when (action) {
                is ImageVectorXmlAction.OnExport -> {
                    _events.send(
                        ExportXmlFile(
                            fileName = "${state.xmlContent.name}.xml",
                            content = action.text,
                        ),
                    )
                }
                is ImageVectorXmlAction.OnCopyInClipboard -> {
                    _events.send(CopyInClipboard(action.text))
                }
                is ImageVectorXmlAction.OnIconNameChange -> {
                    val currentState = _state.value.safeAs<ImageVectorXmlState.Content>() ?: return@launch
                    _state.value = currentState.copy(
                        xmlContent = currentState.xmlContent.copy(
                            name = action.name,
                        ),
                    )
                }
            }
        }
    }

    private fun convertFromPath(path: Path) = viewModelScope.launch(Dispatchers.IO) {
        val ktFile = path.resolveKtFile(project)

        if (ktFile == null) {
            _state.value = ImageVectorXmlState.Error("Failed to read Kotlin file")
            return@launch
        }

        val fileName = ktFile.virtualFile.nameWithoutExtension

        parseImageVectorToXml(ktFile, preferredName = "ic_${sanitizeResourceName(fileName)}")
            .onFailure {
                _state.value = ImageVectorXmlState.Error(
                    message = "Failed to parse ImageVector from file",
                    stacktrace = "Error: ${it.message}",
                )
            }
            .onSuccess {
                _state.value = ImageVectorXmlState.Content(
                    iconSource = ImageVectorSource.FileBasedIcon(path),
                    xmlContent = it,
                )
            }
    }

    private fun convertFromText(kotlinCode: String) = viewModelScope.launch(Dispatchers.IO) {
        val ktFile = PsiKtFileFactory.createFromText(
            project = project,
            name = "ImageVector.kt",
            text = kotlinCode,
        )

        if (ktFile == null) {
            _state.value = ImageVectorXmlState.Error("Failed to parse Kotlin code")
            return@launch
        }

        parseImageVectorToXml(ktFile)
            .onFailure {
                _state.value = ImageVectorXmlState.Error(
                    message = "Failed to parse ImageVector from code",
                    stacktrace = "Error: ${it.message}",
                )
            }
            .onSuccess {
                _state.value = ImageVectorXmlState.Content(
                    iconSource = ImageVectorSource.TextBasedIcon(kotlinCode),
                    xmlContent = it,
                )
            }
    }

    private suspend fun parseImageVectorToXml(
        ktFile: KtFile,
        preferredName: String? = null,
    ): Result<XmlContent> {
        return withContext(Dispatchers.Default) {
            runCatching {
                readAction {
                    val irImageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)
                        ?: error("Failed to parse image vector psi")

                    val xmlCode = irImageVector.toVectorXmlString()
                    val iconName = preferredName ?: sanitizeResourceName(irImageVector.name)

                    XmlContent(
                        name = iconName,
                        xmlCode = xmlCode,
                        irImageVector = irImageVector,
                    )
                }
            }
        }
    }
}

/**
 * Normalizes a file name into a valid Android XML resource name segment: lowercase, with any
 * character outside [a-z0-9] replaced by `_`. Leading/trailing underscores are trimmed and an
 * empty result falls back to "icon". The caller prefixes it with "ic_", guaranteeing a letter start.
 */
internal fun sanitizeResourceName(name: String): String = name.lowercase()
    .replace("[^a-z0-9]".toRegex(), "_")
    .trim('_')
    .ifEmpty { "icon" }
