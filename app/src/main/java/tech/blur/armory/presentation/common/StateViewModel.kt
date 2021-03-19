package tech.blur.armory.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


open class StateViewModel<TState : ViewState>(initialState: TState? = null) : ViewModel() {
    @Suppress("PropertyName")
    protected val _state = initialState?.let { MutableLiveData(it) } ?: MutableLiveData()
    val state: LiveData<TState> = _state

    protected var stateModel: TState?
        get() = _state.value
        set(value) {
            _state.value = value
        }

    protected fun requireStateModel() = stateModel ?: error("No current state")
}

/**
 * View state marker interface
 */
interface ViewState

/**
 * Stub state when ViewModel doesn't have any state
 */
object NoState : ViewState