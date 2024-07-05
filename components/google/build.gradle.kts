plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.components.generator.common)

    implementation(libs.kotlinpoet)
    implementation(libs.xpp3)
}