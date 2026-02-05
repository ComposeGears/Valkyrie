package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion

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
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.ImageVectorSource
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.ImageVectorToXmlParams
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.ImageVectorToXmlState
import io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model.XmlContent
import io.github.composegears.valkyrie.util.extension.PsiKtFileFactory
import io.github.composegears.valkyrie.util.extension.resolveKtFile
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.kotlin.psi.KtFile

class ImageVectorToXmlViewModel(
    private val project: Project,
    savedState: MutableSavedState,
    params: ImageVectorToXmlParams,
) : ViewModel() {

    private val stateRecord = savedState.recordOf<ImageVectorToXmlState>(
        key = "conversionState",
        initialValue = ImageVectorToXmlState.Loading,
    )
    val state = stateRecord.asStateFlow()

    init {
        when (params) {
            is ImageVectorToXmlParams.PathSource -> convertFromPath(params.path)
            is ImageVectorToXmlParams.TextSource -> convertFromText(params.kotlinCode)
        }
    }

    private fun convertFromPath(path: Path) = viewModelScope.launch(Dispatchers.IO) {
        val ktFile = path.resolveKtFile(project)

        if (ktFile == null) {
            stateRecord.value = ImageVectorToXmlState.Error("Failed to read Kotlin file")
            return@launch
        }

        parseImageVectorToXml(ktFile)
            .onFailure {
                stateRecord.value = ImageVectorToXmlState.Error(
                    message = "Failed to parse ImageVector from file",
                    stacktrace = "Error: ${it.message}",
                )
            }
            .onSuccess {
                stateRecord.value = ImageVectorToXmlState.Content(
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
            stateRecord.value = ImageVectorToXmlState.Error("Failed to parse Kotlin code")
            return@launch
        }

        parseImageVectorToXml(ktFile)
            .onFailure {
                stateRecord.value = ImageVectorToXmlState.Error(
                    message = "Failed to parse ImageVector from code",
                    stacktrace = "Error: ${it.message}",
                )
            }
            .onSuccess {
                stateRecord.value = ImageVectorToXmlState.Content(
                    iconSource = ImageVectorSource.TextBasedIcon(kotlinCode),
                    xmlContent = it,
                )
            }
    }

    fun changeIconName(name: String) = viewModelScope.launch(Dispatchers.IO) {
        val currentState = stateRecord.value.safeAs<ImageVectorToXmlState.Content>() ?: return@launch

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
                    val iconName = irImageVector.name.ifEmpty { "Icon" }

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
