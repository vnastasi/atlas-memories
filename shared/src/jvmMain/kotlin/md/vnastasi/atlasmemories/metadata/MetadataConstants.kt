package md.vnastasi.atlasmemories.metadata

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

@OptIn(FormatStringsInDatetimeFormats::class)
val DATE_TIME_ORIGINAL_FORMAT = LocalDateTime.Format { byUnicodePattern("yyyy:MM:dd HH:mm:ss") }
