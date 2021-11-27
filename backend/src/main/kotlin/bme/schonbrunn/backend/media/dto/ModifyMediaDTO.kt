package bme.schonbrunn.backend.media.dto

import bme.schonbrunn.backend.media.entity.MediaEntity
import java.time.OffsetDateTime

data class ModifyMediaDTO(
    val title: String,
    val description: String,
) {
    companion object {
        fun from(mediaEntity: MediaEntity) = ModifyMediaDTO(
            title = mediaEntity.title,
            description = mediaEntity.description,
        )
    }
}
