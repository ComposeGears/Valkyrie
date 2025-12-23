plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.wasm.resources)
    alias(libs.plugins.valkyrie.kover)
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
            resources.srcDir("$rootDir/sdk/test/sharedTestResources")

            dependencies {
                implementation(projects.sdk.test.resourceLoader)

                implementation(libs.bundles.kmp.test)
            }
        }
    }
}
