plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    // https://plugins.jetbrains.com/docs/intellij/using-kotlin.html#kotlin-standard-library
    compileOnly(libs.kotlin.stdlib)
}
