package io.github.composegears.valkyrie.gradle.dsl

import org.gradle.api.Task
import org.gradle.api.tasks.TaskCollection

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T : Task> TaskCollection<*>.withType(): TaskCollection<T> = (this as TaskCollection<in T>).withType(T::class.java)
