package io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
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
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.psi.KtFile

class ImageVectorXmlViewModel(
    private val project: Project,
    savedState: MutableSavedState,
    params: ImageVectorXmlParams,
) : ViewModel() {

    private val stateRecord = savedState.recordOf<ImageVectorXmlState>(
        key = "conversionState",
        initialValue = ImageVectorXmlState.Loading,
    )
    val state = stateRecord.asStateFlow()

    private val _events = Channel<ImageVectorXmlEvent>()
    val events = _events.receiveAsFlow()

    init {
        when (params) {
            is PathSource -> convertFromPath(params.path)
            is TextSource -> convertFromText(params.kotlinCode)
        }
    }

    fun onAction(action: ImageVectorXmlAction) {
        val state = stateRecord.value.safeAs<ImageVectorXmlState.Content>() ?: return

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
                    changeIconName(action.name)
                }
            }
        }
    }

    private fun convertFromPath(path: Path) = viewModelScope.launch(Dispatchers.IO) {
        val ktFile = path.resolveKtFile(project)

        if (ktFile == null) {
            stateRecord.value = ImageVectorXmlState.Error("Failed to read Kotlin file")
            return@launch
        }

        parseImageVectorToXml(ktFile)
            .onFailure {
                stateRecord.value = ImageVectorXmlState.Error(
                    message = "Failed to parse ImageVector from file",
                    stacktrace = "Error: ${it.message}",
                )
            }
            .onSuccess {
                stateRecord.value = ImageVectorXmlState.Content(
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
            stateRecord.value = ImageVectorXmlState.Error("Failed to parse Kotlin code")
            return@launch
        }

        parseImageVectorToXml(ktFile)
            .onFailure {
                stateRecord.value = ImageVectorXmlState.Error(
                    message = "Failed to parse ImageVector from code",
                    stacktrace = "Error: ${it.message}",
                )
            }
            .onSuccess {
                stateRecord.value = ImageVectorXmlState.Content(
                    iconSource = ImageVectorSource.TextBasedIcon(kotlinCode),
                    xmlContent = it,
                )
            }
    }

    private suspend fun changeIconName(name: String) = withContext(Dispatchers.Default) {
        val currentState = stateRecord.value.safeAs<ImageVectorXmlState.Content>() ?: return@withContext

        val newXml = currentState.xmlContent.irImageVector
            .copy(name = name)
            .toVectorXmlString()

        stateRecord.value = currentState.copy(
            xmlContent = currentState.xmlContent.copy(
                name = name,
                xmlCode = newXml,
            ),
        )
    }

    private suspend fun parseImageVectorToXml(ktFile: KtFile): Result<XmlContent> {
        return withContext(Dispatchers.Default) {
            runCatching {
                readAction {
                    val irImageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)
                        ?: error("Failed to parse image vector psi")

                    val xmlCode = irImageVector.toVectorXmlString()
                    val iconName = irImageVector.name.lowercase().ifEmpty { "Icon" }

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
