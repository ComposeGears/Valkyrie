plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinpoet)

    testImplementation(libs.kotlin.test)
}