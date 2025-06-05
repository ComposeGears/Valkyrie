package io.github.composegears.valkyrie.flow.simple.screen.picker.ui

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.PickerResultLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.jetbrains.compose.resources.stringResource
import valkyrie.tools.compose_app.generated.resources.Res
import valkyrie.tools.compose_app.generated.resources.file_picker_title

@Composable
fun rememberIconPicker(onPick: (List<PlatformFile>) -> Unit): PickerResultLauncher {
    val title = stringResource(Res.string.file_picker_title)

    return rememberFilePickerLauncher(
        mode = FileKitMode.Multiple(),
        title = title,
        type = FileKitType.File(extensions = listOf("svg", "xml")),
        onResult = { files ->
            files?.let { onPick(files) }
        },
    )
}
