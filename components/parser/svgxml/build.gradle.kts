plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.components.ir)
    implementation(projects.components.parser.jvm.svg)
    implementation(projects.components.parser.jvm.xml)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)

    compileOnly(libs.intellij.bundle.kotlin)
}
