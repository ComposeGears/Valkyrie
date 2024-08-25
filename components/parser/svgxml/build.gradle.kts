plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.components.extensions)
    api(projects.components.androidxCompose)

    implementation(libs.android.build.tools)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
