import java.nio.file.Paths
import java.util.Properties
import kotlin.io.path.exists

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.valkyrie.abi)
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

val sharedTestResourcesDir: File =
    project(projects.components.test.path)
        .layout
        .projectDirectory
        .dir("sharedTestResources/imagevector")
        .asFile

tasks.test {
    // So we can copy the shared test SVG/XML files into our test cases
    systemProperty("test.dir.svg", sharedTestResourcesDir.resolve("svg"))
    systemProperty("test.dir.xml", sharedTestResourcesDir.resolve("xml"))

    androidHome()?.let { systemProperty("test.androidHome", it) }
    systemProperty("test.composeUi", libs.compose.ui.get().toString())

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
    compileOnly(libs.agp.api)
    compileOnly(libs.kotlin.gradle.plugin)

    api(projects.sdk.core.extensions)
    api(projects.components.generator.iconpack)
    api(projects.components.generator.jvm.imagevector)
    api(projects.components.ir)
    api(projects.components.parser.unified)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)

    testPluginClasspath(libs.agp.full)
    testPluginClasspath(libs.kotlin.gradle.plugin)
}

fun androidHome(): String? {
    val androidSdkRoot = System.getenv("ANDROID_SDK_ROOT")
    if (!androidSdkRoot.isNullOrBlank() && Paths.get(androidSdkRoot).exists()) {
        logger.info("Using ANDROID_SDK_ROOT=$androidSdkRoot")
        return androidSdkRoot
    }

    val androidHome = System.getenv("ANDROID_HOME")
    if (!androidHome.isNullOrBlank() && Paths.get(androidHome).exists()) {
        logger.info("Using ANDROID_HOME=$androidHome")
        return androidHome
    }

    val localProps = rootProject.file("local.properties")
    if (localProps.exists()) {
        val properties = Properties()
        localProps.inputStream().use { properties.load(it) }
        val sdkHome = properties.getProperty("sdk.dir")?.takeIf { it.isNotBlank() }
        if (sdkHome != null && Paths.get(sdkHome).exists()) {
            logger.info("Using local.properties sdk.dir $sdkHome")
            return sdkHome
        }
    }

    logger.warn("No Android SDK found - Android unit tests will be skipped")
    return null
}
