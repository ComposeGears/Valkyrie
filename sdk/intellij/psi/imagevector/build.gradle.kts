import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.valkyrie.compose)
    alias(libs.plugins.jetbrains.intellij.module)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.buildconfig)
}

sourceSets {
    test {
        resources.srcDir("$rootDir/components/test/sharedTestResources")
    }
}

buildConfig.sourceSets.getByName("test") {
    useKotlinOutput {
        topLevelConstants = true
    }
    packageName = "io.github.composegears.valkyrie.psi"

    val path = project.layout.buildDirectory.dir("resources/test").get().asFile.absolutePath
    buildConfigField<String>("TEST_DATA_PATH", path)
}

tasks.test {
    systemProperty("idea.kotlin.plugin.use.k2", "true")
}

configurations {
    testImplementation {
        exclude(group = "org.jetbrains.kotlinx")
    }
}

dependencies {
    implementation(projects.sdk.ir.core)
    implementation(projects.sdk.core.extensions)

    compileOnly(compose.runtime)

    testImplementation(testFixtures(projects.sdk.intellij.testFixtures))
    testImplementation(compose.ui)
    testImplementation(projects.sdk.ir.compose)
    testImplementation(projects.components.test.resourceLoader)
    testImplementation(libs.assertk)
    testRuntimeOnly(libs.junit.launcher)

    intellijPlatform {
        testFramework(TestFrameworkType.Platform)
        testFramework(TestFrameworkType.JUnit5)
    }
}
