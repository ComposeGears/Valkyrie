plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jetbrains.intellij.module)
}

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.ir)
}
