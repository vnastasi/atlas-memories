package md.vnastasi.atlasmemories.metadata

import kotlinx.datetime.LocalDateTime

data class Metadata(
    val dateTimeCreated: LocalDateTime?,
    val latitude: Double?,
    val longitude: Double?
)
