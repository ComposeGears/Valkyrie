plugins {
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.jvm)
    alias(libs.plugins.valkyrie.kover)
}

dependencies {
    testImplementation(libs.bundles.kmp.test)
}
