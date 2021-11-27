package bme.schonbrunn.backend.media.entity

import bme.schonbrunn.backend.user.entity.UserEntity
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime
import javax.persistence.*

@Entity(name = "CommentEntity")
@Table(
    name = "comments"
)
class CommentEntity(
    @Id
    @GeneratedValue
    var id: Int? = null,

    @Column(nullable = false, length = 500)
    var comment: String,

    @ManyToOne(optional = false)
    var media: MediaEntity,

    @ManyToOne(optional = false)
    var createdBy: UserEntity
) {

    @CreationTimestamp
    lateinit var createdAt: OffsetDateTime
}
