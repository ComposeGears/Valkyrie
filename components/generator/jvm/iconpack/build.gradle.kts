plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kover)
}

sourceSets {
    test {
        resources.srcDir("$rootDir/components/test/sharedTestResources")
    }
}

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.generator.jvm.poetExtensions)
    implementation(projects.components.generator.core)

    implementation(libs.kotlinpoet)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
