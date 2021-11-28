package bme.schonbrunn.backend.media.controller

import bme.schonbrunn.backend.media.dto.CommentRequestDTO
import bme.schonbrunn.backend.media.dto.ModifyMediaDTO
import bme.schonbrunn.backend.media.dto.SearchRequestDTO
import bme.schonbrunn.backend.media.service.MediaService
import org.springdoc.api.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.SortDefault
import org.springframework.data.web.SortDefault.SortDefaults
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("medias")
class MediaController(
    private val mediaService: MediaService,
) {
    @GetMapping
    fun listMediaFiles(
        @ParameterObject
        @SortDefaults(
            SortDefault(sort = ["createdAt"], direction = Sort.Direction.DESC),
        )
        pageable: Pageable
    ) = mediaService.listMedia(pageable)

    @GetMapping("{id}")
    fun getMediaFile(@PathVariable id: Int) = ResponseEntity.ok(mediaService.getMediaFile(id))

    @GetMapping("{id}/comments")
    fun getComments(
        @PathVariable id: Int,
        @ParameterObject
        @SortDefaults(
            SortDefault(sort = ["createdAt"], direction = Sort.Direction.DESC),
        )
        pageable: Pageable
    ) = ResponseEntity.ok(mediaService.getComments(id, pageable))

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("{id}/comments")
    fun createComment(
        @PathVariable id: Int,
        @Valid
        commentDto: CommentRequestDTO,
        authentication: Authentication,
    ) = mediaService.createComment(id, commentDto, authentication)

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("{mediaId}/comments/{commentId}")
    fun modifyComment(
        @PathVariable
        mediaId: Int,
        @PathVariable
        commentId: Int,
        @Valid
        @RequestBody
        commentDto: CommentRequestDTO,
        authentication: Authentication,
    ) = mediaService.modifyComment(mediaId, commentId, commentDto, authentication)

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("{id}")
    fun modifyMedia(
        @PathVariable id: Int,
        @Valid
        @RequestBody
        mediaDTO: ModifyMediaDTO,
        authentication: Authentication,
    ) = mediaService.modifyMedia(id, mediaDTO, authentication)

    @GetMapping("search")
    fun searchMediaByTitle(
        @Valid
        searchDto: SearchRequestDTO,
        @ParameterObject
        pageable: Pageable
    ) = mediaService.searchMedia(searchDto, pageable)

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    fun deleteMediaFile(@PathVariable id: Int) = mediaService.deleteMediaFile(id)

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{mediaId}/comments/{commentId}")
    fun deleteComment(@PathVariable mediaId: Int, @PathVariable commentId: Int) =
        mediaService.deleteComment(mediaId, commentId)

}