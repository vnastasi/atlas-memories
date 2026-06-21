package md.vnastasi.atlasmemories.metadata

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class Metadata(
    val createdAt: LocalDateTime?,
    val latitude: Double?,
    val longitude: Double?
)
