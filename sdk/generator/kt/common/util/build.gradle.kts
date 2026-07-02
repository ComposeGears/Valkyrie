plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.valkyrie.explicit.api)
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
    }
}
