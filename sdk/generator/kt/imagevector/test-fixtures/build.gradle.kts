plugins {
    alias(libs.plugins.valkyrie.jvm)
}

dependencies {
    implementation(projects.sdk.generator.kt.imagevector.common)
    implementation(projects.sdk.test.resourceLoader)
}
