plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.intellij)
}

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.generator.iconpack)
    implementation(projects.components.generator.imagevector)
    implementation(projects.components.parser)
    implementation(projects.components.psi)

    compileOnly(compose.desktop.currentOs)
    implementation(compose.desktop.common)
    implementation(compose.desktop.linux_arm64)
    implementation(compose.desktop.linux_x64)
    implementation(compose.desktop.macos_arm64)
    implementation(compose.desktop.macos_x64)
    implementation(compose.desktop.windows_x64)
    implementation(compose.material3)

    implementation(libs.android.build.tools)
    implementation(libs.highlights)
    implementation(libs.koin.compose)
    implementation(libs.tiamat.base)
    implementation(libs.tiamat.koin)

    // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html#setting-up-intellij-platform
    intellijPlatform {
        intellijIdeaCommunity(libs.versions.idea)
        instrumentationTools()
        zipSigner()

        // dependency plugin id for https://plugins.jetbrains.com/plugin/6954-kotlin
        bundledPlugin(libs.kotlin.stdlib.map(Dependency::getGroup))
    }
}

compose.resources {
    generateResClass = never
}

intellijPlatform {
    buildSearchableOptions = false
    pluginConfiguration.ideaVersion {
        sinceBuild = "241"
        untilBuild = "242.*"
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
