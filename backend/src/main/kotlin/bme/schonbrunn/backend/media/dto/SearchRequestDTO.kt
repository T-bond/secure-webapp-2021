package bme.schonbrunn.backend.media.dto

import org.hibernate.validator.constraints.Length

data class SearchRequestDTO(
    @field:Length(min = 1, max = 255)
    val titleContains: String
)
