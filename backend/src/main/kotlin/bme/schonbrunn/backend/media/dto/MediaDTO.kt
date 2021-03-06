package bme.schonbrunn.backend.media.dto

import bme.schonbrunn.backend.media.entity.MediaEntity
import java.time.OffsetDateTime

data class MediaDTO(
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: OffsetDateTime,
    val createdBy: CreatorDTO
) {
    companion object {
        fun from(mediaEntity: MediaEntity) = MediaDTO(
            id = mediaEntity.id!!,
            title = mediaEntity.title,
            description = mediaEntity.description,
            createdAt = mediaEntity.createdAt,
            createdBy = CreatorDTO.from(mediaEntity.createdBy),
        )
    }
}
