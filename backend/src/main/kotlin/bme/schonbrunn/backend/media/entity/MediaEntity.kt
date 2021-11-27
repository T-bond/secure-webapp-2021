package bme.schonbrunn.backend.media.entity

import bme.schonbrunn.backend.user.entity.UserEntity
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime
import javax.persistence.*

@Entity(name = "MediaEntity")
@Table(name = "medias")
class MediaEntity(
    @Id
    @GeneratedValue
    var id: Int? = null,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var description: String,

    @ManyToOne(optional = false)
    var createdBy: UserEntity
) {

    @CreationTimestamp
    lateinit var createdAt: OffsetDateTime

    @OneToMany(mappedBy = "media")
    lateinit var comments: MutableList<CommentEntity>
}