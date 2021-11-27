package bme.schonbrunn.backend.user.dto

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class UserUpdateRequestDTO(
    @field:Length(min = 3, max = 255)
    val username: String,

    @field:Email
    @field:Length(min = 3, max = 320)
    val email: String,

    @field:Size(min = 5, max = 255)
    val password: String?,
)
