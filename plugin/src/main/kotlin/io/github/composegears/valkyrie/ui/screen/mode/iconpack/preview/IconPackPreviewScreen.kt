package io.github.composegears.valkyrie.ui.screen.mode.iconpack.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.ide.CopyPasteManager
import io.github.composegears.valkyrie.theme.LocalProject
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.CopyAction
import io.github.composegears.valkyrie.ui.foundation.IntellijEditorTextField
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.screen.conversion.ConversionScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.datatransfer.StringSelection

val IconPackPreviewScreen by navDestination<Unit> {
    val navController = navController()
    val viewModel = koinTiamatViewModel<IconPackPreviewViewModel>()

    val state by viewModel.state.collectAsState()

    val project = LocalProject.current
    val scope = rememberCoroutineScope()

    IconPackPreviewScreenUI(
        content = state,
        onBack = {
            navController.back(transition = navigationSlideInOut(false))
        },
        onNext = {
            navController.editBackStack { clear() }
            navController.navigate(
                dest = ConversionScreen,
                transition = navigationSlideInOut(true)
            )
        },
        onCopy = { text ->
            CopyPasteManager.getInstance().setContents(StringSelection(text))

            scope.launch {
                val notification = NotificationGroupManager.getInstance()
                    .getNotificationGroup(/* groupId = */ "valkyrie")
                    .createNotification(content = "Copied in clipboard", type = NotificationType.INFORMATION)
                notification.notify(project)

                delay(2000)
                notification.expire()
            }
        }
    )
}

@Composable
fun IconPackPreviewScreenUI(
    content: String,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onCopy: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle(title = "Icon Pack Preview")
            WeightSpacer()
            CopyAction(onCopy = { onCopy(content) })
        }
        IntellijEditorTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            text = content
        )
        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp),
            onClick = onNext,
        ) {
            Text(text = "Next")
        }
    }
}