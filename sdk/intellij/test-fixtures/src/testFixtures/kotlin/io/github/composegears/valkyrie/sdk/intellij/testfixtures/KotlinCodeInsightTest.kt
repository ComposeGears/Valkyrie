package io.github.composegears.valkyrie.sdk.intellij.testfixtures

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl
import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.properties.Delegates.notNull
import org.jetbrains.kotlin.psi.KtFile
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo

/**
 * Base test class for IntelliJ IDEA tests that work with Kotlin files.
 *
 * This class provides a configured [CodeInsightTestFixture] for testing Kotlin PSI operations
 * and includes utility methods for loading Kotlin files from the test resources directory.
 *
 * JUnit 5 compatible
 */
abstract class KotlinCodeInsightTest {

    private var fixture: CodeInsightTestFixture by notNull()

    private val psiManager
        get() = fixture.psiManager

    protected open val testDataPath: String = "src/test/resources"

    @BeforeEach
    internal fun setupFixture(testInfo: TestInfo) {
        val factory = IdeaTestFixtureFactory.getFixtureFactory()
        val fixtureBuilder = factory.createLightFixtureBuilder(null, testInfo.displayName)
        val lightFixture = fixtureBuilder.getFixture()

        fixture = IdeaTestFixtureFactory.getFixtureFactory()
            .createCodeInsightFixture(lightFixture, TempDirTestFixtureImpl())
        fixture.testDataPath = testDataPath
        fixture.setUp()
    }

    @AfterEach
    internal fun tearDownEdt() {
        fixture.tearDown()
    }

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
