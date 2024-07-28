plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.components.extensions)

    // https://plugins.jetbrains.com/docs/intellij/using-kotlin.html#kotlin-standard-library
    compileOnly(libs.kotlin.stdlib)

    implementation(libs.kotlin.embeddable)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
