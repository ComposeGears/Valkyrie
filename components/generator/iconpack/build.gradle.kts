plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.components.generator.common)

    implementation(libs.kotlinpoet)

    testImplementation(libs.kotlin.test)
}