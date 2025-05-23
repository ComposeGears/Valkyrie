package io.github.composegears.valkyrie.cli.ext

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.boolean
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path

internal fun CliktCommand.requiredStringOption(
    vararg names: String,
    help: String,
) = option(names = names, help = help).required()

internal fun CliktCommand.requiredPathOption(
    vararg names: String,
    help: String,
) = option(names = names, help = help).path().required()

internal fun CliktCommand.stringOption(
    vararg names: String,
    help: String,
    default: String,
) = option(names = names, help = help).default(default)

internal fun CliktCommand.booleanOption(
    vararg names: String,
    help: String,
    default: Boolean = false,
) = option(names = names, help = help).boolean().default(default)

internal fun CliktCommand.intOption(
    vararg names: String,
    help: String,
    default: Int,
) = option(names = names, help = help).int().default(default)
