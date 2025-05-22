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
    implementation(projects.components.generator.jvm.common)

    implementation(libs.kotlinpoet)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
