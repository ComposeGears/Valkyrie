import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.jetbrains.intellij.module)
    alias(libs.plugins.valkyrie.kover)
}

dependencies {
    implementation(projects.components.generator.core)

    testImplementation(testFixtures(projects.sdk.intellij.testFixtures))
    testImplementation(projects.components.test.resourceLoader)
    testImplementation(libs.assertk)

    intellijPlatform {
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.JUnit5)
    }
}
