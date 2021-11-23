package bme.schonbrunn.backend.auth.service

import bme.schonbrunn.backend.auth.UserDetails
import bme.schonbrunn.backend.user.repository.UserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.UserDetailsService as SpringUserDetailsService

@Service
class UserDetailsService(
    private val userRepository: UserRepository,
) : SpringUserDetailsService {
    override fun loadUserByUsername(email: String?) =
        email?.let {
            val userEntity = userRepository.findByEmail(email) ?: throw UsernameNotFoundException("")

            UserDetails(userEntity)
        }


}