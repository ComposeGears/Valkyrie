plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.compose.core)
            implementation(projects.compose.ui)
            implementation(projects.compose.util)

            implementation(compose.material3)
            api(libs.highlights)
        }
    }
}
