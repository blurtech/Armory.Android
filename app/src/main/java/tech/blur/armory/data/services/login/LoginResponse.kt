package tech.blur.armory.data.services.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val id: Int,
    val email: String,
    @SerialName("firstName")
    val name: String,
    @SerialName("lastName")
    val surname: String,
    @SerialName("jwt")
    val token: String
)