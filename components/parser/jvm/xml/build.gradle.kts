plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.kover)
}

dependencies {
    implementation(projects.components.ir)
    implementation(projects.components.parser.common)
    implementation(libs.xpp3)
}

tasks.withType<Jar>().configureEach {
    archiveBaseName = "parser-jvm-xml"
}
