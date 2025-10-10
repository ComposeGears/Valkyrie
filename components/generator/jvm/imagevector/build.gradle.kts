plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.kover)
}

sourceSets {
    test {
        resources.srcDir("$rootDir/components/test/sharedTestResources")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

dependencies {
    implementation(projects.sdk.core.extensions)
    api(projects.components.generator.jvm.poetExtensions)
    api(projects.components.generator.core)
    implementation(projects.components.ir)

    implementation(libs.kotlinpoet)

    testImplementation(projects.components.parser.unified)
    testImplementation(projects.components.test.resourceLoader)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
