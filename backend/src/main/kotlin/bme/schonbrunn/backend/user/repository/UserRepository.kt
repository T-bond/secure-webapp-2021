package bme.schonbrunn.backend.user.repository

import bme.schonbrunn.backend.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Int> {

    fun findByEmail(email: String): UserEntity?

    fun existsByEmail(email: String): Boolean

}