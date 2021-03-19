package tech.blur.armory.data.services.login

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)