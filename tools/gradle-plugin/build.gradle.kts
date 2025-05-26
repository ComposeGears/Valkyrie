plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
}

tasks.validatePlugins {
    // TODO: https://github.com/gradle/gradle/issues/22600
    enableStricterValidation = true
}

gradlePlugin {
    vcsUrl = "https://github.com/ComposeGears/Valkyrie"
    website = "https://github.com/ComposeGears/Valkyrie"

    plugins {
        create("valkyrie") {
            id = "io.github.composegears.valkyrie"
            displayName = name
            implementationClass = "io.github.composegears.valkyrie.gradle.ValkyrieGradlePlugin"
            description = "Generates Kotlin accessors for ImageVectors, based on input SVG files"
            tags.addAll("kotlin", "svg", "xml", "imagevector", "valkyrie")
        }
    }
}

fun Project.sharedTestResourcesDir(name: String): String = project
    .project(projects.components.test.path)
    .layout
    .projectDirectory
    .dir("sharedTestResources/imagevector/$name")
    .asFile
    .absolutePath

tasks.test {
    // So we can copy the shared test SVG/XML files into our test cases
    systemProperty("test.dir.svg", sharedTestResourcesDir("svg"))
    systemProperty("test.dir.xml", sharedTestResourcesDir("xml"))

    // TODO: Set up tests to run for different gradle versions?
    systemProperty("test.version.gradle", GradleVersion.current().version)
}

// Adapted from https://github.com/GradleUp/shadow/blob/1d7b0863fed3126bf376f11d563e9176de176cd3/build.gradle.kts#L63-L65
// Allows gradle test cases to use the same classpath as the parent build - meaning we don't need to specify versions
// when loading plugins into test projects.
val testPluginClasspath by configurations.registering {
    isCanBeResolved = true
}

tasks.pluginUnderTestMetadata {
    // Plugins used in tests could be resolved in classpath.
    pluginClasspath.from(testPluginClasspath)
}

dependencies {
    compileOnly(libs.gradle.agp.api)
    compileOnly(libs.kotlin.gradle.plugin)

    api(projects.components.extensions)
    api(projects.components.generator.jvm.iconpack)
    api(projects.components.generator.jvm.imagevector)
    api(projects.components.ir)
    api(projects.components.parser.unified)

    testPluginClasspath(libs.gradle.agp.full)
    testPluginClasspath(libs.kotlin.gradle.plugin)

    testImplementation(gradleTestKit())
    testImplementation(libs.gradle.agp.full)
    testImplementation(libs.kotlin.gradle.plugin)
    testImplementation(libs.junit5.jupiter)
    testImplementation(libs.kotlin.test)
}
