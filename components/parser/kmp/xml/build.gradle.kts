plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.ir)
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
