package bme.schonbrunn.backend.user.entity

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.time.OffsetDateTime
import javax.persistence.*

@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
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
) {

    var deletedAt: OffsetDateTime? = null
}