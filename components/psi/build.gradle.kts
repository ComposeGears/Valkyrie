import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jetbrains.intellij.module)
}

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}
dependencies {
    implementation(projects.components.extensions)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
    // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-faq.html#junit5-test-framework-refers-to-junit4
    testRuntimeOnly(libs.junit4)

    intellijPlatform {
        intellijIdeaCommunity(libs.versions.idea)
        instrumentationTools()

        bundledPlugin("org.jetbrains.kotlin")

        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.JUnit5)
    }
}
