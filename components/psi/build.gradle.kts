plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
}

dependencies {
    implementation(projects.components.extensions)

    // https://plugins.jetbrains.com/docs/intellij/using-kotlin.html#kotlin-standard-library
    compileOnly(libs.kotlin.stdlib)

    compileOnly(compose.desktop.currentOs)
    implementation(compose.desktop.common)
    implementation(compose.desktop.linux_arm64)
    implementation(compose.desktop.linux_x64)
    implementation(compose.desktop.macos_arm64)
    implementation(compose.desktop.macos_x64)
    implementation(compose.desktop.windows_x64)

    implementation(compose.ui)

    implementation(libs.kotlin.embeddable)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
