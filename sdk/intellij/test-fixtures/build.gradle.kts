import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jetbrains.intellij.module)
    alias(libs.plugins.valkyrie.kover)
    `java-test-fixtures`
}

dependencies {
    testFixturesApi(libs.junit4)

    intellijPlatform {
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.JUnit5)
    }
}
