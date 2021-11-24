package bme.schonbrunn.backend.auth.service

import bme.schonbrunn.backend.auth.dto.AuthenticationRequestDTO
import bme.schonbrunn.backend.user.dto.UserCreateRequestDTO
import bme.schonbrunn.backend.user.service.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
) {
    fun signIn(authenticationRequest: AuthenticationRequestDTO) {
        val authReq = UsernamePasswordAuthenticationToken(
            authenticationRequest.email,
            authenticationRequest.password
        )
        val auth = authenticationManager.authenticate(authReq)!!
        val sc = SecurityContextHolder.getContext()
        sc.authentication = auth
    }

    fun signUp(userCreateRequest: UserCreateRequestDTO) {
        userService.createUser(userCreateRequest)
    }

    fun logout(request: HttpServletRequest, response: HttpServletResponse) {
        val auth = SecurityContextHolder.getContext().authentication
        SecurityContextLogoutHandler().logout(request, response, auth)
    }
}