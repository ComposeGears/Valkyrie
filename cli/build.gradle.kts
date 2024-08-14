plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.shadow)
}

val baseName = "valkyrie"
val versionName = rootProject.providers.gradleProperty("VERSION_NAME").get()
version = versionName

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
    dependsOn(tasks.jar)

    exclude(
        "**/*.kotlin_metadata",
        "**/*.kotlin_builtins",
        "**/*.kotlin_module",
        "**/module-info.class",
        "assets/**",
        "font_metrics.properties",
        "META-INF/AL2.0",
        "META-INF/DEPENDENCIES",
        "META-INF/jdom-info.xml",
        "META-INF/LGPL2.1",
        "META-INF/maven/**",
        "META-INF/native-image/**",
        "META-INF/*.version",
        "**/*.proto",
        "**/*.dex",
        "**/LICENSE**",
        "**/NOTICE**",
        "r8-version.properties",
        "migrateToAndroidx/*",
    )
}

val r8File = layout.buildDirectory.file("libs/$baseName-$version-r8.jar").map { it.asFile }
val rulesFile = project.file("src/main/proguard-rules.pro")
val r8Jar by tasks.registering(JavaExec::class) {
    dependsOn(tasks.shadowJar)

    val fatJarFile = tasks.shadowJar.get().archiveFile
    inputs.file(fatJarFile)
    inputs.file(rulesFile)
    outputs.file(r8File)

    classpath(r8)
    mainClass = com.android.tools.r8.R8::class.java.canonicalName
    args(
        "--release",
        "--classfile",
        "--output", r8File.get().path,
        "--pg-conf", rulesFile.path,
        "--lib", providers.systemProperty("java.home").get(),
        fatJarFile.get().toString(),
    )
}

val binaryFile = layout.buildDirectory.file("libs/$baseName-$version-binary.jar").map { it.asFile }
val binaryJar by tasks.registering {
    dependsOn(r8Jar)

    val r8FileProvider = layout.file(r8File)
    val binaryFileProvider = layout.file(binaryFile)
    inputs.files(r8FileProvider)
    outputs.file(binaryFileProvider)

    doLast {
        val r8File = r8FileProvider.get().asFile
        val binaryFile = binaryFileProvider.get().asFile

        binaryFile.parentFile.mkdirs()
        binaryFile.delete()
        binaryFile.writeText("#!/bin/sh\n\nexec java \$JAVA_OPTS -jar \$0 \"\$@\"\n\n")
        binaryFile.appendBytes(r8File.readBytes())

        binaryFile.setExecutable(true, false)
    }
}

tasks.test {
    dependsOn(binaryJar)
    systemProperty("CLI_PATH", binaryFile.get().absolutePath)
}

val r8: Configuration by configurations.creating

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.generator.iconpack)
    implementation(projects.components.generator.imagevector)
    implementation(projects.components.parser)
    implementation(projects.components.psi.iconpack)

    implementation(libs.clikt)
    r8(libs.r8)

    testImplementation(projects.components.extensions)
    testImplementation(projects.components.generator.imagevector)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junit.launcher)
}
