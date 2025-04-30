plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.components.ir)
    implementation(projects.components.parser.common)
    implementation(libs.xpp3)
}
