package bme.schonbrunn.backend.media.dto

import org.hibernate.validator.constraints.Length

data class CommentRequestDTO(
    @field:Length(min = 2, max = 500)
    val comment: String
)
