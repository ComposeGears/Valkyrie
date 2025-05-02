package io.github.composegears.valkyrie.parser.ktfile.common

import com.intellij.openapi.project.Project
import com.intellij.testFramework.ProjectExtension
import org.junit.jupiter.api.extension.RegisterExtension

open class BaseKtParserTest {

    companion object {
        @JvmField
        @RegisterExtension
        val projectExtension = ProjectExtension()
    }

    protected val project: Project
        get() = projectExtension.project
}
