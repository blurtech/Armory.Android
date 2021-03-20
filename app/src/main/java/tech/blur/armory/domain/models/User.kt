package tech.blur.armory.domain.models

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
)