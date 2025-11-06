plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.kover)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.sdk.ir.core)
        }
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
    }
}

tasks.withType<Jar>().configureEach {
    archiveBaseName = "sdk-ir-util"
}
