import io.github.composegears.valkyrie.task.CheckComposeVersionCompatibility
import org.jetbrains.changelog.Changelog
import org.jetbrains.intellij.platform.gradle.extensions.excludeKotlinStdlib
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask.FailureLevel
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.compose)
    alias(libs.plugins.jetbrains.intellij)
    alias(libs.plugins.jetbrains.changelog)
    alias(libs.plugins.kotlin.serialization)
}

group = rootProject.providers.gradleProperty("GROUP").get()
version = rootProject.providers.gradleProperty("VERSION_NAME").get()

configurations.all {
    // Exclude Compose Multiplatform dependencies not needed in IntelliJ plugin
    //  exclude("androidx.annotation")
    //  exclude("androidx.arch.core")
    //  exclude("androidx.compose")
    //  exclude("androidx.lifecycle")
    //  exclude("org.jetbrains.compose")
    //  exclude("org.jetbrains.compose.foundation")
    //  exclude("org.jetbrains.compose.runtime")
    //  exclude("org.jetbrains.compose.ui")
    //  exclude("org.jetbrains.jewel")
    //  exclude("org.jetbrains.kotlin")
    //  exclude("org.jetbrains.kotlinx")
    exclude("org.jetbrains.skiko")

    exclude(group = "net.sourceforge.plantuml") // PlantUML (~20MB) - from dev.snipme:highlights

    // Exclude kotlinx-serialization-json-io to prevent conflicts with IntelliJ's bundled libraries
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io")
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-serialization-json-io-jvm")
}

configurations.all {
    // Exclude desktop preview
    exclude(group = "org.jetbrains.compose.ui", module = "ui-tooling-preview-desktop")
}

dependencies {
    implementation(projects.components.generator.iconpack)
    implementation(projects.components.generator.jvm.imagevector)
    implementation(projects.components.parser.unified)
    implementation(projects.compose.icons)
    implementation(projects.compose.ui)
    implementation(projects.compose.util)
    implementation(projects.sdk.compose.codeviewer)
    implementation(projects.sdk.compose.highlightsCore)
    implementation(projects.sdk.compose.foundation) {
        excludeKotlinStdlib()
        excludeCompose()
    }
    implementation(projects.sdk.core.extensions)
    implementation(projects.sdk.intellij.psi.iconpack)
    implementation(projects.sdk.intellij.psi.imagevector)
    implementation(projects.sdk.ir.core)
    implementation(projects.sdk.ir.compose)
    implementation(projects.sdk.ir.util)
    implementation(projects.sdk.ir.xml)
    implementation(projects.shared)

    implementation(compose.material3)
    implementation(compose.preview)

    implementation(libs.android.build.tools)
    implementation(libs.fonticons)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.serialization.json)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.fuzzysearch)
    implementation(libs.leviathan)
    implementation(libs.leviathan.compose)
    implementation(libs.tiamat)

    testImplementation(libs.bundles.kmp.test)
    testImplementation(libs.junit4)

    // Support for Compose Preview in IntelliJ
    compileOnly(libs.compose.ui.tooling.preview)

    intellijPlatform {
        zipSigner()
        pluginVerifier()
    }
}

compose.resources {
    generateResClass = never
}

intellijPlatform {
    buildSearchableOptions = false
    projectName = "valkyrie-plugin"
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "252"
            untilBuild = provider { null }
        }
        changeNotes = provider { changelog.render(Changelog.OutputType.HTML) }
    }
    pluginVerification {
        failureLevel = listOf(
            FailureLevel.COMPATIBILITY_WARNINGS,
            FailureLevel.COMPATIBILITY_PROBLEMS,
            FailureLevel.OVERRIDE_ONLY_API_USAGES,
            FailureLevel.NON_EXTENDABLE_API_USAGES,
            FailureLevel.PLUGIN_STRUCTURE_WARNINGS,
            FailureLevel.MISSING_DEPENDENCIES,
            FailureLevel.INVALID_PLUGIN,
            FailureLevel.NOT_DYNAMIC,
        )
        ides {
            recommended()
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

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

changelog {
    groups.empty()
    repositoryUrl = "https://github.com/ComposeGears/Valkyrie"
    versionPrefix = ""
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
    val checkComposeVersionCompatibility by registering(CheckComposeVersionCompatibility::class) {
        artifactCollection = configurations.runtimeClasspath.map {
            it.incoming.artifactView { lenient(true) }.artifacts
        }
        expectedComposeVersion = libs.versions.compose
    }
    check {
        dependsOn(checkComposeVersionCompatibility)
    }
}


fun ModuleDependency.excludeCompose() {
    exclude("org.jetbrains.compose")
    exclude("org.jetbrains.compose.foundation")
    exclude("org.jetbrains.compose.runtime")
    exclude("org.jetbrains.compose.ui")
}