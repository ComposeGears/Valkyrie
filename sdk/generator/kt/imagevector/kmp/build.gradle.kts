import io.github.composegears.valkyrie.extension.because

plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.wasm.resources)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.valkyrie.explicit.api)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sdk.generator.kt.common.util)
            api(projects.sdk.generator.kt.imagevector.common) because "Shared config model"
            implementation(projects.sdk.generator.kt.common.ir)
            implementation(projects.sdk.ir.core)
        }
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
        jvmTest {
            resources.srcDir("$rootDir/sdk/test/sharedTestResources")

            dependencies {
                implementation(projects.components.parser.unified)
                implementation(projects.sdk.test.resourceLoader)
                implementation(projects.sdk.generator.kt.imagevector.testFixtures)
                implementation(libs.bundles.test)
            }
        }
    }
}
