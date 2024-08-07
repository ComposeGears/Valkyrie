plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(projects.components.androidxCompose)

    // https://plugins.jetbrains.com/docs/intellij/using-kotlin.html#kotlin-standard-library
    compileOnly(libs.kotlin.stdlib)

    implementation(libs.android.build.tools)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
