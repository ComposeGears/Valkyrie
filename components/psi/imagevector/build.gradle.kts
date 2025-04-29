plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jetbrains.intellij.module)
    alias(libs.plugins.kover)
}

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.ir)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
