package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.model

sealed interface ImageVectorToXmlAction {
    data class OnCopyInClipboard(val text: String) : ImageVectorToXmlAction
    data class OnIconNameChange(val name: String) : ImageVectorToXmlAction
}
