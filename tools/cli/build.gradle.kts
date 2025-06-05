import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.shadow)
    alias(libs.plugins.kover)
    application
}

val baseName = "valkyrie"
val versionName = rootProject.providers.gradleProperty("VERSION_NAME").get()

application {
    mainClass = "io.github.composegears.valkyrie.cli.MainKt"
    applicationName = "valkyrie"
    version = versionName
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
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

val buildWithR8 by tasks.registering(JavaExec::class) {
    dependsOn(tasks.installShadowDist)

    val proguardRulesFile = layout.projectDirectory.file("proguard-rules.pro").asFile
    val jar = layout.buildDirectory.file("install/cli-shadow/lib/$baseName-$version-all.jar").map { it.asFile }

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

    from(layout.buildDirectory.file("install/cli-shadow"))

    archiveFileName.set("$baseName-cli-$version.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions/"))
}

tasks.test {
    dependsOn(buildWithR8)
    systemProperty("CLI_PATH", layout.buildDirectory.file("install/cli-shadow/bin").get().asFile.path)
}

val r8: Configuration by configurations.creating

dependencies {
    implementation(projects.components.extensions)
    implementation(projects.components.generator.iconpack)
    implementation(projects.components.generator.jvm.imagevector)
    implementation(projects.components.ir)
    implementation(projects.components.parser.unified)

    implementation(libs.clikt)
    r8(libs.r8)

    testImplementation(libs.bundles.test)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.mockk)
    testRuntimeOnly(libs.junit.launcher)
}
