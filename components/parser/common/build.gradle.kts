plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.kover)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.ir)
        }
    }
}
