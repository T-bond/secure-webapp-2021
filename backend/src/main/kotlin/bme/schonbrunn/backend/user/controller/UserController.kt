package bme.schonbrunn.backend.user.controller

import bme.schonbrunn.backend.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("me")
    fun getOwnProfile(authentication: Authentication) = userService.getMe(authentication)

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    fun deleteUser(@PathVariable id: Int, authentication: Authentication) = userService.deleteUser(id, authentication)
}