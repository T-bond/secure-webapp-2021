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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

    private val logger: Logger = LoggerFactory.getLogger(MediaService::class.java)


    fun listMedia(pageable: Pageable): Page<MediaDTO> {
        logger.info("Listing media with page: $pageable")
        return mediaRepository.findAll(pageable).map {
            1
            MediaDTO.from(it)
        }
    }

    fun getMediaFile(id: Int): SingleMediaDTO {
        val result = mediaRepository.findById(id).map {
            SingleMediaDTO.from(it)
        }.orElseThrow {
            logger.info("Getting media file info for non existing media: $id")
            EntityNotFoundException()
        }

        logger.info("Getting media file info for media: $id")
        return result
    }

    fun getComments(
        id: Int,
        pageable: Pageable
    ): Page<CommentDTO> {
        if (!mediaRepository.existsById(id)) {
            logger.info("Getting comments for non existing media: $id")
            throw EntityNotFoundException()
        }

        logger.info("Getting comments for media: $id with page: $pageable")
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
            logger.info("Creating comment for non existing media: $id")
            throw EntityNotFoundException()
        }

        val userId = principal.userEntity.id
        logger.info("Creating comment for media: $id")

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
            logger.info("Trying to modify non existing comment: $commentId")
            throw EntityNotFoundException()
        }

        if (!principal.userEntity.isAdmin && principal.userEntity.id != comment.createdBy.id) {
            logger.info("Trying to edit other users comment: $commentId")
            throw AccessDeniedException("User can only modify their of comments")
        }

        logger.info("Editing comment: $commentId")
        comment.comment = commentDto.comment
    }

    @Transactional
    fun modifyMedia(id: Int, mediaDTO: ModifyMediaDTO, authentication: Authentication) {
        val principal = authentication.principal
        if (principal !is UserDetails) {
            throw InternalError("Unsupported user details found")
        }

        val media = mediaRepository.findById(id).orElseThrow {
            logger.info("Trying to modify not existing media: $id")
            throw EntityNotFoundException()
        }

        if (!principal.userEntity.isAdmin && principal.userEntity.id != media.createdBy.id) {
            logger.info("Trying to modify other users media: $id")
            throw AccessDeniedException("User can only modify their of comments")
        }

        logger.info("Modifying media: $id")
        media.title = mediaDTO.title
        media.description = mediaDTO.description
    }

    fun searchMedia(searchDto: SearchRequestDTO, pageable: Pageable): Page<MediaDTO> {
        logger.info("Searching media: $searchDto Page: $pageable")
        return mediaRepository.findAllByTitleContainingIgnoreCase(searchDto.titleContains, pageable).map {
            MediaDTO.from(it)
        }
    }

    fun deleteMediaFile(id: Int) {
        if (!mediaRepository.existsById(id)) {
            logger.info("Trying delete non existing media: $id")
            throw EntityNotFoundException()
        }

        logger.info("Deleting media: $id")
        mediaRepository.deleteById(id);
    }

    fun deleteComment(mediaId: Int, commentId: Int) {
        if (!commentsRepository.existsById(commentId)) {
            logger.info("Trying delete non existing comment: $commentId")
            throw EntityNotFoundException()
        }

        logger.info("Deleting comment: $commentId")
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
            logger.error("Could not transfer media file to TMP store", exception)
            tmpFile.deleteOnExit()
        }

        val caff = NativeParserDriver.parse(tmpFile.absolutePath)
        if (caff?.isValid != true) {
            tmpFile.delete()
            tmpFile.deleteOnExit()

            logger.warn("Uploaded invalid CAFF file")
            throw InvalidCaffFileException("The provided CAFF file is invalid")
        }

        val UUID = UUID.randomUUID().toString()
        val finalMedia = Paths.get("$caffsStore/$UUID.caff")
        try {
            finalMedia.parent.createDirectories()
            Files.move(tmpFile.toPath(), finalMedia)
        } catch (exception: Exception) {
            logger.warn("Could not move uploaded CAFF to final destination", exception)
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

        logger.info("Saved caff file: ${savedEntity.id}")
        generatePreview(savedEntity)
    }

    private fun generatePreview(mediaEntity: MediaEntity): Boolean {
        val finalMedia = Paths.get("$caffsStore/${mediaEntity.mediaName}.caff")
        val previewPath = Paths.get("$previewsStore/${mediaEntity.mediaName}.gif")

        return try {
            previewPath.parent.createDirectories()
            val result = NativeParserDriver.preview(finalMedia.absolutePathString(), previewPath.absolutePathString())
            logger.info("Generated preview for media: ${mediaEntity.id}")
            result
        } catch (exception: Exception) {
            logger.info("Failed to generate preview for media: ${mediaEntity.id}", exception)
            false
        }
    }

    fun getCAFF(id: Int): ResponseEntity<ByteArrayResource> {
        val mediaEntity = mediaRepository.findById(id).orElseThrow {
            throw EntityNotFoundException()
        }

        val caffPath = Paths.get("$caffsStore/${mediaEntity.mediaName}.caff")
        if (!Files.exists(caffPath)) {
            logger.info("Failed to get CAFF for media not existing media: $id")
            throw EntityNotFoundException()
        }


        logger.info("Requested CAFF file for media: $id")
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
                logger.info("Requested preview which could not be generated for media: $id")
                throw EntityNotFoundException()
            }
        }

        logger.info("Returning preview for media: $id")
        val resource = ByteArrayResource(Files.readAllBytes(previewPath))
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }
}