package io.github.composegears.valkyrie.cli.ext

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat

internal fun CliktCommand.stringOption(
    vararg names: String,
    help: String,
    default: String,
) = option(names = names, help = help).default(default)

internal fun CliktCommand.booleanOption(
    vararg names: String,
    help: String,
    default: Boolean = false,
) = option(names = names, help = help).flag(default = default)

internal fun CliktCommand.intOption(
    vararg names: String,
    help: String,
    default: Int,
) = option(names = names, help = help).int().default(default)

internal fun CliktCommand.outputFormatOption() = option(
    "--output-format",
    help = "ImageVector output format, it must be backing-property or lazy-property.",
).convert {
    when (it.lowercase()) {
        "backing-property" -> OutputFormat.BackingProperty
        "lazy-property" -> OutputFormat.LazyProperty
        else -> error("Invalid output format, it must be backing-property or lazy-property.")
    }
}.default(OutputFormat.BackingProperty)
