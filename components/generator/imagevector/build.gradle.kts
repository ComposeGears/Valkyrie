plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(projects.components.generator.common)
    implementation(projects.components.google)

    implementation(libs.kotlinpoet)

    testImplementation(projects.components.parser)
    testImplementation(libs.kotlin.test)
}