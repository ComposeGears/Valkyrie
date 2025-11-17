plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.shadow)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.valkyrie.abi)
    application
}

val baseName = "valkyrie"
val versionName = cli.versions.cli.version.get()

application {
    mainClass = "io.github.composegears.valkyrie.cli.MainKt"
    applicationName = "valkyrie"
    version = versionName
}

sourceSets {
    test {
        resources.srcDir("$rootDir/components/test/sharedTestResources")
    }
}

buildConfig {
    buildConfigField("VERSION_NAME", versionName)
    packageName = "io.github.composegears.valkyrie.cli"
}

tasks.withType<Jar>().configureEach {
    archiveBaseName = baseName
    archiveVersion = versionName

    manifest {
        attributes["Main-Class"] = "io.github.composegears.valkyrie.cli.MainKt"
        attributes["Implementation-Version"] = versionName
    }
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

val buildWithR8 by tasks.registering(JavaExec::class) {
    dependsOn(tasks.installShadowDist)

    val proguardRulesFile = layout.projectDirectory.file("proguard-rules.pro").asFile
    val jar = layout.buildDirectory.file("install/valkyrie-shadow/lib/$baseName-$version-all.jar").map { it.asFile }

    inputs.files(jar, proguardRulesFile)
    outputs.file(jar)

    classpath(r8)
    mainClass = "com.android.tools.r8.R8"
    args(
        "--release",
        "--classfile",
        "--output", jar.get().path,
        "--pg-conf", proguardRulesFile.path,
        "--lib", providers.systemProperty("java.home").get(),
        jar.get().toString(),
    )
}

val buildCLI by tasks.registering(Zip::class) {
    dependsOn(buildWithR8)

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
    dependsOn(buildWithR8)
    systemProperty("CLI_PATH", layout.buildDirectory.file("install/valkyrie-shadow/bin").get().asFile.path)
}

val r8: Configuration by configurations.creating

dependencies {
    implementation(projects.components.generator.iconpack)
    implementation(projects.components.generator.jvm.imagevector)
    implementation(projects.components.parser.unified)
    implementation(projects.sdk.core.extensions)
    implementation(projects.sdk.ir.core)

    implementation(cli.clikt)
    r8(cli.r8)

    testImplementation(projects.components.test.resourceLoader)
    testImplementation(libs.bundles.test)
    testImplementation(libs.kotlin.test)
    testImplementation(cli.mockk)
    testRuntimeOnly(libs.junit.launcher)
}
