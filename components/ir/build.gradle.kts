plugins {
    alias(libs.plugins.valkyrie.kmp)
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(libs.assertk)
            implementation(libs.kotlin.test)
        }
    }
}
