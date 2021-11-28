package bme.schonbrunn.backend.media.dto

import bme.schonbrunn.backend.media.entity.MediaEntity
import org.hibernate.validator.constraints.Length

data class ModifyMediaDTO(
    @field:Length(min = 3, max = 255)
    val title: String,

    @field:Length(max = 500)
    val description: String,
) {
    companion object {
        fun from(mediaEntity: MediaEntity) = ModifyMediaDTO(
            title = mediaEntity.title,
            description = mediaEntity.description,
        )
    }
}
