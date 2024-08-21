package io.github.composegears.valkyrie.ui.domain

interface ParamUseCase<Params : Any, Success : Any> {
  suspend operator fun invoke(params: Params): Success
}
