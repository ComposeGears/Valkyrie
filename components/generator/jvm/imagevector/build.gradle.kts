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

dependencies {
    implementation(projects.sdk.core.extensions)
    implementation(projects.sdk.ir.core)
    api(projects.components.generator.jvm.poetExtensions)
    api(projects.components.generator.core)

    implementation(libs.kotlinpoet)

    testImplementation(projects.components.parser.unified)
    testImplementation(projects.sdk.test.resourceLoader)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
