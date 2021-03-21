package tech.blur.armory.presentation.common

import java.io.Serializable


/**
 * A handy container-class which can be used to store "one-shot" data into State
 * <br></br>
 * Use [Action.set] to store the value
 * <br></br>
 * Use [Action.hasAction] to check if there is an available value
 * <br></br>
 * Use [Action.get] to retrieve the value and remove it from the container
 */
class Action<T : Any> : Serializable {
    private lateinit var value: T

    var hasAction = false
        private set

    fun set(value: T) {
        this.value = value
        hasAction = true
    }

    fun get(): T {
        if (!hasAction) {
            throw IllegalStateException("No action yet")
        }
        val result = value
        hasAction = false
        return result
    }

    fun handle(block: (T) -> Unit) {
        if (hasAction) {
            block(get())

            hasAction = false
        }
    }

    operator fun invoke(block: (T) -> Unit) {
        handle(block)
    }

    object Nothing : Serializable
}