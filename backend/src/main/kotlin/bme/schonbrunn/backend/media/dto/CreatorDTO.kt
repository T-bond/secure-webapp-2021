package bme.schonbrunn.backend.media.dto

import bme.schonbrunn.backend.user.entity.UserEntity

data class CreatorDTO(
    val id: Int,
    val username: String,
) {
    companion object {
        fun from(userEntity: UserEntity) = CreatorDTO(
            id = userEntity.id!!,
            username = userEntity.username,
        )
    }
}
