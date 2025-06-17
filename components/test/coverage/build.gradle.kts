plugins {
    alias(libs.plugins.kover)
}

kover {
    useJacoco(libs.versions.jacoco.get())
}

dependencies {
    // include only necessary dependencies for the test coverage
    kover(projects.tools.cli)
    kover(projects.components.generator.core)
    kover(projects.components.generator.jvm.poetExtensions)
    kover(projects.components.generator.iconpack)
    kover(projects.components.generator.jvm.imagevector)
    kover(projects.components.ir)
    kover(projects.components.parser.common)
    kover(projects.components.parser.jvm.svg)
    kover(projects.components.parser.jvm.xml)
    kover(projects.components.parser.kmp.xml)
    kover(projects.components.parser.unified)
    kover(projects.components.psi.iconpack)
    kover(projects.components.psi.imagevector)
}
