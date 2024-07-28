plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.kotlinpoet)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
