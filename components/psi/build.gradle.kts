plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
}

dependencies {
    implementation(projects.components.extensions)

    // https://plugins.jetbrains.com/docs/intellij/using-kotlin.html#kotlin-standard-library
    compileOnly(libs.kotlin.stdlib)
    implementation(compose.ui)

    implementation(libs.kotlin.embeddable)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
