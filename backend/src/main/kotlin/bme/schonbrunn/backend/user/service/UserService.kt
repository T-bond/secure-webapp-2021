package bme.schonbrunn.backend.user.service

import bme.schonbrunn.backend.auth.UserDetails
import bme.schonbrunn.backend.configuration.Constants
import bme.schonbrunn.backend.user.dto.OwnUserDTO
import bme.schonbrunn.backend.user.dto.UserCreateRequestDTO
import bme.schonbrunn.backend.user.dto.UserUpdateRequestDTO
import bme.schonbrunn.backend.user.entity.UserEntity
import bme.schonbrunn.backend.user.exception.EmailAlreadyInUseException
import bme.schonbrunn.backend.user.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: DelegatingPasswordEncoder
) {

    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    fun createUser(userCreateRequest: UserCreateRequestDTO) {
        val email = userCreateRequest.email
        if (userRepository.existsByEmail(email)) {
            logger.info("Duplicated email registration request: $email")
            throw EmailAlreadyInUseException()
        }

        val user = UserEntity(
            email = userCreateRequest.email,
            username = userCreateRequest.username,
            password = passwordEncoder.encode(userCreateRequest.password)
        )

        userRepository.save(user)
    }

    fun getMe(authentication: Authentication): OwnUserDTO {
        val principal = authentication.principal
        if (principal !is UserDetails) {
            throw InternalError(Constants.UNSUPPORTED_DETAILS)
        }

        logger.info("Requesting ME information")
        return OwnUserDTO.from(principal.userEntity)
    }

    fun deleteUser(id: Int, authentication: Authentication) {
        val principal = authentication.principal
        if (principal !is UserDetails) {
            throw InternalError(Constants.UNSUPPORTED_DETAILS)
        }

        if (id == principal.userEntity.id) {
            logger.info("Own user delete request: $id")
            throw AccessDeniedException("Can not delete own user")
        }

        if (!userRepository.existsById(id)) {
            logger.info("Trying to delete not existing user $id")
            throw EntityNotFoundException()
        }

        logger.info("Deleting user $id")
        userRepository.deleteById(id);
    }

    @Transactional
    fun modifyUser(id: Int, userDTO: UserUpdateRequestDTO, authentication: Authentication) {
        val principal = authentication.principal
        if (principal !is UserDetails) {
            throw InternalError(Constants.UNSUPPORTED_DETAILS)
        }

        val media = userRepository.findById(id).orElseThrow {
            logger.info("Deleting non existing user: $id")
            throw EntityNotFoundException()
        }

        if (!principal.userEntity.isAdmin && principal.userEntity.id != media.id) {
            logger.info("Trying to edit other user: $id")
            throw AccessDeniedException("User can only modify itself")
        }

        logger.info("Editing user: $id")
        media.apply {
            email = userDTO.email
            username = userDTO.username

            if (userDTO.password != null) {
                password = passwordEncoder.encode(userDTO.password)
            }
        }
    }
}