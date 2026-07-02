plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.valkyrie.explicit.api)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.sdk.core.tree)
        }
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
    }
}
