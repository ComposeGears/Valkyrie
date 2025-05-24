import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask.FailureLevel

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.compose)
    alias(libs.plugins.jetbrains.intellij)
}

group = rootProject.providers.gradleProperty("GROUP").get()
version = rootProject.providers.gradleProperty("VERSION_NAME").get()

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.generator.jvm.iconpack)
    implementation(projects.components.generator.jvm.imagevector)
    implementation(projects.components.irCompose)
    implementation(projects.components.parser.unified)
    implementation(projects.components.psi.iconpack)
    implementation(projects.components.psi.imagevector)
    implementation(projects.compose.core)

    compileOnly(compose.desktop.currentOs) {
        exclude(group = "org.jetbrains.compose.material")
    }
    implementation(compose.desktop.common)
    implementation(compose.desktop.linux_arm64)
    implementation(compose.desktop.linux_x64)
    implementation(compose.desktop.macos_arm64)
    implementation(compose.desktop.macos_x64)
    implementation(compose.desktop.windows_x64)
    implementation(compose.material3)

    implementation(libs.android.build.tools)
    implementation(libs.highlights)
    implementation(libs.leviathan)
    implementation(libs.leviathan.compose)
    implementation(libs.tiamat)

    intellijPlatform {
        zipSigner()
    }
}

compose.resources {
    generateResClass = never
}

intellijPlatform {
    buildSearchableOptions = false
    projectName = "valkyrie-plugin"
    pluginConfiguration.ideaVersion {
        sinceBuild = "242"
        untilBuild = provider { null }
    }
    pluginVerification {
        failureLevel = listOf(
            FailureLevel.COMPATIBILITY_WARNINGS,
            FailureLevel.COMPATIBILITY_PROBLEMS,
            FailureLevel.DEPRECATED_API_USAGES,
            FailureLevel.OVERRIDE_ONLY_API_USAGES,
            FailureLevel.NON_EXTENDABLE_API_USAGES,
            FailureLevel.PLUGIN_STRUCTURE_WARNINGS,
            FailureLevel.MISSING_DEPENDENCIES,
            FailureLevel.INVALID_PLUGIN,
            FailureLevel.NOT_DYNAMIC,
        )
        ides {
            ide(IntelliJPlatformType.IntellijIdeaCommunity, "2024.2.4")
            ide(IntelliJPlatformType.IntellijIdeaCommunity, "2025.1")
        }
    }
    signing {
        // chain.crt content (base64 ci)
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        // private.pem content (base64 ci)
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        // PEM pass phrase
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }
    publishing.token = providers.environmentVariable("PUBLISH_TOKEN")
}

java {
    // IDEA 2024.2 or above requires Java 21.
    // https://plugins.jetbrains.com/docs/intellij/api-changes-list-2024.html
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks {
    // workaround for https://youtrack.jetbrains.com/issue/IDEA-285839/Classpath-clash-when-using-coroutines-in-an-unbundled-IntelliJ-plugin
    buildPlugin {
        exclude { "coroutines" in it.name }
        archiveFileName = "valkyrie-$version.zip"
    }
    prepareSandbox {
        exclude { "coroutines" in it.name }
    }
}
