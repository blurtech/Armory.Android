package tech.blur.armory.domain.models

import java.io.Serializable

data class Room(
    val id: Int,
    val name: String,
    val square: Double,
    val flor: Int,
    val video: Boolean,
    val mic: Boolean,
    val led: Boolean,
    val wifi: Boolean,
    val capacity: Int,
    val nearEvents: List<Event>
) : Serializable