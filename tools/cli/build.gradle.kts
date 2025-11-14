plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.shadow)
    alias(libs.plugins.valkyrie.kover)
    alias(libs.plugins.valkyrie.abi)
    `maven-publish`
    signing
    application
}

java {
    withSourcesJar()
    withJavadocJar()
}

val baseName = "valkyrie"
val versionName = cli.versions.cli.version.get()

application {
    mainClass = "io.github.composegears.valkyrie.cli.MainKt"
    applicationName = "valkyrie"
    version = versionName
}

shadow {
    addShadowVariantIntoJavaComponent = false
}

sourceSets {
    main {
        resources.srcDir("$projectDir")
        resources.include("CHANGELOG.md")
    }
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
    archiveClassifier = ""
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.distTar {
    enabled = false
}

val buildWithR8 by tasks.registering(JavaExec::class) {
    dependsOn(tasks.installShadowDist)

    val proguardRulesFile = layout.projectDirectory.file("proguard-rules.pro").asFile
    val jar = layout.buildDirectory.file("install/valkyrie-shadow/lib/$baseName-$version.jar").map { it.asFile }

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
    implementation(cli.clikt.markdown)
    r8(cli.r8)

    testImplementation(projects.components.test.resourceLoader)
    testImplementation(libs.bundles.test)
    testImplementation(libs.kotlin.test)
    testImplementation(cli.mockk)
    testRuntimeOnly(libs.junit.launcher)
}

// TODO: Migrate to gradle-maven-publish-plugin after https://github.com/vanniktech/gradle-maven-publish-plugin/issues/1123
publishing {
    publications {
        create<MavenPublication>("shadow") {
            groupId = "io.github.composegears.valkyrie"
            artifactId = "valkyrie-cli"
            version = versionName

            from(components["shadow"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name = "Valkyrie CLI"
                description = "CLI tool for converting SVG/XML to Compose ImageVector and managing icon packs"
                url = "https://github.com/ComposeGears/Valkyrie"

                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/composegears/valkyrie.git"
                    developerConnection = "scm:git:ssh://github.com:composegears/valkyrie.git"
                    url = "https://github.com/ComposeGears/Valkyrie"
                }

                developers {
                    developer {
                        id = "composegears"
                        name = "ComposeGears"
                        url = "https://github.com/ComposeGears"
                    }
                }
            }
        }
    }
}

signing {
    val signingKey = System.getenv("PGP_KEY")
    val signingPassword = System.getenv("PGP_PASS")

    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey.replace("|", "\n"), signingPassword)
        sign(publishing.publications["shadow"])
    }
}
