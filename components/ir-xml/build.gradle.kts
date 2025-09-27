plugins {
    alias(libs.plugins.valkyrie.kmp)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.components.ir)
            implementation(projects.sdk.generator.xml)
        }
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
    }
}
