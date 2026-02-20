import org.jetbrains.changelog.Changelog
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.tasks.VerifyPluginTask.FailureLevel
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.valkyrie.compose)
    alias(libs.plugins.jetbrains.intellij)
    alias(libs.plugins.jetbrains.changelog)
    alias(libs.plugins.kotlin.serialization)
}

group = "io.github.composegears"
version = ideaPluginVersions.versions.idea.plugin.version.get()

configurations.getByName("implementation") {
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")

    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core-jvm")
    exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-slf4j")

    exclude(group = "org.jetbrains.compose")
    exclude(group = "org.jetbrains.compose.foundation")
    exclude(group = "org.jetbrains.compose.desktop")
    exclude(group = "org.jetbrains.compose.runtime")
    exclude(group = "org.jetbrains.compose.ui")
    exclude(group = "org.jetbrains.compose.components")
    exclude(group = "org.jetbrains.skiko")
exclude(group = "org.jetbrains.skiko")

    exclude(group = "androidx.annotation")
    exclude(group = "androidx.lifecycle")

    // Android Build Tools
    exclude(group = "com.android.tools.analytics-library")
    exclude(group = "com.android.tools.build", module = "aapt2-proto")
    exclude(group = "com.android.tools.ddms")
    exclude(group = "com.android.tools.layoutlib")
    exclude(group = "com.android.tools", module = "sdklib")
    exclude(group = "com.google.code.gson")
    exclude(group = "com.google.guava")
    exclude(group = "com.google.protobuf")
    exclude(group = "org.apache.commons")
    exclude(group = "org.bouncycastle")
    exclude(group = "org.glassfish.jaxb")
    exclude(group = "net.java.dev.jna")
    exclude(group = "net.sf.kxml")
}

dependencies {
    implementation(projects.components.generator.iconpack)
    implementation(projects.components.generator.jvm.imagevector)
    implementation(projects.components.parser.jvm.svg)
    implementation(projects.components.parser.unified)
    implementation(projects.sdk.compose.foundation)
    implementation(projects.sdk.compose.highlightsCore)
    implementation(projects.sdk.compose.icons)
    implementation(projects.sdk.core.extensions)
    implementation(projects.sdk.intellij.psi.iconpack)
    implementation(projects.sdk.intellij.psi.imagevector)
    implementation(projects.sdk.ir.core)
    implementation(projects.sdk.ir.compose)
    implementation(projects.sdk.ir.util)
    implementation(projects.sdk.ir.xml)
    implementation(projects.sdk.shared)

    implementation(libs.android.build.tools)
    implementation(libs.androidx.collection)
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
            sinceBuild = "253"
            untilBuild = provider { null }
        }
        changeNotes = provider { changelog.render(Changelog.OutputType.HTML) }
    }
    pluginVerification {
        failureLevel = listOf(
            FailureLevel.COMPATIBILITY_WARNINGS,
            FailureLevel.COMPATIBILITY_PROBLEMS,
            FailureLevel.SCHEDULED_FOR_REMOVAL_API_USAGES,
            FailureLevel.INTERNAL_API_USAGES,
            FailureLevel.OVERRIDE_ONLY_API_USAGES,
            FailureLevel.NON_EXTENDABLE_API_USAGES,
            FailureLevel.PLUGIN_STRUCTURE_WARNINGS,
            FailureLevel.MISSING_DEPENDENCIES,
            FailureLevel.INVALID_PLUGIN,
            FailureLevel.NOT_DYNAMIC,
        )
        ides {
            create(
                type = IntelliJPlatformType.IntellijIdea,
                version = "2025.3",
            )
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
}
