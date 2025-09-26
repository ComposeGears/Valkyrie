plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
        }
    }
}
