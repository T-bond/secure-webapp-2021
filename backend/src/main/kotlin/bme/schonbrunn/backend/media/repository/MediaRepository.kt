package bme.schonbrunn.backend.media.repository

import bme.schonbrunn.backend.media.entity.MediaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MediaRepository : JpaRepository<MediaEntity, Int> {

    fun findAllByTitleContainingIgnoreCase(titlePart: String, pageable: Pageable): Page<MediaEntity>

}