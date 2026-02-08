package io.github.composegears.valkyrie.ui.screen.settings.tabs.generator

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat.BackingProperty
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat.LazyProperty
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType
import io.github.composegears.valkyrie.jewel.highlight.KtCodeViewer
import io.github.composegears.valkyrie.jewel.settings.CheckboxSettingsRow
import io.github.composegears.valkyrie.jewel.settings.DropdownSettingsRow
import io.github.composegears.valkyrie.jewel.settings.Group
import io.github.composegears.valkyrie.jewel.settings.GroupSpacing
import io.github.composegears.valkyrie.jewel.settings.RadioButtonGroup
import io.github.composegears.valkyrie.jewel.settings.RadioButtonTooltipRow
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.sdk.compose.highlights.core.rememberCodeHighlight
import io.github.composegears.valkyrie.ui.screen.settings.GeneratorSettings
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateAddTrailingComma
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateExplicitMode
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateFlatPackage
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateIndentSize
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateOutputFormat
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewAnnotationType
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewGeneration
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateUseComposeColors
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateUsePathDataString
import io.github.composegears.valkyrie.ui.screen.settings.tabs.generator.ui.PreviewAnnotationSection
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer

val GeneratorSettingsScreen by navDestination {
    val viewModel = viewModel<SettingsViewModel>(viewModelStoreOwner = navController())
    val generatorSettings by viewModel.generatorSettings.collectAsState()

    GeneratorSettingsUi(
        onAction = viewModel::onAction,
        generatorSettings = generatorSettings,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GeneratorSettingsUi(
    generatorSettings: GeneratorSettings,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    VerticallyScrollableContainer(modifier = modifier) {
        Column {
            Group(text = stringResource("settings.generator.group.codestyle")) {
                CheckboxSettingsRow(
                    text = stringResource("settings.generator.explicit.mode"),
                    infoText = stringResource("settings.generator.explicit.mode.description"),
                    checked = generatorSettings.useExplicitMode,
                    onCheckedChange = { onAction(UpdateExplicitMode(it)) },
                )
                DropdownSettingsRow(
                    text = stringResource("settings.generator.indent"),
                    items = List(10) { it + 1 },
                    onSelectItem = { onAction(UpdateIndentSize(it)) },
                    current = generatorSettings.indentSize,
                    infoText = stringResource("settings.generator.indent.description"),
                    comboxModifier = Modifier.width(80.dp),
                )
            }
            GroupSpacing()

            Group(text = stringResource("settings.generator.group.imagevector.generation")) {
                RadioButtonGroup(
                    text = stringResource("settings.generator.output.header"),
                    content = {
                        RadioButtonTooltipRow(
                            text = stringResource("settings.generator.output.backing"),
                            selected = generatorSettings.outputFormat == BackingProperty,
                            onClick = { onAction(UpdateOutputFormat(BackingProperty)) },
                            tooltipContent = {
                                KtCodeViewer(
                                    highlights = rememberCodeHighlight(
                                        codeBlock = BackingPropertyHint,
                                        isDark = JewelTheme.isDark,
                                    ),
                                )
                            },
                        )
                        RadioButtonTooltipRow(
                            text = stringResource("settings.generator.output.lazy"),
                            selected = generatorSettings.outputFormat == LazyProperty,
                            onClick = { onAction(UpdateOutputFormat(LazyProperty)) },
                            tooltipContent = {
                                KtCodeViewer(
                                    highlights = rememberCodeHighlight(
                                        codeBlock = LazyDelegatePropertyHint,
                                        isDark = JewelTheme.isDark,
                                    ),
                                )
                            },
                        )
                    },
                )
                CheckboxSettingsRow(
                    text = stringResource("settings.generator.flat.package"),
                    infoText = stringResource("settings.generator.flat.package.description"),
                    checked = generatorSettings.useFlatPackage,
                    onCheckedChange = { onAction(UpdateFlatPackage(it)) },
                )
                CheckboxSettingsRow(
                    text = stringResource("settings.generator.composecolor"),
                    infoText = stringResource("settings.generator.composecolor.description"),
                    checked = generatorSettings.useComposeColors,
                    onCheckedChange = { onAction(UpdateUseComposeColors(it)) },
                )
                CheckboxSettingsRow(
                    text = stringResource("settings.generator.trailing.comma"),
                    infoText = stringResource("settings.generator.trailing.comma.description"),
                    checked = generatorSettings.addTrailingComma,
                    onCheckedChange = { onAction(UpdateAddTrailingComma(it)) },
                )
                CheckboxSettingsRow(
                    text = stringResource("settings.generator.pathdata"),
                    infoText = stringResource("settings.generator.pathdata.description"),
                    checked = generatorSettings.usePathDataString,
                    onCheckedChange = { onAction(UpdateUsePathDataString(it)) },
                )
                PreviewAnnotationSection(
                    generatePreview = generatorSettings.generatePreview,
                    previewAnnotationType = generatorSettings.previewAnnotationType,
                    onGeneratePreviewChange = { onAction(UpdatePreviewGeneration(it)) },
                    onAnnotationTypeChange = { onAction(UpdatePreviewAnnotationType(it)) },
                )
            }
            GroupSpacing()
        }
    }
}

private val BackingPropertyHint = """
    val ArrowLeft: ImageVector
        get() {
            if (_ArrowLeft != null) {
                return _ArrowLeft!!
            }
            _ArrowLeft = ImageVector.Builder(...)
                .build()

            return _ArrowLeft!!
        }

    private var _ArrowLeft: ImageVector? = null
""".trimIndent()

private val LazyDelegatePropertyHint = """
    val ArrowLeft by lazy {
        ImageVector.Builder(...)
            .build()
    }
""".trimIndent()

@Preview
@Composable
private fun GeneratorSettingsPreview() = PreviewTheme {
    var outputFormat by rememberMutableState { BackingProperty }
    var useComposeColors by rememberMutableState { true }
    var generatePreview by rememberMutableState { true }
    var useFlatPackage by rememberMutableState { true }
    var useExplicitMode by rememberMutableState { false }
    var addTrailingComma by rememberMutableState { false }
    var usePathDataString by rememberMutableState { false }
    var indentSize by rememberMutableState { 4 }
    var previewAnnotationType by rememberMutableState { PreviewAnnotationType.Jetbrains }

    val onAction = { action: SettingsAction ->
        when (action) {
            is UpdateOutputFormat -> outputFormat = action.outputFormat
            is UpdateUseComposeColors -> useComposeColors = action.useComposeColor
            is UpdatePreviewGeneration -> generatePreview = action.generate
            is UpdateFlatPackage -> useFlatPackage = action.useFlatPackage
            is UpdateExplicitMode -> useExplicitMode = action.useExplicitMode
            is UpdateAddTrailingComma -> addTrailingComma = action.addTrailingComma
            is UpdateUsePathDataString -> usePathDataString = action.usePathDataString
            is UpdateIndentSize -> indentSize = action.indent
            is UpdatePreviewAnnotationType -> previewAnnotationType = action.annotationType
            else -> {}
        }
    }
    GeneratorSettingsUi(
        onAction = onAction,
        generatorSettings = GeneratorSettings(
            outputFormat = outputFormat,
            useComposeColors = useComposeColors,
            generatePreview = generatePreview,
            useFlatPackage = useFlatPackage,
            useExplicitMode = useExplicitMode,
            addTrailingComma = addTrailingComma,
            usePathDataString = usePathDataString,
            indentSize = indentSize,
            previewAnnotationType = previewAnnotationType,
        ),
    )
}
