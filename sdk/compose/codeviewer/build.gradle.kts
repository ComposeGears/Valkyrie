plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.compose)
}

kotlin {
    // https://youtrack.jetbrains.com/issue/CMP-4906
    wasmJs {
        binaries.executable()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.sdk.compose.foundation)
            implementation(projects.sdk.compose.highlightsCore)

            implementation(libs.compose.material3)
        }
    }
}
