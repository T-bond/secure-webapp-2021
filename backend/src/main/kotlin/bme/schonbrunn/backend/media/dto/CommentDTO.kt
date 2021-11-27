package bme.schonbrunn.backend.media.dto

import bme.schonbrunn.backend.media.entity.CommentEntity
import java.time.OffsetDateTime

data class CommentDTO(
    val id: Int,
    val comment: String,
    val createdAt: OffsetDateTime,
    val createdBy: CreatorDTO
) {
    companion object {
        fun from(commentEntity: CommentEntity) = CommentDTO(
            id = commentEntity.id!!,
            comment = commentEntity.comment,
            createdAt = commentEntity.createdAt,
            createdBy = CreatorDTO.from(commentEntity.createdBy),
        )
    }
}
