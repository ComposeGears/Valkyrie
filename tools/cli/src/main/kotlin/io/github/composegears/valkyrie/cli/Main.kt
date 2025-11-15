package io.github.composegears.valkyrie.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption
import io.github.composegears.valkyrie.cli.command.ChangelogCommand
import io.github.composegears.valkyrie.cli.command.IconPackCommand
import io.github.composegears.valkyrie.cli.command.SvgXmlToImageVectorCommand

fun main(vararg args: String) = ValkyrieCli()
    .subcommands(SvgXmlToImageVectorCommand(), IconPackCommand(), ChangelogCommand())
    .versionOption(BuildConfig.VERSION_NAME, names = setOf("-v", "--version"))
    .main(args)

internal class ValkyrieCli : CliktCommand(name = "valkyrie") {

    override val printHelpOnEmptyArgs: Boolean = true

    override fun run() = Unit
}
