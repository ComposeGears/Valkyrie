package io.github.composegears.valkyrie.generator.kmp.imagevector.util

import io.github.composegears.valkyrie.generator.core.formatFloat
import io.github.composegears.valkyrie.generator.core.trimTrailingZero
import io.github.composegears.valkyrie.generator.kmp.imagevector.ImageVectorRenderConfig
import io.github.composegears.valkyrie.generator.kmp.imagevector.render.asPathDataString
import io.github.composegears.valkyrie.generator.kmp.imagevector.render.asStatement
import io.github.composegears.valkyrie.generator.kmp.imagevector.render.resolveIconBuilderName
import io.github.composegears.valkyrie.generator.kmp.imagevector.render.resolvePackageName
import io.github.composegears.valkyrie.generator.kmp.imagevector.render.resolveReceiverName
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode

internal object ImageVectorRenderer {
    fun renderBacking(config: ImageVectorRenderConfig, vector: IrImageVector): String {
        val writer = CodeWriter(config.indentSize)
        writeHeader(writer, config, vector)

        val targetName = config.targetName()
        val explicit = if (config.useExplicitMode) "public " else ""
        val backingName = "_${config.iconName}"

        with(writer) {
            line("${explicit}val $targetName: ImageVector")
            line("${indent(1)}get() {")
            line("${indent(2)}if ($backingName != null) {")
            line("${indent(3)}return $backingName!!")
            line("${indent(2)}}")
            append("${indent(2)}$backingName = ${builderStart(config, vector, 2)}")
            writeBody(this, config, vector, level = 2)
            line()
            line("${indent(2)}return $backingName!!")
            line("${indent(1)}}")
            line()
            line("@Suppress(\"ObjectPropertyName\")")
            line("private var $backingName: ImageVector? = null")

            if (config.generatePreview) {
                line()
                writePreview(this, config)
            }
        }

        return writer.toString()
    }

    fun renderLazy(config: ImageVectorRenderConfig, vector: IrImageVector): String {
        val writer = CodeWriter(config.indentSize)
        writeHeader(writer, config, vector)

        val targetName = config.targetName()
        val explicit = if (config.useExplicitMode) "public " else ""

        with(writer) {
            line("${explicit}val $targetName: ImageVector by lazy(LazyThreadSafetyMode.NONE) {")
            append("${indent(1)}${builderStart(config, vector, 1)}")
            writeBody(this, config, vector, level = 1)
            line("}")

            if (config.generatePreview) {
                line()
                writePreview(this, config)
            }
        }

        return writer.toString()
    }

    private fun writeHeader(writer: CodeWriter, config: ImageVectorRenderConfig, vector: IrImageVector) {
        val packageName = config.resolvePackageName()
        if (packageName.isNotEmpty()) {
            writer.line("package $packageName")
            writer.line()
        }

        val imports = collectImports(config, vector)
        imports.forEach(writer::line)
        if (imports.isNotEmpty()) {
            writer.line()
        }
    }

    private fun builderStart(config: ImageVectorRenderConfig, vector: IrImageVector, level: Int): String {
        val args = buildList {
            add("name = \"${config.resolveIconBuilderName()}\"")
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
                val comma = if (index == args.lastIndex && !config.addTrailingComma) "" else ","
                append("${" ".repeat(config.indentSize * (level + 1))}$arg$comma\n")
            }
            append("${" ".repeat(config.indentSize * level)})")
        }
    }

    private fun writeBody(
        writer: CodeWriter,
        config: ImageVectorRenderConfig,
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
                writeVectorNode(this, config, node, level + 1)
            }
            line("${indent(level)}}.build()")
        }
    }

    private fun writeVectorNode(
        writer: CodeWriter,
        config: ImageVectorRenderConfig,
        node: IrVectorNode,
        level: Int,
    ) {
        when (node) {
            is IrVectorNode.IrGroup -> writeGroup(writer, config, node, level)
            is IrVectorNode.IrPath -> writePath(writer, config, node, level)
        }
    }

    private fun writeGroup(
        writer: CodeWriter,
        config: ImageVectorRenderConfig,
        node: IrVectorNode.IrGroup,
        level: Int,
    ) {
        val params = collectGroupParams(node, config, level, writer)
        writeBlockCall(
            writer = writer,
            level = level,
            call = "group",
            params = params,
            addTrailingComma = config.addTrailingComma,
            indentMultilineContent = false,
        )

        node.nodes.forEach { child ->
            writeVectorNode(writer, config, child, level + 1)
        }
        writer.line("${writer.indent(level)}}")
    }

    private fun writePath(
        writer: CodeWriter,
        config: ImageVectorRenderConfig,
        node: IrVectorNode.IrPath,
        level: Int,
    ) {
        val params = collectPathParams(node, config)
        if (config.usePathDataString) {
            writeCall(
                writer = writer,
                level = level,
                call = "addPath",
                params = params + "pathData = addPathNodes(\"${node.paths.asPathDataString().escapeKotlin()}\")",
                addTrailingComma = config.addTrailingComma,
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
            addTrailingComma = config.addTrailingComma,
            indentMultilineContent = true,
        )

        node.paths.forEach { pathNode ->
            writer.line("${writer.indent(level + 1)}${pathNode.asStatement()}")
        }
        writer.line("${writer.indent(level)}}")
    }

    private fun writePreview(writer: CodeWriter, config: ImageVectorRenderConfig) {
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

    private fun ImageVectorRenderConfig.targetName(): String {
        val receiver = resolveReceiverName()
        return if (receiver.isEmpty()) iconName else "$receiver.$iconName"
    }
}
