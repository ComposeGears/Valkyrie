plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.kotlinpoet)

    testImplementation(libs.kotlin.test)
}