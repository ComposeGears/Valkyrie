plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.intellij)
}

group = rootProject.providers.gradleProperty("GROUP").get()
version = rootProject.providers.gradleProperty("VERSION_NAME").get()

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.generator.iconpack)
    implementation(projects.components.generator.imagevector)
    implementation(projects.components.irCompose)
    implementation(projects.components.parser.ktfile)
    implementation(projects.components.parser.svgxml)
    implementation(projects.components.psi.iconpack)
    implementation(projects.components.psi.imagevector)

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
        sinceBuild = "241"
        untilBuild = provider { null }
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
    // IDEA 2024.1 requires Java 17.
    toolchain.languageVersion = JavaLanguageVersion.of(17)
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
