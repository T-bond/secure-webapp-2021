package bme.schonbrunn.backend.parser

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

data class CAFF(
    val isValid: Boolean,
    val creator: String,
    private val createdAtUnix: Long,
    val width: Long,
    val height: Long,
    val captions: MutableSet<String> = mutableSetOf(),
    val tags: MutableList<MutableSet<String>> = mutableListOf(),
) {
    fun getCreatedAt(): OffsetDateTime = Instant.ofEpochSecond(createdAtUnix).atOffset(ZoneOffset.UTC)
}