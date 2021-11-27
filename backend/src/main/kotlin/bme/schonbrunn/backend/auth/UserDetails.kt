package bme.schonbrunn.backend.auth

import bme.schonbrunn.backend.user.entity.UserEntity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails as SpringUserDetails

class UserDetails(
    val userEntity: UserEntity,
) : SpringUserDetails {
    override fun getAuthorities() =
        listOf(
            SimpleGrantedAuthority(
                if (userEntity.isAdmin)
                    "ADMIN"
                else
                    "USER"
            )
        )

    override fun getPassword() = userEntity.password

    override fun getUsername() = userEntity.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}