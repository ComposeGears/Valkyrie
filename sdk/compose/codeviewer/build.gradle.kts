plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.sdk.compose.foundation)
            implementation(projects.sdk.compose.highlightsCore)
            implementation(projects.compose.ui)

            implementation(compose.material3)
        }
    }
}
