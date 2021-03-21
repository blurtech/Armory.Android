package tech.blur.armory.presentation.rooms

import java.io.Serializable

data class Filter( //todo
    val mic: Boolean? = null,
    val video: Boolean? = null,
    val led: Boolean? = null,
    val wifi: Boolean? = null,
    val square: Int? = null,
    val capacity: Int? = null
) : Serializable