package bme.schonbrunn.backend.user.service

import bme.schonbrunn.backend.auth.UserDetails
import bme.schonbrunn.backend.user.dto.OwnUserDTO
import bme.schonbrunn.backend.user.dto.UserCreateRequestDTO
import bme.schonbrunn.backend.user.entity.UserEntity
import bme.schonbrunn.backend.user.exception.EmailAlreadyInUseException
import bme.schonbrunn.backend.user.repository.UserRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: DelegatingPasswordEncoder,
) {

    fun createUser(userCreateRequest: UserCreateRequestDTO) {
        val email = userCreateRequest.email
        if (userRepository.existsByEmail(email)) {
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

}