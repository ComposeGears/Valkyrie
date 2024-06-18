plugins {
    id("java")
    alias(libs.plugins.intellij.sdk)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.jvm)
    id("idea")
}

group = "io.github.composegears"
version = "1.0"

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
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
    version.set("2023.1.5")
    type.set("IC") // Target IDE Platform
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
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
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
