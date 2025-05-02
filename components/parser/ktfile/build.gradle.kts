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

// IntelliJ Platform tests must run sequentially to avoid file access conflicts.
// Error: "storage /idea-sandbox/IC-2024.2/system-test/caches/content.dat] is already opened (and not yet closed) -- can't open same file more than once"
tasks.test {
    maxParallelForks = 1
}

configurations {
    implementation {
        exclude(group = "org.jetbrains.kotlinx")
    }
}

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.irCompose)
    implementation(projects.components.psi.imagevector)

    implementation(compose.ui)
    implementation(libs.material.icons.core)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
    // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-faq.html#junit5-test-framework-refers-to-junit4
    testRuntimeOnly(libs.junit4)

    intellijPlatform {
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.JUnit5)
    }
}
