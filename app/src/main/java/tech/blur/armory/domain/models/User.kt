package tech.blur.armory.domain.models

data class User(
    val id: Int,
    val email: String,
    val name: String,
    val surname: String,
    val token: String
)