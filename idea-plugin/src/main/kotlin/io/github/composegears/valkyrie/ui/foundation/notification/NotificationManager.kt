package io.github.composegears.valkyrie.ui.foundation.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import io.github.composegears.valkyrie.ui.foundation.theme.LocalProject
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.time.Duration.Companion.seconds

@Composable
fun rememberNotificationManager(): NotificationManager {
  if (LocalInspectionMode.current) return NoOpNotificationManager

  val project = LocalProject.current
  return remember { NotificationManagerImpl(project) }
}

interface NotificationManager {
  fun show(message: String)
}

private object NoOpNotificationManager : NotificationManager {
  override fun show(message: String) = Unit
}

private class NotificationManagerImpl(private val project: Project) : NotificationManager {

  override fun show(message: String) {
    val notification = NotificationGroupManager.getInstance()
      .getNotificationGroup("valkyrie")
      .createNotification(content = message, type = NotificationType.INFORMATION)

    notification.notify(project)

    Timer().schedule(2.seconds.inWholeMilliseconds) {
      notification.expire()
    }
  }
}
