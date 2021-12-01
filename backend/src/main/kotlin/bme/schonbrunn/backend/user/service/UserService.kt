package bme.schonbrunn.backend.user.service

import bme.schonbrunn.backend.auth.UserDetails
import bme.schonbrunn.backend.configuration.WebSecurityConfiguration
import bme.schonbrunn.backend.user.dto.OwnUserDTO
import bme.schonbrunn.backend.user.dto.UserCreateRequestDTO
import bme.schonbrunn.backend.user.dto.UserUpdateRequestDTO
import bme.schonbrunn.backend.user.entity.UserEntity
import bme.schonbrunn.backend.user.exception.EmailAlreadyInUseException
import bme.schonbrunn.backend.user.repository.UserRepository
import org.hibernate.validator.internal.util.Contracts.assertTrue
import org.passay.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: DelegatingPasswordEncoder,
    @Autowired
    private val webSecurityConfiguration: WebSecurityConfiguration
) {

    fun createUser(userCreateRequest: UserCreateRequestDTO) {
        val email = userCreateRequest.email
        if (userRepository.existsByEmail(email)) {
            throw EmailAlreadyInUseException()
        }

        assertTrue(webSecurityConfiguration.passwordValidator().validate(PasswordData(userCreateRequest.password)).isValid,
            "Error: password does not meet the complexity requirements")

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
            throw InternalError("Unsupported user details found")
        }

        return OwnUserDTO.from(principal.userEntity)
    }

    fun deleteUser(id: Int, authentication: Authentication) {
        val principal = authentication.principal
        if (principal !is UserDetails) {
            throw InternalError("Unsupported user details found")
        }

        if (id == principal.userEntity.id) {
            throw AccessDeniedException("Can not delete own user")
        }

        if (!userRepository.existsById(id)) {
            throw EntityNotFoundException()
        }

        userRepository.deleteById(id);
    }

    @Transactional
    fun modifyUser(id: Int, userDTO: UserUpdateRequestDTO, authentication: Authentication) {
        val principal = authentication.principal
        if (principal !is UserDetails) {
            throw InternalError("Unsupported user details found")
        }

        val media = userRepository.findById(id).orElseThrow {
            throw EntityNotFoundException()
        }

        if (!principal.userEntity.isAdmin && principal.userEntity.id != media.id) {
            throw AccessDeniedException("User can only modify their comments")
        }

        media.apply {
            email = userDTO.email
            username = userDTO.username

            if (userDTO.password != null) {
                password = passwordEncoder.encode(userDTO.password)
            }
        }
    }
}