import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.intellij.sdk)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.jvm)
    id("idea")
}

group = "io.github.composegears"
version = "0.0.2-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.preview)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(compose.uiTooling)

    implementation(libs.androd.build.tools)
    implementation(libs.koin.compose)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlinpoet)
    implementation(libs.multiplatform.filepicker)
    implementation(libs.tiamat)
    implementation(libs.tiamat.koin)

    testImplementation(libs.kotlin.test)
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2024.1")
    plugins.set(listOf("org.jetbrains.kotlin"))
}

val useDebugPersistantSettings = false

tasks {
    run {
        // workaround for https://youtrack.jetbrains.com/issue/IDEA-285839/Classpath-clash-when-using-coroutines-in-an-unbundled-IntelliJ-plugin
        buildPlugin {
            exclude { "coroutines" in it.name }
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

    jar.configure {
        destinationDirectory.set(file("$rootDir/plugin"))
    }

    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("241.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    if (useDebugPersistantSettings) {
        runIde {
            dependsOn("copyBuildRunIdeSandbox")
        }

        register<Copy>("copyBuildRunIdeSandbox") {
            dependsOn("prepareSandbox")
            from("buildRunIdeSandbox")
            into("build/idea-sandbox")
        }
    }
}
