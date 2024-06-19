import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.gradle.dependency.check)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.intellij)
    alias(libs.plugins.kotlin.jvm)
    id("idea")
}

group = "io.github.composegears"
version = "0.0.7-SNAPSHOT"

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

    withType<DependencyUpdatesTask> {
        rejectVersionIf {
            isNonStable(candidate.version)
        }
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

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any {
        version.uppercase().contains(it)
    }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}