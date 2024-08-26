plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.components.generator.common)
    implementation(projects.components.androidxCompose)

    implementation(libs.kotlinpoet)

    testImplementation(projects.components.extensions)
    testImplementation(projects.components.parser.svgxml)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
