plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(projects.components.google)

    implementation(libs.android.build.tools)

    testImplementation(libs.kotlin.test)
}