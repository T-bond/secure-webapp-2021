package bme.schonbrunn.backend.user.dto

import bme.schonbrunn.backend.user.entity.UserEntity

data class OwnUserDTO(
    val username: String,
    val email: String,
) {

    companion object {
        fun from(userEntity: UserEntity) =
            OwnUserDTO(
                username = userEntity.username,
                email = userEntity.email,
            )
    }

}
