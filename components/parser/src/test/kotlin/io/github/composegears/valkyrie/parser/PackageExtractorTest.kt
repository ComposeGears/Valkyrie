package io.github.composegears.valkyrie.parser

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class PackageExtractorTest {

  @Test
  fun `test package name extraction`() {
    val testData = listOf(
      PackageTest(
        path = "Work/plugin/Valkyrie/playground/app/src/main/kotlin/io/github/composegears/valkyrie/playground/icons/lazy",
        expectedPackage = "io.github.composegears.valkyrie.playground.icons.lazy",
      ),
      PackageTest(
        path = "app/src/main/java/io/github/composegears/valkyrie/playground/icons/lazy/",
        expectedPackage = "io.github.composegears.valkyrie.playground.icons.lazy",
      ),
      PackageTest(
        path = "src/main/kotlin/io/github/composegears/valkyrie/core",
        expectedPackage = "io.github.composegears.valkyrie.core",
      ),
      PackageTest(
        path = "src/main/java/com/example/project/module",
        expectedPackage = "com.example.project.module",
      ),
      PackageTest(
        path = "/src/main/java/com/example/project/module/",
        expectedPackage = "com.example.project.module",
      ),
      PackageTest(
        path = "src/main/java/com/example/project/module",
        expectedPackage = "com.example.project.module",
      ),
      PackageTest(
        path = "//src/main/java/com/example/project/module/",
        expectedPackage = "com.example.project.module",
      ),
      PackageTest(
        path = "",
        expectedPackage = null,
      ),
      PackageTest(
        path = "invalid/path/without/package",
        expectedPackage = null,
      ),
      PackageTest(
        path = "nested/directory/structure/io/github/composegears/valkyrie/deep/nest",
        expectedPackage = null,
      ),
    )

    testData.forEach { test ->
      val extractedPackage = PackageExtractor.getFrom(test.path)
      assertThat(extractedPackage).isEqualTo(test.expectedPackage)
    }
  }
}

private data class PackageTest(
  val path: String,
  val expectedPackage: String?,
)
