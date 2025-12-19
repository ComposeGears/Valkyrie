package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
import com.intellij.openapi.application.readAction
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import io.github.composegears.valkyrie.sdk.ir.xml.toVectorXmlString
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.ImageVectorSource
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.ImageVectorToXmlParams
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.ImageVectorToXmlState
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.XmlContent
import io.github.composegears.valkyrie.util.extension.PsiKtFileFactory
import io.github.composegears.valkyrie.util.extension.resolveKtFile
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.psi.KtFile

class ImageVectorToXmlViewModel(
    private val project: Project,
    savedState: MutableSavedState,
    params: ImageVectorToXmlParams,
) : ViewModel() {

    private val stateRecord = savedState.recordOf<ImageVectorToXmlState?>(
        key = "conversionState",
        initialValue = null,
    )
    val state = stateRecord.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    init {
        when (params) {
            is ImageVectorToXmlParams.PathSource -> convertFromPath(params.path)
            is ImageVectorToXmlParams.TextSource -> convertFromText(params.kotlinCode)
        }
    }

    private fun convertFromPath(path: Path) = viewModelScope.launch(Dispatchers.IO) {
        val ktFile = path.resolveKtFile(project)

        if (ktFile == null) {
            _events.emit("Failed to read Kotlin file")
            return@launch
        }

        val xmlOutput = parseImageVectorToXml(ktFile)

        if (xmlOutput == null) {
            _events.emit("Failed to parse ImageVector from file")
        } else {
            stateRecord.value = ImageVectorToXmlState(
                iconSource = ImageVectorSource.FileBasedIcon(path),
                xmlContent = xmlOutput,
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
            _events.emit("Failed to parse Kotlin code")
            return@launch
        }

        val xmlOutput = parseImageVectorToXml(ktFile)

        if (xmlOutput == null) {
            _events.emit("Failed to parse ImageVector from code")
        } else {
            stateRecord.value = ImageVectorToXmlState(
                iconSource = ImageVectorSource.TextBasedIcon(kotlinCode),
                xmlContent = xmlOutput,
            )
        }
    }

    fun changeIconName(name: String) = viewModelScope.launch(Dispatchers.IO) {
        val currentState = stateRecord.value ?: return@launch

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

    private suspend fun parseImageVectorToXml(ktFile: KtFile): XmlContent? {
        return withContext(Dispatchers.Default) {
            runCatching {
                readAction {
                    val irImageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)
                        ?: return@readAction null

                    val xmlCode = irImageVector.toVectorXmlString()
                    val iconName = irImageVector.name.ifEmpty { "Icon" }

                    XmlContent(
                        name = iconName,
                        xmlCode = xmlCode,
                        irImageVector = irImageVector,
                    )
                }
            }.getOrNull()
        }
    }
}
