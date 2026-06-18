plugins {
    alias(libs.plugins.valkyrie.jvm)
    alias(libs.plugins.valkyrie.explicit.api)
}

dependencies {
    implementation(projects.sdk.generator.kt.imagevector.common)
    implementation(projects.sdk.test.resourceLoader)
}
