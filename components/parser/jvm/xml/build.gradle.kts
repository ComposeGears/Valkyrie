plugins {
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.jvm)
    alias(libs.plugins.valkyrie.kover)
}

dependencies {
    implementation(projects.sdk.ir.core)
    implementation(projects.sdk.parser.common)
    implementation(libs.xpp3) {
        exclude(group = "junit", module = "junit")
    }
}
