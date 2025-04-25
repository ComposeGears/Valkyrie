plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.components.ir)

    implementation(libs.xpp3)
}
