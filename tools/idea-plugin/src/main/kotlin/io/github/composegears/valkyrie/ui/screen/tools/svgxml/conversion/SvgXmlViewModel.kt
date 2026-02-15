package io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composegears.tiamat.navigation.MutableSavedState
import com.composegears.tiamat.navigation.asStateFlow
import com.composegears.tiamat.navigation.recordOf
import io.github.composegears.valkyrie.parser.unified.ParserType
import io.github.composegears.valkyrie.parser.unified.SvgXmlParser
import io.github.composegears.valkyrie.parser.unified.ext.toIOPath
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgSource
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlParams
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.SvgXmlState
import io.github.composegears.valkyrie.ui.screen.tools.svgxml.conversion.model.XmlContent
import java.nio.file.Path
import kotlinx.coroutines.Dispatchers
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

    init {
        when (params) {
            is SvgXmlParams.PathSource -> convertFromPath(params.path)
            is SvgXmlParams.TextSource -> convertFromText(params.svg)
        }
    }

    private fun convertFromPath(path: Path) = viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            val xmlOutput = SvgXmlParser.svgToXml(parser = ParserType.Jvm, path.toIOPath())

            XmlContent(xmlCode = xmlOutput)
        }.onFailure {
            stateRecord.value = SvgXmlState.Error(
                message = "Failed to convert SVG to XML",
                stacktrace = "Error: ${it.message}",
            )
        }.onSuccess {
            stateRecord.value = SvgXmlState.Content(
                svgSource = SvgSource.FileBasedIcon(path),
                xmlContent = it,
            )
        }
    }

    private fun convertFromText(svg: String) = viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            val xmlOutput = SvgXmlParser.svgToXml(parser = ParserType.Jvm, text = svg)

            XmlContent(xmlCode = xmlOutput)
        }.onFailure {
            stateRecord.value = SvgXmlState.Error(
                message = "Failed to convert SVG to XML",
                stacktrace = "Error: ${it.message}",
            )
        }.onSuccess {
            stateRecord.value = SvgXmlState.Content(
                svgSource = SvgSource.TextBasedIcon(svg),
                xmlContent = it,
            )
        }
    }
}
