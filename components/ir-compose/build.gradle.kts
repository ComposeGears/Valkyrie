plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.compose)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.components.ir)

            implementation(compose.material3)
            implementation(compose.ui)
        }
    }
}
