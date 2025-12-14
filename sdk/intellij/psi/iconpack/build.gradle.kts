import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.jetbrains.intellij.module)
    alias(libs.plugins.valkyrie.kover)
}

dependencies {
    implementation(projects.sdk.core.extensions)

    testImplementation(projects.components.test.resourceLoader)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
    // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-faq.html#junit5-test-framework-refers-to-junit4
    testRuntimeOnly(libs.junit4)

    intellijPlatform {
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.JUnit5)
    }
}
