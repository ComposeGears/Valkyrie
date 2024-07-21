plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(projects.components.google)

    implementation(libs.android.build.tools)
    implementation(libs.kotlin.io)

    testImplementation(libs.kotlin.test)
}