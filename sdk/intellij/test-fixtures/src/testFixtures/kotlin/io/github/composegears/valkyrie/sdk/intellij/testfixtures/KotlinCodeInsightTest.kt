package io.github.composegears.valkyrie.sdk.intellij.testfixtures

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.nio.file.Path
import kotlin.io.path.absolute
import org.jetbrains.kotlin.psi.KtFile
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

/**
 * Base test class for IntelliJ IDEA tests that work with Kotlin files.
 *
 * This class provides a configured functionality for testing Kotlin PSI operations
 * and includes utility methods for loading Kotlin files from the test resources directory.
 *
 * JUnit 5 compatible
 */
abstract class KotlinCodeInsightTest : BasePlatformTestCase() {

    @BeforeEach
    internal fun beforeEach() {
        super.setUp()
    }

    @AfterEach
    internal fun afterEach() {
        super.tearDown()
    }

    override fun getTestDataPath(): String = "src/test/resources"

    protected fun loadKtFile(fileName: String): KtFile {
        val path = Path.of(testDataPath, fileName).absolute()

        val virtualFile = LocalFileSystem.getInstance().findFileByPath(path.toString())
            ?: error("File not found: $fileName at $path")

        val psiFile = psiManager.findFile(virtualFile)
            ?: error("Could not create PSI file for: $fileName")

        return psiFile as? KtFile
            ?: error("File is not a Kotlin file: $fileName (type: ${psiFile::class.simpleName})")
    }
}
