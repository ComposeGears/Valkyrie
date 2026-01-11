plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.sdk.compose.foundation)
            implementation(projects.compose.ui)
            implementation(projects.compose.util)

            implementation(compose.material3)
            api(libs.highlights)
        }
    }
}
