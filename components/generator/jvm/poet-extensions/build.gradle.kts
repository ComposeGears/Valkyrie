plugins {
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.jvm)
    alias(libs.plugins.valkyrie.kover)
}

dependencies {
    implementation(libs.kotlinpoet)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
