plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.kover)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.generator.core)
        }
        jvmMain.dependencies {
            implementation(projects.components.generator.jvm.poetExtensions)
            implementation(libs.kotlinpoet)
        }
        commonTest {
            dependencies {
                implementation(libs.assertk)
                implementation(libs.kotlin.test)
            }
        }
    }
}
