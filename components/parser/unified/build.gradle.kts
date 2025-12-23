plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.kover)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.sdk.ir.core)

            api(libs.kotlin.io)
        }
        jvmMain.dependencies {
            implementation(projects.components.parser.jvm.svg)
            implementation(projects.components.parser.jvm.xml)
            implementation(projects.components.parser.kmp.svg)
            implementation(projects.components.parser.kmp.xml)
        }
        jvmTest {
            resources.srcDir("$rootDir/sdk/test/sharedTestResources")

            dependencies {
                implementation(projects.sdk.test.resourceLoader)

                implementation(libs.bundles.test)
            }
        }
        wasmJsMain.dependencies {
            implementation(projects.components.parser.kmp.svg)
            implementation(projects.components.parser.kmp.xml)
        }
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
    }
}
