package bme.schonbrunn.backend.user.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class UserCreateRequestDTO(
    @field:NotEmpty
    val username: String,
    @field:NotEmpty
    @field:Email
    val email: String,
    @field:NotEmpty
    @field:Size(min = 5)
    val password: String,
)
