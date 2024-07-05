plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinpoet)
    implementation(libs.xpp3)
}