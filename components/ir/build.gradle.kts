plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly(libs.intellij.bundle.kotlin)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
