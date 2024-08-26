import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.intellij.module)
}

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.ir)
    implementation(projects.components.psi.imagevector)

    implementation(compose.ui)

    testImplementation(compose.material3)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
    // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-faq.html#junit5-test-framework-refers-to-junit4
    testRuntimeOnly(libs.junit4)

    intellijPlatform {
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.JUnit5)
    }
}
