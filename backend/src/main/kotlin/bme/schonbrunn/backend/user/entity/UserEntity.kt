package bme.schonbrunn.backend.user.entity

import javax.persistence.*

@Entity(name = "UserEntity")
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email"]),
    ]
)

class UserEntity(
    @Id
    @GeneratedValue
    var id: Int? = null,

    @Column(nullable = false, length = 255)
    var username: String,

    @Column(nullable = false, length = 320)
    var email: String,

    @Column(nullable = false)
    var password: String,

    var isAdmin: Boolean = false
)