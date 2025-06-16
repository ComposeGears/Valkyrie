plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.kover)
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
    }
}
