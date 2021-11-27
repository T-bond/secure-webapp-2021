package bme.schonbrunn.backend.media.service

import bme.schonbrunn.backend.auth.UserDetails
import bme.schonbrunn.backend.media.dto.*
import bme.schonbrunn.backend.media.entity.CommentEntity
import bme.schonbrunn.backend.media.repository.CommentRepository
import bme.schonbrunn.backend.media.repository.MediaRepository
import bme.schonbrunn.backend.user.entity.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.EntityNotFoundException
import javax.persistence.PersistenceContext
import javax.transaction.Transactional


@Service
class MediaService(
    private val mediaRepository: MediaRepository,
    private val commentsRepository: CommentRepository,
    @PersistenceContext
    private val entityManager: EntityManager
) {
    fun listMedia(pageable: Pageable) = mediaRepository.findAll(pageable).map {
        MediaDTO.from(it)
    }

    fun getMediaFile(id: Int): SingleMediaDTO = mediaRepository.findById(id).map {
        SingleMediaDTO.from(it)
    }.orElseThrow {
        EntityNotFoundException()
    }

    fun getComments(
        id: Int,
        pageable: Pageable
    ): Page<CommentDTO> {
        if (!mediaRepository.existsById(id)) {
            throw EntityNotFoundException()
        }

        return commentsRepository.findAllByMediaId(id, pageable).map {
            CommentDTO.from(it)
        }
    }

    fun createComment(id: Int, commentDto: CommentRequestDTO, authentication: Authentication) {
        val principal = authentication.principal
        if (principal !is UserDetails) {
            throw InternalError("Unsupported user details found")
        }

        if (!mediaRepository.existsById(id)) {
            throw EntityNotFoundException()
        }

        val userId = principal.userEntity.id

        commentsRepository.save(
            CommentEntity(
                comment = commentDto.comment,
                media = mediaRepository.getById(id),
                createdBy = entityManager.getReference(UserEntity::class.java, userId)
            )
        )
    }

    @Transactional
    fun modifyComment(mediaId: Int, commentId: Int, commentDto: CommentRequestDTO, authentication: Authentication) {
        val principal = authentication.principal
        if (principal !is UserDetails) {
            throw InternalError("Unsupported user details found")
        }

        val comment = commentsRepository.findById(commentId).orElseThrow {
            throw EntityNotFoundException()
        }

        if (!principal.userEntity.isAdmin && principal.userEntity.id != comment.createdBy.id) {
            throw AccessDeniedException("User can only modify their of comments")
        }

        comment.comment = commentDto.comment
    }

    @Transactional
    fun modifyMedia(id: Int, mediaDTO: ModifyMediaDTO, authentication: Authentication) {
        val principal = authentication.principal
        if (principal !is UserDetails) {
            throw InternalError("Unsupported user details found")
        }

        val media = mediaRepository.findById(id).orElseThrow {
            throw EntityNotFoundException()
        }

        if (!principal.userEntity.isAdmin && principal.userEntity.id != media.createdBy.id) {
            throw AccessDeniedException("User can only modify their of comments")
        }

        media.title = mediaDTO.title
        media.description = mediaDTO.description
    }

    fun searchMedia(searchDto: SearchRequestDTO, pageable: Pageable) =
        mediaRepository.findAllByTitleContainingIgnoreCase(searchDto.titleContains, pageable).map {
            MediaDTO.from(it)
        }

    fun deleteMediaFile(id: Int) {
        if (!mediaRepository.existsById(id)) {
            throw EntityNotFoundException()
        }

        mediaRepository.deleteById(id);
    }

    fun deleteComment(mediaId: Int, commentId: Int) {
        if (!commentsRepository.existsById(commentId)) {
            throw EntityNotFoundException()
        }

        commentsRepository.deleteById(commentId);
    }
}