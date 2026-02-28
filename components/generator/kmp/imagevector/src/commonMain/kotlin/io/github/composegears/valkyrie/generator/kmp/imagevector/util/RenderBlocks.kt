package io.github.composegears.valkyrie.generator.kmp.imagevector.util

internal fun writeBlockCall(
    writer: CodeWriter,
    level: Int,
    call: String,
    params: List<String>,
    addTrailingComma: Boolean,
    indentMultilineContent: Boolean,
) {
    writeCall(
        writer = writer,
        level = level,
        call = call,
        params = params,
        addTrailingComma = addTrailingComma,
        indentMultilineContent = indentMultilineContent,
        opensBlock = true,
        forceMultiline = false,
    )
}

internal fun writeCall(
    writer: CodeWriter,
    level: Int,
    call: String,
    params: List<String>,
    addTrailingComma: Boolean,
    indentMultilineContent: Boolean,
    opensBlock: Boolean,
    forceMultiline: Boolean,
) {
    val callSuffix = if (opensBlock) " {" else ""
    when {
        params.isEmpty() -> {
            val noArgCall = if (opensBlock) call else "$call()"
            writer.line("${writer.indent(level)}$noArgCall$callSuffix")
        }
        params.size == 1 && !params.first().contains('\n') && !forceMultiline -> {
            writer.line("${writer.indent(level)}$call(${params.first()})$callSuffix")
        }
        else -> {
            writer.line("${writer.indent(level)}$call(")
            params.forEachIndexed { index, param ->
                val comma = if (index == params.lastIndex && !addTrailingComma) "" else ","
                if (param.contains('\n')) {
                    val lines = param.lines()
                    lines.forEachIndexed { lineIndex, line ->
                        val prefix = when {
                            indentMultilineContent -> writer.indent(level + 1)
                            lineIndex == 0 -> writer.indent(level + 1)
                            else -> ""
                        }
                        val lineComma = if (lineIndex == lines.lastIndex) comma else ""
                        writer.line("$prefix$line$lineComma")
                    }
                } else {
                    writer.line("${writer.indent(level + 1)}$param$comma")
                }
            }
            writer.line("${writer.indent(level)})$callSuffix")
        }
    }
}
