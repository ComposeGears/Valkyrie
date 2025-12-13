package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.model

import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError

sealed interface BatchAction {

    data class ImportIssues(val issues: Map<ValidationError, List<IconName>>) : BatchAction

    data object None : BatchAction
}
