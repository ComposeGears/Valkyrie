plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.shadow)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.valkyrie.abi)
    alias(libs.plugins.jetbrains.changelog)
    application
}

val baseName = "valkyrie"
val versionName = cli.versions.cli.version.get()

application {
    mainClass = "io.github.composegears.valkyrie.cli.MainKt"
    applicationName = "valkyrie"
    version = versionName
    // Suppress native access warnings in forked JVMs on JDK 24+
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}

sourceSets {
    main {
        resources.srcDir("$projectDir")
        resources.include("CHANGELOG.md")
    }
    test {
        resources.srcDir("$rootDir/sdk/test/sharedTestResources")
    }
}

buildConfig {
    buildConfigField("VERSION_NAME", versionName)
    packageName = "io.github.composegears.valkyrie.cli"
}

tasks.shadowJar {
    archiveBaseName = baseName
    archiveVersion = versionName

    manifest {
        attributes["Main-Class"] = "io.github.composegears.valkyrie.cli.MainKt"
        attributes["Implementation-Version"] = versionName
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    minimize {
        r8 {
            enableOptimization()
            keepRuleFiles.from(layout.projectDirectory.file("proguard-rules.pro"))
        }
    }
}

tasks.distTar {
    enabled = false
}

val buildCLI by tasks.registering(Zip::class) {
    dependsOn(tasks.installShadowDist)

    from(layout.buildDirectory.file("install/valkyrie-shadow")) {
        filesMatching("bin/valkyrie") {
            permissions {
                unix("rwxr-xr-x") // 755 in octal
            }
        }
    }

    archiveFileName.set("$baseName-cli-$version.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions/"))
}

tasks.test {
    dependsOn(tasks.installShadowDist)
    systemProperty("CLI_PATH", layout.buildDirectory.file("install/valkyrie-shadow/bin").get().asFile.path)
}

changelog {
    groups.empty()
}

configurations.getByName("implementation") {
    exclude(group = "com.android.tools.analytics-library")
    exclude(group = "com.android.tools.build", module = "aapt2-proto")
    exclude(group = "com.android.tools.ddms")
    exclude(group = "com.android.tools.layoutlib")
    exclude(group = "com.android.tools", module = "sdklib")
    exclude(group = "com.google.code.gson")
    exclude(group = "com.google.protobuf")
    exclude(group = "org.apache.commons")
    exclude(group = "org.bouncycastle")
    exclude(group = "org.glassfish.jaxb")
    exclude(group = "net.sf.kxml")
}

dependencies {
    implementation(projects.components.generator.iconpack)
    implementation(projects.components.generator.jvm.imagevector)
    implementation(projects.components.parser.unified)
    implementation(projects.sdk.core.extensions)
    implementation(projects.sdk.ir.core)

    implementation(kotlin("stdlib"))
    implementation(cli.clikt)
    implementation(cli.clikt.markdown)
    shadowR8(cli.r8)

    testImplementation(projects.sdk.test.resourceLoader)
    testImplementation(libs.bundles.test)
    testImplementation(libs.kotlin.test)
    testImplementation(cli.mockk)
    testRuntimeOnly(libs.junit.launcher)
}
