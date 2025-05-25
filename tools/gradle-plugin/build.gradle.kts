plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildConfig)
    `java-gradle-plugin`
}

tasks.validatePlugins {
    enableStricterValidation = true
}

val originUrl = providers
    .exec { commandLine("git", "remote", "get-url", "origin") }
    .standardOutput
    .asText
    .map { it.trim() }

gradlePlugin {
    vcsUrl = originUrl
    website = originUrl

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

buildConfig {
    generateAtSync = true
    sourceSets.getByName("test") {
        packageName = "io.github.composegears.valkyrie.gradle"
        useKotlinOutput {
            internalVisibility = true
            topLevelConstants = true
        }

        // TODO: Set up test matrix for different gradle/agp/kotlin version combos?
        buildConfigField("AGP_VERSION", libs.gradle.agp.get().version)
        buildConfigField("KOTLIN_VERSION", libs.gradle.kotlin.get().version)
        buildConfigField("GRADLE_VERSION", "8.14.1")

        fun Project.sharedTestResourcesDir(name: String) = project
            .project(projects.components.test.path)
            .layout
            .projectDirectory
            .dir("sharedTestResources/imagevector/$name")
            .asFile
            .absolutePath

        // So we can copy the shared test SVG/XML files into our test cases
        buildConfigField("SAMPLE_SVG_DIR", sharedTestResourcesDir("svg"))
        buildConfigField("SAMPLE_XML_DIR", sharedTestResourcesDir("xml"))
    }
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(gradleKotlinDsl())

    implementation(libs.gradle.agp)
    implementation(libs.gradle.kotlin)

    api(projects.components.extensions)
    api(projects.components.generator.jvm.iconpack)
    api(projects.components.generator.jvm.imagevector)
    api(projects.components.ir)
    api(projects.components.parser.unified)

    testImplementation(gradleTestKit())
    testImplementation(gradleKotlinDsl())
    testImplementation(libs.gradle.agp)
    testImplementation(libs.gradle.kotlin)
    testImplementation(libs.junit5.jupiter)
    testImplementation(libs.kotlin.test)
}
