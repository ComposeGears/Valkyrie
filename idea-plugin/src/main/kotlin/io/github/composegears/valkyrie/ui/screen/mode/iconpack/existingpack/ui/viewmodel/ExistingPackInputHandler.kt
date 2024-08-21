package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.viewmodel

import io.github.composegears.valkyrie.ui.domain.validation.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.inputhandler.BasicInputHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputFieldState

class ExistingPackInputHandler :
  BasicInputHandler(
    initialState = InputFieldState(
      iconPackName = InputState(),
      packageName = InputState(),
      nestedPacks = emptyList(),
    ),
  )
