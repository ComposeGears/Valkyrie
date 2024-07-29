package io.github.composegears.valkyrie.generator.imagevector.spec

import androidx.compose.material.icons.generator.MemberNames
import androidx.compose.material.icons.generator.vector.VectorNode
import com.squareup.kotlinpoet.CodeBlock
import io.github.composegears.valkyrie.generator.imagevector.util.addPath

internal fun CodeBlock.Builder.addVectorNode(vectorNode: VectorNode) {
    when (vectorNode) {
        is VectorNode.Group -> {
            beginControlFlow("%M", MemberNames.Group)
            vectorNode.paths.forEach { path ->
                addVectorNode(path)
            }
            endControlFlow()
        }
        is VectorNode.Path -> {
            addPath(vectorNode) {
                vectorNode.nodes.forEach { pathNode ->
                    addStatement(pathNode.asFunctionCall())
                }
            }
        }
    }
}
