package bme.schonbrunn.backend.media.dto

import org.hibernate.validator.constraints.Length

data class CreateMediaRequestDTO(
    @field:Length(min = 3, max = 255)
    val title: String,

    @field:Length(max = 500)
    val description: String,
)