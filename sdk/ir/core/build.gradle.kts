plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.kover)
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
    }
}

tasks.withType<Jar>().configureEach {
    archiveBaseName = "sdk-ir-core"
}
