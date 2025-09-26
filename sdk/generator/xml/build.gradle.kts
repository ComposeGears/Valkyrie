plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.sdk.core.xml)
            implementation(projects.components.ir)
        }
    }
}

tasks.withType<Jar>().configureEach {
    archiveBaseName = "sdk-generator-xml"
}
