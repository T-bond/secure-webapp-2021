package bme.schonbrunn.backend.user.service

import bme.schonbrunn.backend.user.dto.UserCreateRequestDTO
import bme.schonbrunn.backend.user.entity.UserEntity
import bme.schonbrunn.backend.user.exception.EmailAlreadyInUseException
import bme.schonbrunn.backend.user.repository.UserRepository
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.stereotype.Service

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

}