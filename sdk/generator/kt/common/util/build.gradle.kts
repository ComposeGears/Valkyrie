plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.kover)
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
    }
}
