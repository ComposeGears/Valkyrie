plugins {
    `kotlin-dsl`
}

group = "io.github.composegears.valkyrie"

dependencies {
    compileOnly(libs.kotlin.compose.compiler.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.kover.plugin)
}

gradlePlugin {
    plugins {
        register("valkyrie.kmp") {
            id = "valkyrie.kmp"
            implementationClass = "KmpPlugin"
        }
        register("valkyrie.compose") {
            id = "valkyrie.compose"
            implementationClass = "ComposePlugin"
        }
        register("valkyrie.wasm.resources") {
            id = "valkyrie.wasm.resources"
            implementationClass = "WasmResourcesPlugin"
        }
        register("valkyrie.kover") {
            id = "valkyrie.kover"
            implementationClass = "KoverPlugin"
        }
        register("valkyrie.abi") {
            id = "valkyrie.abi"
            implementationClass = "AbiPlugin"
        }
    }
}
