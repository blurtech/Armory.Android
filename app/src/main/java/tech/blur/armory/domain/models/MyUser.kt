package tech.blur.armory.domain.models

data class MyUser(
    val id: Int,
    val email: String,
    val name: String,
    val surname: String,
    val token: String
)