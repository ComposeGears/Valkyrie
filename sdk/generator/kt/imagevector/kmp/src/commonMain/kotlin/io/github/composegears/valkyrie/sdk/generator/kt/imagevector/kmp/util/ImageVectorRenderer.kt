package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.kmp.util

import io.github.composegears.valkyrie.generator.kt.common.ir.asStatement
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.resolveIconBuilderName
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.resolvePackageName
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.kmp.common.resolveReceiverName
import io.github.composegears.valkyrie.sdk.generator.kt.util.formatFloat
import io.github.composegears.valkyrie.sdk.generator.kt.util.trimTrailingZero
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode
import io.github.composegears.valkyrie.sdk.ir.core.toPathString

internal object ImageVectorRenderer {

    fun renderBacking(config: ImageVectorGeneratorConfig, vector: IrImageVector): String = with(config) {
        val writer = CodeWriter(config.codeStyle.indentSize)
        writeHeader(writer, vector)

        val targetName = config.targetName()
        val explicit = if (config.codeStyle.useExplicitMode) "public " else ""
        val backingName = "_${config.iconName}"

        with(writer) {
            if (config.imageVector.suppressUnusedReceiverWarning && config.resolveReceiverName().isNotEmpty()) {
                line("@Suppress(\"UnusedReceiverParameter\")")
            }
            line("${explicit}val $targetName: ImageVector")
            line("${indent(1)}get() {")
            line("${indent(2)}if ($backingName != null) {")
            line("${indent(3)}return $backingName!!")
            line("${indent(2)}}")
            append("${indent(2)}$backingName = ${builderStart(config, vector, 2)}")
            writeBody(this, vector, level = 2)
            line()
            line("${indent(2)}return $backingName!!")
            line("${indent(1)}}")
            line()
            line("@Suppress(\"ObjectPropertyName\")")
            line("private var $backingName: ImageVector? = null")

            if (config.imageVector.generatePreview) {
                line()
                writePreview(this, config)
            }
        }

        return writer.toString()
    }

    fun renderLazy(config: ImageVectorGeneratorConfig, vector: IrImageVector): String = with(config) {
        val writer = CodeWriter(config.codeStyle.indentSize)
        writeHeader(writer, vector)

        val targetName = config.targetName()
        val explicit = if (config.codeStyle.useExplicitMode) "public " else ""

        with(writer) {
            if (config.imageVector.suppressUnusedReceiverWarning && config.resolveReceiverName().isNotEmpty()) {
                line("@Suppress(\"UnusedReceiverParameter\")")
            }
            line("${explicit}val $targetName: ImageVector by lazy(LazyThreadSafetyMode.NONE) {")
            append("${indent(1)}${builderStart(config, vector, 1)}")
            writeBody(this, vector, level = 1)
            line("}")

            if (config.imageVector.generatePreview) {
                line()
                writePreview(this, config)
            }
        }

        return writer.toString()
    }

    context(config: ImageVectorGeneratorConfig)
    private fun writeHeader(writer: CodeWriter, vector: IrImageVector) {
        val packageName = config.resolvePackageName()
        if (packageName.isNotEmpty()) {
            writer.line("package $packageName")
            writer.line()
        }

        val imports = collectImports(vector)
        imports.forEach(writer::line)
        if (imports.isNotEmpty()) {
            writer.line()
        }
    }

    private fun builderStart(config: ImageVectorGeneratorConfig, vector: IrImageVector, level: Int): String {
        val args = buildList {
            add("name = \"${config.resolveIconBuilderName().escapeKotlin()}\"")
            add("defaultWidth = ${vector.defaultWidth.trimTrailingZero()}.dp")
            add("defaultHeight = ${vector.defaultHeight.trimTrailingZero()}.dp")
            add("viewportWidth = ${vector.viewportWidth.formatFloat()}")
            add("viewportHeight = ${vector.viewportHeight.formatFloat()}")
            if (vector.autoMirror) {
                add("autoMirror = true")
            }
        }

        return buildString {
            append("ImageVector.Builder(\n")
            args.forEachIndexed { index, arg ->
                val comma = if (index == args.lastIndex && !config.imageVector.addTrailingComma) "" else ","
                append("${" ".repeat(config.codeStyle.indentSize * (level + 1))}$arg$comma\n")
            }
            append("${" ".repeat(config.codeStyle.indentSize * level)})")
        }
    }

    context(config: ImageVectorGeneratorConfig)
    private fun writeBody(
        writer: CodeWriter,
        vector: IrImageVector,
        level: Int,
    ) {
        with(writer) {
            if (vector.nodes.isEmpty()) {
                append(".build()")
                newLine()
                return
            }

            append(".apply {")
            newLine()
            vector.nodes.forEach { node ->
                writeVectorNode(this, node, level + 1)
            }
            line("${indent(level)}}.build()")
        }
    }

    context(config: ImageVectorGeneratorConfig)
    private fun writeVectorNode(
        writer: CodeWriter,
        node: IrVectorNode,
        level: Int,
    ) {
        when (node) {
            is IrVectorNode.IrGroup -> writeGroup(writer, node, level)
            is IrVectorNode.IrPath -> writePath(writer, node, level)
        }
    }

    context(config: ImageVectorGeneratorConfig)
    private fun writeGroup(
        writer: CodeWriter,
        node: IrVectorNode.IrGroup,
        level: Int,
    ) {
        val params = collectGroupParams(node, level, writer)
        writeBlockCall(
            writer = writer,
            level = level,
            call = "group",
            params = params,
            addTrailingComma = config.imageVector.addTrailingComma,
            indentMultilineContent = false,
        )

        node.nodes.forEach { child ->
            writeVectorNode(writer, child, level + 1)
        }
        writer.line("${writer.indent(level)}}")
    }

    context(config: ImageVectorGeneratorConfig)
    private fun writePath(
        writer: CodeWriter,
        node: IrVectorNode.IrPath,
        level: Int,
    ) {
        val params = collectPathParams(node)
        if (config.imageVector.usePathDataString) {
            writeCall(
                writer = writer,
                level = level,
                call = "addPath",
                params = params + "pathData = addPathNodes(\"${node.paths.toPathString().escapeKotlin()}\")",
                addTrailingComma = config.imageVector.addTrailingComma,
                indentMultilineContent = true,
                opensBlock = false,
                forceMultiline = true,
            )
            return
        }

        writeBlockCall(
            writer = writer,
            level = level,
            call = "path",
            params = params,
            addTrailingComma = config.imageVector.addTrailingComma,
            indentMultilineContent = true,
        )

        node.paths.forEach { pathNode ->
            writer.line("${writer.indent(level + 1)}${pathNode.asStatement()}")
        }
        writer.line("${writer.indent(level)}}")
    }

    private fun writePreview(writer: CodeWriter, config: ImageVectorGeneratorConfig) {
        with(writer) {
            line("@Preview")
            line("@Composable")
            line("private fun ${config.iconName}Preview() {")
            line("${indent(1)}Box(modifier = Modifier.padding(12.dp)) {")
            line("${indent(2)}Image(imageVector = ${config.targetName()}, contentDescription = null)")
            line("${indent(1)}}")
            line("}")
        }
    }

    private fun ImageVectorGeneratorConfig.targetName(): String {
        val receiver = resolveReceiverName()
        return if (receiver.isEmpty()) iconName else "$receiver.$iconName"
    }
}
