plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kover)
}

sourceSets {
    test {
        resources {
            srcDir("$rootDir/components/sharedTestResources")
        }
    }
}

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.generator.common)

    implementation(libs.kotlinpoet)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
