package io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlAction
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlEvent
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlEvent.CopyInClipboard
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlEvent.ExportXmlFile
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlParams
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlState
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.XmlCode
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SvgXmlViewModel(
    savedState: MutableSavedState,
    params: SvgXmlParams,
) : ViewModel() {

    private val stateRecord = savedState.recordOf<SvgXmlState>(
        key = "conversionState",
        initialValue = SvgXmlState.Loading,
    )
    val state = stateRecord.asStateFlow()

    private val _events = Channel<SvgXmlEvent>()
    val events = _events.receiveAsFlow()

    init {
        when (params) {
            is SvgXmlParams.PathSource -> convertFromPath(params.path)
            is SvgXmlParams.TextSource -> convertFromText(params.svg)
        }
    }

    fun onAction(action: SvgXmlAction) {
        val state = stateRecord.value.safeAs<SvgXmlState.Content>() ?: return

        viewModelScope.launch {
            when (action) {
                is SvgXmlAction.OnCopyInClipboard -> {
                    _events.send(CopyInClipboard(text = state.xmlCode.value))
                }
                is SvgXmlAction.OnExportFile -> {
                    _events.send(
                        ExportXmlFile(
                            fileName = "${state.fileName}.xml",
                            content = state.xmlCode.value,
                        ),
                    )
                }
                is SvgXmlAction.OnNameChange -> {
                    stateRecord.value = state.copy(fileName = action.name)
                }
                is SvgXmlAction.OnCodeChange -> {
                    stateRecord.value = state.copy(xmlCode = XmlCode(value = action.newCode))
                }
            }
        }
    }

    private fun convertFromPath(path: Path) = viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            val xmlOutput = SvgXmlParser.svgToXml(parser = ParserType.Jvm, path.toIOPath())

            XmlCode(value = xmlOutput)
        }.onFailure {
            stateRecord.value = SvgXmlState.Error(
                message = "Failed to convert SVG to XML",
                stacktrace = "Error: ${it.message}",
            )
        }.onSuccess {
            stateRecord.value = SvgXmlState.Content(
                fileName = path.nameWithoutExtension,
                xmlCode = it,
            )
        }
    }

    private fun convertFromText(svg: String) = viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            val xmlOutput = SvgXmlParser.svgToXml(parser = ParserType.Jvm, text = svg)

            XmlCode(value = xmlOutput)
        }.onFailure {
            stateRecord.value = SvgXmlState.Error(
                message = "Failed to convert SVG to XML",
                stacktrace = "Error: ${it.message}",
            )
        }.onSuccess {
            stateRecord.value = SvgXmlState.Content(
                fileName = "ic_temp",
                xmlCode = it,
            )
        }
    }
}
