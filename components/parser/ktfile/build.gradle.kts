import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.compose)
    alias(libs.plugins.jetbrains.intellij.module)
}

sourceSets {
    test {
        resources {
            srcDir("$rootDir/components/sharedTestResources")
        }
    }
}

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.irCompose)
    implementation(projects.components.psi.imagevector)

    implementation(compose.material3)
    implementation(compose.ui)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
    // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-faq.html#junit5-test-framework-refers-to-junit4
    testRuntimeOnly(libs.junit4)

    intellijPlatform {
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.JUnit5)
    }
}
