package io.github.composegears.valkyrie.util

import com.composegears.tiamat.TiamatViewModel
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.MVIAction
import pro.respawn.flowmvi.api.MVIIntent
import pro.respawn.flowmvi.api.MVIState
import pro.respawn.flowmvi.api.Store

open class ContainerViewModel<T : Container<S, I, A>, S : MVIState, I : MVIIntent, A : MVIAction>(
    val container: T,
    start: Boolean = true,
) : TiamatViewModel(),
    Store<S, I, A> by container.store,
    Container<S, I, A> by container {

    init {
        if (start) {
            store.start(viewModelScope)
        }
    }

    override fun onClosed() {
        super.onClosed()
        store.close()
    }
}
