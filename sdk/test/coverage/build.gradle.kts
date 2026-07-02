plugins {
    alias(libs.plugins.valkyrie.kover)
}

// include only necessary projects for the test coverage
dependencies {
    kover(projects.tools.cli)
    kover(projects.components.parser.jvm.svg)
    kover(projects.components.parser.jvm.xml)
    kover(projects.components.parser.kmp.xml)
    kover(projects.components.parser.unified)
    kover(projects.sdk.generator.kt.common.iconpackTree)
    kover(projects.sdk.generator.kt.common.ir)
    kover(projects.sdk.generator.kt.common.poetExtensions)
    kover(projects.sdk.generator.kt.common.util)
    kover(projects.sdk.generator.kt.iconpack)
    kover(projects.sdk.generator.kt.imagevector.common)
    kover(projects.sdk.generator.kt.imagevector.jvm)
    kover(projects.sdk.intellij.psi.iconpack)
    kover(projects.sdk.intellij.psi.imagevector)
    kover(projects.sdk.ir.compose)
    kover(projects.sdk.ir.core)
    kover(projects.sdk.ir.util)
    kover(projects.sdk.ir.xml)
    kover(projects.sdk.parser.common)
}
