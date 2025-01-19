plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
}

dependencies {
    api(projects.components.ir)

    implementation(compose.material3)
    implementation(compose.ui)
}
