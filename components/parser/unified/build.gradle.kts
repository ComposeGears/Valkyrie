plugins {
    alias(libs.plugins.valkyrie.kmp)
    alias(libs.plugins.kover)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.components.ir)

            api(libs.kotlin.io)
        }
        jvmMain.dependencies {
            implementation(projects.components.parser.jvm.svg)
            implementation(projects.components.parser.jvm.xml)
            implementation(projects.components.parser.kmp.svg)
            implementation(projects.components.parser.kmp.xml)
        }
        wasmJsMain.dependencies {
            implementation(projects.components.parser.kmp.svg)
            implementation(projects.components.parser.kmp.xml)
        }
        commonTest.dependencies {
            implementation(libs.assertk)
            implementation(libs.kotlin.test)
        }
    }
}
