package bme.schonbrunn.backend.media.dto

import bme.schonbrunn.backend.media.entity.MediaEntity

data class MediaDTO(
    val id: Int,
    val title: String,
    val description: String,
) {
    companion object {
        fun from(mediaEntity: MediaEntity) = MediaDTO(
            id = mediaEntity.id!!,
            title = mediaEntity.title,
            description = mediaEntity.description,
        )
    }
}
