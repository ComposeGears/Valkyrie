import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.intellij)
    alias(libs.plugins.kotlin.jvm)
}

val pluginProperties = Properties().apply {
    load(file("${rootDir}/plugin.properties").reader())
}

group = "io.github.composegears"
version = pluginProperties.getProperty("version")

/**
 * Could not reuse repositories in settings.gradle.kts, seems this is a bug of Intellij plugin.
 */
repositories {
    google {
        mavenContent {
            includeGroupAndSubgroups("androidx")
            includeGroupAndSubgroups("com.android")
            includeGroupAndSubgroups("com.google")
        }
    }
    mavenCentral()
}

dependencies {
    implementation(projects.components.generator.iconpack)
    implementation(projects.components.generator.imagevector)
    implementation(projects.components.parser)

    compileOnly(compose.desktop.currentOs)
    implementation(compose.desktop.common)
    implementation(compose.desktop.linux_arm64)
    implementation(compose.desktop.linux_x64)
    implementation(compose.desktop.macos_arm64)
    implementation(compose.desktop.macos_x64)
    implementation(compose.desktop.windows_x64)
    implementation(compose.material3)

    implementation(libs.android.build.tools)
    implementation(libs.koin.compose)
    implementation(libs.tiamat)
    implementation(libs.tiamat.koin)
}

compose.resources {
    generateResClass = never
}

composeCompiler {
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version = "2024.1"
}

tasks {
    run {
        // workaround for https://youtrack.jetbrains.com/issue/IDEA-285839/Classpath-clash-when-using-coroutines-in-an-unbundled-IntelliJ-plugin
        buildPlugin {
            exclude { "coroutines" in it.name }
            archiveFileName = "${rootProject.name}-${version}.zip"
        }
        prepareSandbox {
            exclude { "coroutines" in it.name }
        }
    }
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    withType<KotlinCompile> {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    buildSearchableOptions {
        enabled = false
    }

    patchPluginXml {
        sinceBuild = "241"
        untilBuild = "242.*"
    }

    signPlugin {
        // chain.crt content (base64 ci)
        certificateChain = System.getenv("CERTIFICATE_CHAIN")

        // private.pem content (base64 ci)
        privateKey = System.getenv("PRIVATE_KEY")

        // PEM pass phrase
        password = System.getenv("PRIVATE_KEY_PASSWORD")
    }

    publishPlugin {
        token = System.getenv("PUBLISH_TOKEN")
    }
}