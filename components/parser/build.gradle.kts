plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    api(projects.components.google)

    implementation(libs.android.build.tools)
    implementation(libs.kotlin.io)
}