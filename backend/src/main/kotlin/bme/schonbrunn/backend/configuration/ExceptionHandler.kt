package bme.schonbrunn.backend.configuration

import bme.schonbrunn.backend.media.exception.EmailAlreadyInUseException
import bme.schonbrunn.backend.user.exception.InvalidCaffFileException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException
import javax.persistence.EntityNotFoundException

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFoundException(exception: Exception): ResponseEntity<Any> {
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler(EmailAlreadyInUseException::class)
    fun handleEmailAlreadyInUseException(exception: Exception): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.CONFLICT).build()
    }

    @ExceptionHandler(InvalidCaffFileException::class)
    fun handleInvalidCaffFileException(exception: Exception): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSizeExceededException(exception: Exception): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build()
    }

}