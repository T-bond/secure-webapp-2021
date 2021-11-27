package bme.schonbrunn.backend.media.repository

import bme.schonbrunn.backend.media.entity.CommentEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<CommentEntity, Int> {

    fun findAllByMediaId(id: Int, pageable: Pageable): Page<CommentEntity>

}