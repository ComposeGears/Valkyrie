plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.components.extensions)

    // https://plugins.jetbrains.com/docs/intellij/using-kotlin.html#kotlin-standard-library
    compileOnly(libs.kotlin.stdlib)
    // We don't have to include kotlin-compiler-embeddable in distributions.
    compileOnly(libs.kotlin.embeddable)

    testImplementation(libs.kotlin.embeddable)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
