package io.github.composegears.valkyrie.compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.structuralEqualityPolicy

@Composable
inline fun <T> rememberMutableState(
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy(),
    crossinline calculation: @DisallowComposableCalls () -> T,
) = remember { mutableStateOf(calculation(), policy) }

@Composable
inline fun <T> rememberMutableState(
    key1: Any?,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy(),
    crossinline calculation: @DisallowComposableCalls () -> T,
) = remember(key1) { mutableStateOf(calculation(), policy) }

@Composable
inline fun <T> rememberMutableState(
    key1: Any?,
    key2: Any?,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy(),
    crossinline calculation: @DisallowComposableCalls () -> T,
) = remember(key1, key2) { mutableStateOf(calculation(), policy) }

@Composable
inline fun rememberMutableIntState(
    key1: Any?,
    crossinline calculation: @DisallowComposableCalls () -> Int,
) = remember(key1) { mutableIntStateOf(calculation()) }
