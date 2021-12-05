package bme.schonbrunn.backend.user.dto

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.Pattern

data class UserUpdateRequestDTO(
    @field:Length(min = 3, max = 255)
    val username: String,

    @field:Email
    @field:Length(min = 3, max = 320)
    val email: String,

    @field:Pattern(regexp = "(?=.*?[a-zà-öőűø-ÿ])(?=.*?[A-ZÀ-ÖŐŰØ-ß])(?=.*?[0-9])(?=.*?[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~])[\\wÀ-ÖŐŰØ-ßà-öőűø-ÿ!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]{8,255}")
    val password: String?,
)
