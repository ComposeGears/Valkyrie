plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
}

tasks.validatePlugins {
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

    // TODO: Set up test matrix for different gradle/agp/kotlin version combos?
    systemProperty("test.version.agp", libs.versions.agp.get().toString())
    systemProperty("test.version.kotlin", libs.versions.kotlin.get().toString())
    systemProperty("test.version.gradle", "8.14.1")
}

dependencies {
    implementation(libs.gradle.agp.api)
    implementation(libs.kotlin.gradle.plugin)

    api(projects.components.extensions)
    api(projects.components.generator.jvm.iconpack)
    api(projects.components.generator.jvm.imagevector)
    api(projects.components.ir)
    api(projects.components.parser.unified)

    testImplementation(gradleTestKit())
    testImplementation(libs.gradle.agp.full)
    testImplementation(libs.kotlin.gradle.plugin)
    testImplementation(libs.junit5.jupiter)
    testImplementation(libs.kotlin.test)
}
