plugins {
    alias(libs.plugins.valkyrie.kmp)
}

kotlin {
    sourceSets {
        wasmJsMain.dependencies {
            implementation(libs.kotlinx.browser)
        }
    }
}
