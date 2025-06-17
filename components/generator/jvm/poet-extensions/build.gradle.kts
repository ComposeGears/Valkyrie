plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.kover)
}

dependencies {
    implementation(libs.kotlinpoet)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
