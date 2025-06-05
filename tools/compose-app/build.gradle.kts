import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "composeApp"

        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sdk.compose.foundation)
            implementation(projects.sdk.compose.codeviewer)
            implementation(projects.sdk.compose.highlightsCore)
            implementation(projects.sdk.compose.icons)
            implementation(projects.sdk.shared)

            implementation(libs.compose.material3)
            implementation(libs.compose.resources)
            implementation(libs.compose.ui.tooling.preview)

            implementation(kotlin("stdlib"))
            implementation(webLibs.filekit.compose)
            implementation(libs.leviathan)
            implementation(libs.tiamat)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        wasmJsMain.dependencies {
            implementation(projects.sdk.core.extensions)
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.github.composegears.valkyrie.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.github.composegears.valkyrie"
            packageVersion = "1.0.0"

            // Need for FileKit
            linux {
                modules("jdk.security.auth")
            }
        }
    }
}
