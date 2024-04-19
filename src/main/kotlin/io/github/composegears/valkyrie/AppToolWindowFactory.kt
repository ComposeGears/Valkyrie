package io.github.composegears.valkyrie

import androidx.compose.material3.Surface
import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import io.github.composegears.valkyrie.theme.WidgetTheme
import io.github.composegears.valkyrie.ui.ValkyriePlugin
import io.github.composegears.valkyrie.ui.screen.intro.IntroViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

class AppToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        System.setProperty("compose.interop.blending", "true")
        System.setProperty("compose.swing.render.on.graphics", "true")

        startKoin {
            modules(appModule)
        }

        ApplicationManager.getApplication().invokeLater {
            toolWindow.contentManager.addContent(
                ContentFactory.getInstance().createContent(
                    ComposePanel().apply {
                        setBounds(0, 0, 800, 600)
                        setContent {
                            WidgetTheme(project) {
                                Surface {
                                    ValkyriePlugin()
                                }
                            }
                        }
                    },
                    "Valkyrie SVG to ImageVector",
                    false
                )
            )
        }
    }
}

val appModule = module {
    factoryOf(::IntroViewModel)
}