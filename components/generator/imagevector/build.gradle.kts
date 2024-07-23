plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.components.generator.common)
    implementation(projects.components.androidxCompose)

    implementation(libs.kotlinpoet)

    testImplementation(projects.components.parser)
    testImplementation(libs.kotlin.test)
}
