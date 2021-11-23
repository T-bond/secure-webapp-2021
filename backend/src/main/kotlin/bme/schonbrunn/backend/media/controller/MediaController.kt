package bme.schonbrunn.backend.media.controller

import bme.schonbrunn.backend.media.service.MediaService
import org.springdoc.api.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("medias")
class MediaController(
    private val mediaService: MediaService,
) {
    @GetMapping
    fun listMediaFiles(
        @ParameterObject
        pageable: Pageable
    ) = mediaService.listMedia(pageable)

    @GetMapping("{id}")
    fun getMediaFile(@PathVariable id: Int) = ResponseEntity.ok(mediaService.getMediaFile(id))
}