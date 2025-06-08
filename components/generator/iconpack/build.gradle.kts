plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.wasm.resources)
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
            resources.srcDir("$rootDir/components/test/sharedTestResources")

            dependencies {
                implementation(projects.components.test.resourceLoader)

                implementation(libs.assertk)
                implementation(libs.kotlin.test)
            }
        }
    }
}
