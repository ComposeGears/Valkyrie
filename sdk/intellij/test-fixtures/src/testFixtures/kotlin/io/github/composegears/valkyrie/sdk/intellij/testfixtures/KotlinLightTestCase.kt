package io.github.composegears.valkyrie.sdk.intellij.testfixtures

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import org.jetbrains.kotlin.psi.KtFile

/**
 * Base test class for IntelliJ IDEA tests that work with Kotlin files.
 * Extends [com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase] and provides utility methods
 * for loading and working with Kotlin PSI files.
 */
abstract class KotlinLightTestCase : LightPlatformCodeInsightFixture4TestCase() {

    override fun getTestDataPath(): String = "src/test/resources"

    /**
     * Loads a Kotlin file from the test resources directory.
     *
     * @param fileName The name of the file to load (relative to [getTestDataPath])
     * @return The loaded [org.jetbrains.kotlin.psi.KtFile] PSI element
     * @throws IllegalStateException if the file is not found
     */
    protected fun loadKtFile(fileName: String): KtFile {
        val kotlinFile = LocalFileSystem.getInstance().findFileByPath("$testDataPath/$fileName")
            ?: error("File not found: $fileName")

        return psiManager.findFile(kotlinFile) as KtFile
    }
}
