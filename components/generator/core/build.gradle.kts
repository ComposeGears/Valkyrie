plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.kover)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.sdk.ir.core)
            api(projects.sdk.core.tree)
            implementation(projects.sdk.generator.kt.common.util)
        }
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
    }
}
