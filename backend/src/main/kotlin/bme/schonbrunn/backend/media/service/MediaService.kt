package bme.schonbrunn.backend.media.service

import bme.schonbrunn.backend.media.dto.MediaDTO
import bme.schonbrunn.backend.media.repository.MediaRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException


@Service
class MediaService(
    private val mediaRepository: MediaRepository,
) {
    fun listMedia(pageable: Pageable) = mediaRepository.findAll(pageable).map {
        MediaDTO.from(it)
    }

    fun getMediaFile(id: Int): MediaDTO {
        val optionalMedia = mediaRepository.findById(id)
        val media = optionalMedia.orElseThrow {
            EntityNotFoundException()
        }
        return MediaDTO.from(media)
    }

}