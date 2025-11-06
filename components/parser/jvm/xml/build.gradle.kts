plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.kover)
}

dependencies {
    implementation(projects.components.parser.common)
    implementation(projects.sdk.ir.core)
    implementation(libs.xpp3)
}

tasks.withType<Jar>().configureEach {
    archiveBaseName = "parser-jvm-xml"
}
