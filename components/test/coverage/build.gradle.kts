plugins {
    alias(libs.plugins.kover)
}

dependencies {
    // include only necessary dependencies for the test coverage
    kover(projects.cli)
    kover(projects.components.generator.common)
    kover(projects.components.generator.iconpack)
    kover(projects.components.generator.imagevector)
    kover(projects.components.ir)
    kover(projects.components.parser.common)
    kover(projects.components.parser.jvm.svg)
    kover(projects.components.parser.jvm.xml)
    kover(projects.components.parser.kmp.xml)
    kover(projects.components.parser.svgxml)
    kover(projects.components.psi.iconpack)
    kover(projects.components.psi.imagevector)
}
