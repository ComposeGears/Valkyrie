package io.github.composegears.valkyrie.generator.kmp.imagevector.util

internal fun writeBlockCall(
    writer: CodeWriter,
    level: Int,
    call: String,
    params: List<String>,
    addTrailingComma: Boolean,
    indentMultilineContent: Boolean,
) {
    when {
        params.isEmpty() -> {
            writer.line("${writer.indent(level)}$call {")
        }
        params.size == 1 && !params.first().contains('\n') -> {
            writer.line("${writer.indent(level)}$call(${params.first()}) {")
        }
        else -> {
            writer.line("${writer.indent(level)}$call(")
            params.forEachIndexed { index, param ->
                val comma = if (index == params.lastIndex && !addTrailingComma) "" else ","
                if (param.contains('\n')) {
                    val lines = param.lines()
                    lines.forEachIndexed { lineIndex, line ->
                        val prefix = if (indentMultilineContent) writer.indent(level + 1) else if (lineIndex == 0) writer.indent(level + 1) else ""
                        writer.line("$prefix$line")
                    }
                    if (comma.isNotEmpty()) {
                        writer.line("${writer.indent(level + 1)}$comma")
                    }
                } else {
                    writer.line("${writer.indent(level + 1)}$param$comma")
                }
            }
            writer.line("${writer.indent(level)}) {")
        }
    }
}
