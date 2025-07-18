plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.kover)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.ir)

            api(libs.kotlin.io)
        }
        jvmMain.dependencies {
            implementation(projects.components.parser.jvm.svg)
            implementation(projects.components.parser.jvm.xml)
            implementation(projects.components.parser.kmp.svg)
            implementation(projects.components.parser.kmp.xml)
        }
        jvmTest {
            resources.srcDir("$rootDir/components/test/sharedTestResources")

            dependencies {
                implementation(projects.components.test.resourceLoader)

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
