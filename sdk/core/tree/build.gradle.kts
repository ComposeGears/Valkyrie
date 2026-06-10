plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.explicit.api)
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
    }
}
