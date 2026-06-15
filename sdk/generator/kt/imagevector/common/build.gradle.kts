plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.kover)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.sdk.generator.kt.common.iconpackTree)
        }
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
    }
}
