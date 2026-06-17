import io.github.composegears.valkyrie.extension.because

plugins {
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.jvm)
    alias(libs.plugins.valkyrie.kover)
}

sourceSets {
    test {
        resources.srcDir("$rootDir/sdk/test/sharedTestResources")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

dependencies {
    implementation(projects.sdk.core.extensions)
    implementation(projects.sdk.generator.kt.common.ir)
    implementation(projects.sdk.generator.kt.common.poetExtensions)
    implementation(projects.sdk.generator.kt.common.util)
    api(projects.sdk.generator.kt.imagevector.common) because "Shared config model"
    implementation(projects.sdk.ir.core)

    implementation(libs.kotlinpoet)

    testImplementation(projects.components.parser.unified)
    testImplementation(projects.sdk.test.resourceLoader)
    testImplementation(projects.sdk.generator.kt.imagevector.testFixtures)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
