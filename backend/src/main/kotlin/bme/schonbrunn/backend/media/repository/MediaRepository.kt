package bme.schonbrunn.backend.media.repository

import bme.schonbrunn.backend.media.entity.MediaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MediaRepository : JpaRepository<MediaEntity, Int>