plugins {
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.jvm)
    alias(libs.plugins.valkyrie.kover)
}

dependencies {
    implementation(projects.components.parser.common)
    implementation(projects.sdk.ir.core)
    implementation(libs.xpp3) {
        exclude(group = "junit", module = "junit")
    }
}
