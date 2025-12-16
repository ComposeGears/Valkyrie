plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.kover)
}

dependencies {
    implementation(libs.android.build.tools)

    testImplementation(libs.bundles.kmp.test)
}
