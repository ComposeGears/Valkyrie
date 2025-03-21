plugins {
    alias(libs.plugins.kotlin.jvm)
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
    api(projects.components.generator.common)
    implementation(projects.components.ir)

    implementation(libs.kotlinpoet)

    testImplementation(projects.components.parser.svgxml)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
