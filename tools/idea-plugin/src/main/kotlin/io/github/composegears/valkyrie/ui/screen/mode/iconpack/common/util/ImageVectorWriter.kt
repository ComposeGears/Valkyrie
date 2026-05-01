package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.platform.ide.progress.withBackgroundProgress
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

data class ImageVectorFile(
    val directoryPath: String,
    val fileName: String,
    val content: String,
)

object ImageVectorWriter {

    @Suppress("InconsistentCommentForJavaParameter")
    suspend fun saveVectors(
        project: Project,
        files: List<ImageVectorFile>,
    ) {
        if (files.isEmpty()) return

        withBackgroundProgress(
            project = project,
            title = "Generate ImageVector",
        ) {
            val rootsToRefresh = withContext(Dispatchers.IO) {
                files
                    .groupBy { Path(it.directoryPath) }
                    .onEach { (dir, dirFiles) ->
                        ensureActive()

                        dir.createDirectories()

                        dirFiles.forEach { (_, fileName, content) ->
                            dir.resolve(fileName).writeText(content)
                        }
                    }
                    .keys
                    .toList()
            }

            LocalFileSystem.getInstance()
                .refreshNioFiles(
                    /* files = */
                    rootsToRefresh,
                    /* async = */
                    true,
                    /* recursive = */
                    true,
                    /* onFinish = */
                    null,
                )
        }
    }
}
