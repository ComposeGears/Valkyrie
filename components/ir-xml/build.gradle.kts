plugins {
    alias(libs.plugins.valkyrie.kmp)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.components.ir)
            implementation(projects.components.parser.unified)
        }
        commonTest.dependencies {
            implementation(libs.bundles.kmp.test)
        }
    }
}
