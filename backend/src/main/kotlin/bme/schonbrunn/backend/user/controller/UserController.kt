package bme.schonbrunn.backend.user.controller

import bme.schonbrunn.backend.auth.UserDetails
import bme.schonbrunn.backend.user.dto.OwnUserDTO
import bme.schonbrunn.backend.user.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("users")
class UserController(
    private val userRepository: UserRepository,
) {

    @GetMapping("me")
    fun getOwnProfile(): OwnUserDTO {
        val principal = SecurityContextHolder.getContext().authentication.principal

        val userEntity = if (principal is UserDetails) {
            principal.userEntity
        } else {
            val email = principal.toString()
            userRepository.findByEmail(email)!!
        }

        return OwnUserDTO.from(userEntity)
    }
}