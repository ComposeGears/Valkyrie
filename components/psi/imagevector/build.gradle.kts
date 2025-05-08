import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.compose)
    alias(libs.plugins.jetbrains.intellij.module)
    alias(libs.plugins.kover)
}

sourceSets {
    test {
        resources {
            srcDir("$rootDir/components/sharedTestResources")
        }
    }
}

configurations {
    testImplementation {
        exclude(group = "org.jetbrains.kotlinx")
    }
}

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.ir)

    compileOnly(compose.runtime)

    testImplementation(compose.ui)
    testImplementation(projects.components.irCompose)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)

    // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-faq.html#junit5-test-framework-refers-to-junit4
    testRuntimeOnly(libs.junit4)

    intellijPlatform {
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.JUnit5)
    }
}
