import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.jetbrains.intellij.module)
    alias(libs.plugins.valkyrie.kover)
}

tasks.test {
    systemProperty("idea.kotlin.plugin.use.k2", "true")
}

dependencies {
    implementation(projects.components.generator.core)

    testImplementation(testFixtures(projects.sdk.intellij.testFixtures))
    testImplementation(projects.sdk.test.resourceLoader)
    testImplementation(libs.assertk)
    testRuntimeOnly(libs.junit.launcher)

    intellijPlatform {
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.JUnit5)
    }
}
