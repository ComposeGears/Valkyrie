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

repositories {
    mavenCentral()
    google()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(projects.google)

    compileOnly(compose.desktop.currentOs)
    implementation(compose.desktop.macos_arm64)
    implementation(compose.desktop.macos_x64)
    implementation(compose.desktop.windows_x64)
    implementation(compose.desktop.common)
    implementation(compose.material3)

    implementation(libs.android.build.tools)
    implementation(libs.koin.compose)
    implementation(libs.kotlinpoet)
    implementation(libs.multiplatform.filepicker)
    implementation(libs.tiamat)
    implementation(libs.tiamat.koin)

    testImplementation(libs.kotlin.test)
}

compose.resources {
    generateResClass = never
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2024.1")
}

val useDebugPersistantSettings = false

tasks {
    run {
        // workaround for https://youtrack.jetbrains.com/issue/IDEA-285839/Classpath-clash-when-using-coroutines-in-an-unbundled-IntelliJ-plugin
        buildPlugin {
            exclude { "coroutines" in it.name }
            archiveFileName.set("${rootProject.name}-${version}.zip")
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
        sinceBuild.set("241")
        untilBuild.set("241.*")
    }

    signPlugin {
        // chain.crt content (base64 ci)
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))

        // private.pem content (base64 ci)
        privateKey.set(System.getenv("PRIVATE_KEY"))

        // PEM pass phrase
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}