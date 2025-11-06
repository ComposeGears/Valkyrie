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
            implementation(projects.sdk.ir.core)
            implementation(projects.components.parser.common)
            implementation(libs.xmlutil)
        }
        commonTest {
            dependencies {
                implementation(libs.bundles.kmp.test)
            }
        }
    }
}

tasks.withType<Jar>().configureEach {
    archiveBaseName = "parser-kmp-xml"
}
