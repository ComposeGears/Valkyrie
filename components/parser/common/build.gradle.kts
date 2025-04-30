plugins {
    alias(libs.plugins.valkyrie.kmp)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.ir)
        }
    }
}
