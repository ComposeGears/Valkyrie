plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.components.ir)

    implementation(libs.android.build.tools)
    implementation(libs.xpp3)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
