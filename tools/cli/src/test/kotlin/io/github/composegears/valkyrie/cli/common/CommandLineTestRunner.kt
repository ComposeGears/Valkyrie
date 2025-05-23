package io.github.composegears.valkyrie.cli.common

class CommandLineTestRunner(
    private var commands: List<String>,
) : Runnable {

    override fun run() {
        val process = ProcessBuilder(cliCommand(commands)).start()
        val exitCode = process.waitFor()

        val err = process.errorStream.readBytes().toString(Charsets.UTF_8)

        if (exitCode != 0 || err.isNotEmpty()) {
            error("Error occurred when running command line: $err")
        }
    }

    companion object {
        private val isWindows = System.getProperty("os.name").startsWith("Windows")
        private val cliPath = System.getProperty("CLI_PATH") ?: error("CLI_PATH must not be null.")

        private fun cliCommand(arguments: List<String>) = buildList {
            if (isWindows) {
                add("$cliPath/valkyrie.bat")
            } else {
                add("$cliPath/valkyrie")
            }
            addAll(arguments)
        }
    }
}
