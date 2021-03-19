package tech.blur.armory.data.services.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val id: Int,
    val email: String,
    val name: String,
    val surname: String,
    val token: String
)