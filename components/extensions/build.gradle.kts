plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
