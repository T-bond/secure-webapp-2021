package bme.schonbrunn.backend.auth.controller

import bme.schonbrunn.backend.auth.dto.AuthenticationRequestDTO
import bme.schonbrunn.backend.auth.service.AuthService
import bme.schonbrunn.backend.user.dto.UserCreateRequestDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("sign-in")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun signIn(@RequestBody authenticationRequest: AuthenticationRequestDTO) =
        authService.signIn(authenticationRequest)

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@Valid @RequestBody userCreateRequest: UserCreateRequestDTO) =
        authService.signUp(userCreateRequest)

    @DeleteMapping("logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(request: HttpServletRequest, response: HttpServletResponse) =
        authService.logout(request, response)

}