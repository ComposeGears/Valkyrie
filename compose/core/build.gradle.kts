plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.foundation)
            implementation(compose.runtime)
        }
    }
}
