package io.github.composegears.valkyrie

import org.gradle.api.artifacts.Configuration
import org.gradle.kotlin.dsl.exclude

/**
 * Exclude not used libraries from `com.android.tools:sdk-common:x.x.x` artifact
 */
fun Configuration.excludeAndroidBuildTools() {
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
