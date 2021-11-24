package bme.schonbrunn.backend.configuration

import bme.schonbrunn.backend.user.exception.EmailAlreadyInUseException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
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
}