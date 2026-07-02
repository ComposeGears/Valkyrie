plugins {
    alias(libs.plugins.valkyrie.jvm)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.valkyrie.explicit.api)
}

dependencies {
    implementation(libs.kotlinpoet)
}
