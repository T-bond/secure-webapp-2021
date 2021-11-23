package bme.schonbrunn.backend.auth.dto

data class AuthenticationRequestDTO(
    val email: String,
    val password: String,
)
