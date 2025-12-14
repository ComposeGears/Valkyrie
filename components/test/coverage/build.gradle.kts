plugins {
    alias(libs.plugins.valkyrie.kover)
}

// include only necessary projects for the test coverage
dependencies {
    kover(projects.tools.cli)
    kover(projects.components.generator.core)
    kover(projects.components.generator.jvm.poetExtensions)
    kover(projects.components.generator.iconpack)
    kover(projects.components.generator.jvm.imagevector)
    kover(projects.components.parser.common)
    kover(projects.components.parser.jvm.svg)
    kover(projects.components.parser.jvm.xml)
    kover(projects.components.parser.kmp.xml)
    kover(projects.components.parser.unified)
    kover(projects.components.psi.imagevector)
    kover(projects.sdk.intellij.psi.iconpack)
    kover(projects.sdk.ir.compose)
    kover(projects.sdk.ir.core)
    kover(projects.sdk.ir.util)
    kover(projects.sdk.ir.xml)
}
