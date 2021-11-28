package bme.schonbrunn.backend.media.service

import bme.schonbrunn.backend.auth.UserDetails
import bme.schonbrunn.backend.media.dto.*
import bme.schonbrunn.backend.media.entity.CommentEntity
import bme.schonbrunn.backend.media.entity.MediaEntity
import bme.schonbrunn.backend.media.exception.InvalidCaffFileException
import bme.schonbrunn.backend.media.repository.CommentRepository
import bme.schonbrunn.backend.media.repository.MediaRepository
import bme.schonbrunn.backend.parser.NativeParserDriver
import bme.schonbrunn.backend.user.entity.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.EntityNotFoundException
import javax.persistence.PersistenceContext
import javax.transaction.Transactional
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories


@Service
class MediaService(
    private val mediaRepository: MediaRepository,
    private val commentsRepository: CommentRepository,
    @PersistenceContext
    private val entityManager: EntityManager,
    @Value("\${backend.store.caffs:'caffs'}")
    private var caffsStore: String?,
    @Value("\${backend.store.previews:'previews'}")
    private var previewsStore: String?,
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

    @Transactional
    fun createMedia(
        mediaData: CreateMediaRequestDTO,
        multipartFile: MultipartFile,
        authentication: Authentication
    ) {
        val principal = authentication.principal
        if (principal !is UserDetails) {
            throw InternalError("Unsupported user details found")
        }

        val userId = principal.userEntity.id

        val tmpFile = File.createTempFile("tmp_caff_u${userId}_", ".caff")
        try {
            multipartFile.transferTo(tmpFile)
        } catch (exception: Exception) {
            tmpFile.deleteOnExit()
        }

        val caff = NativeParserDriver.parse(tmpFile.absolutePath)
        if (caff?.isValid != true) {
            tmpFile.delete()
            tmpFile.deleteOnExit()

            throw InvalidCaffFileException("The provided CAFF file is invalid")
        }

        val UUID = UUID.randomUUID().toString()
        val finalMedia = Paths.get("$caffsStore/$UUID.caff")
        try {
            finalMedia.parent.createDirectories()
            Files.move(tmpFile.toPath(), finalMedia)
        } catch (exception: Exception) {
            tmpFile.delete()
            tmpFile.deleteOnExit()

            throw InternalError("Error moving CAFF file to final destination")
        }

        val savedEntity = mediaRepository.save(
            MediaEntity(
                title = mediaData.title,
                description = mediaData.description,
                createdBy = entityManager.getReference(UserEntity::class.java, userId),
                mediaName = UUID,
            )
        )

        generatePreview(savedEntity)
    }

    private fun generatePreview(mediaEntity: MediaEntity): Boolean {
        val finalMedia = Paths.get("$caffsStore/${mediaEntity.mediaName}.caff")
        val previewPath = Paths.get("$previewsStore/${mediaEntity.mediaName}.gif")

        return try {
            previewPath.parent.createDirectories()
            NativeParserDriver.preview(finalMedia.absolutePathString(), previewPath.absolutePathString())
        } catch (exception: Exception) {
            false
        }
    }

    fun getCAFF(id: Int): ResponseEntity<ByteArrayResource> {
        val mediaEntity = mediaRepository.findById(id).orElseThrow {
            throw EntityNotFoundException()
        }

        val caffPath = Paths.get("$caffsStore/${mediaEntity.mediaName}.caff")
        if (!Files.exists(caffPath)) {
            throw EntityNotFoundException()
        }


        val resource = ByteArrayResource(Files.readAllBytes(caffPath))
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }

    fun getPreview(id: Int): ResponseEntity<ByteArrayResource> {
        val mediaEntity = mediaRepository.findById(id).orElseThrow {
            throw EntityNotFoundException()
        }

        val previewPath = Paths.get("$previewsStore/${mediaEntity.mediaName}.gif")
        if (!Files.exists(previewPath)) {
            generatePreview(mediaEntity) // Try to regenerate preview

            if (!Files.exists(previewPath)) {
                throw EntityNotFoundException()
            }
        }

        val resource = ByteArrayResource(Files.readAllBytes(previewPath))
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }
}