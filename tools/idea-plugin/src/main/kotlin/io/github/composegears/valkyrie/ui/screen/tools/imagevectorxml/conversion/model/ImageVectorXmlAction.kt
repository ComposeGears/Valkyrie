package io.github.composegears.valkyrie.ui.screen.tools.imagevectorxml.conversion.model

sealed interface ImageVectorXmlAction {
    data class OnCopyInClipboard(val text: String) : ImageVectorXmlAction
    data class OnIconNameChange(val name: String) : ImageVectorXmlAction
}
