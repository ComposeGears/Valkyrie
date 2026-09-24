plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.compose)
    alias(libs.plugins.valkyrie.kover)
}

kotlin {
    // https://youtrack.jetbrains.com/issue/CMP-4906
    wasmJs {
        binaries.executable()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.sdk.ir.core)

            implementation(libs.compose.ui)
        }
    }
}
