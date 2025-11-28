package io.github.composegears.valkyrie.gradle.dsl

import org.gradle.api.Task
import org.gradle.api.tasks.TaskCollection

internal inline fun <reified T : Task> TaskCollection<in T>.withType(): TaskCollection<T> = withType(T::class.java)
