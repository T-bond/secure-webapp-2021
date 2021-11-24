package bme.schonbrunn.backend.media.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "MediaEntity")
@Table(name = "medias")
class MediaEntity(
    @Id
    @GeneratedValue
    var id: Int? = null,
    var title: String,
    var description: String,
)