package bme.schonbrunn.backend.user.entity

import javax.persistence.*

@Entity(name = "UserEntity")
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue
    var id: Int? = null,

    @Column(nullable = false)
    var username: String,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var password: String,
)