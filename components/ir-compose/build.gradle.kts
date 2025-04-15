plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
}

dependencies {
    api(projects.components.ir)

    compileOnly(libs.intellij.bundle.kotlin)

    implementation(compose.material3) {
        exclude(group = "org.jetbrains.kotlinx")
    }
    implementation(compose.ui) {
        exclude(group = "org.jetbrains.kotlinx")
    }
}
