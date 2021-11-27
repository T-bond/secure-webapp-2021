package bme.schonbrunn.backend.media.entity

import bme.schonbrunn.backend.user.entity.UserEntity
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import java.time.OffsetDateTime
import javax.persistence.*

@SQLDelete(sql = "UPDATE medias SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Entity(name = "MediaEntity")
@Table(name = "medias")
class MediaEntity(
    @Id
    @GeneratedValue
    var id: Int? = null,

    @Column(nullable = false, length = 255)
    var title: String,

    @Column(nullable = false, length = 500)
    var description: String,

    @ManyToOne(optional = false)
    var createdBy: UserEntity
) {

    @CreationTimestamp
    lateinit var createdAt: OffsetDateTime

    @OneToMany(mappedBy = "media")
    lateinit var comments: MutableList<CommentEntity>

    var deletedAt: OffsetDateTime? = null
}