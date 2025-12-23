plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
}

kotlin {
    sourceSets {
        wasmJsMain.dependencies {
            implementation(libs.kotlinx.browser)
        }
    }
}
