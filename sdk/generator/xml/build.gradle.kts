plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.valkyrie.wasm.resources)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.sdk.core.xml)
            implementation(projects.sdk.ir.core)
            implementation(libs.xmlutil)
        }
        commonTest {
            resources.srcDir("$rootDir/sdk/test/sharedTestResources")

            dependencies {
                implementation(projects.sdk.test.resourceLoader)
                implementation(projects.components.parser.kmp.xml)

                implementation(libs.bundles.kmp.test)
            }
        }
    }
}

tasks.withType<Jar>().configureEach {
    archiveBaseName = "sdk-generator-xml"
}
