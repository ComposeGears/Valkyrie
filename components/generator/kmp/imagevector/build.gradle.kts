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
            api(projects.sdk.ir.core)
        }
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
        jvmTest {
            resources.srcDir("$rootDir/sdk/test/sharedTestResources")

            dependencies {
                implementation(projects.components.parser.unified)
                implementation(projects.sdk.test.resourceLoader)
                implementation(libs.bundles.test)
            }
        }
    }
}
