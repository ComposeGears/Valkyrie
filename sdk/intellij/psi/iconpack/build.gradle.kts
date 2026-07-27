import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.jetbrains.intellij.module)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.buildconfig)
}

buildConfig.sourceSets.getByName("test") {
    useKotlinOutput {
        topLevelConstants = true
    }
    packageName = "io.github.composegears.valkyrie.psi.iconpack"

    val path = project.layout.buildDirectory.dir("resources/test").get().asFile.absolutePath
    buildConfigField<String>("TEST_DATA_PATH", path)
}

tasks.test {
    systemProperty("idea.kotlin.plugin.use.k2", "true")
}

dependencies {
    implementation(projects.components.generator.core)

    testImplementation(testFixtures(projects.sdk.intellij.testFixtures))
    testImplementation(projects.sdk.test.resourceLoader)
    testImplementation(libs.assertk)
    testImplementation(kotlin("stdlib"))
    testRuntimeOnly(libs.junit.launcher)

    intellijPlatform {
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.JUnit5)
    }
}
