plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.cashapp.burst)
}

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.generator.common)
    implementation(projects.components.ir)

    implementation(libs.kotlinpoet)

    testImplementation(projects.components.parser.svgxml)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
