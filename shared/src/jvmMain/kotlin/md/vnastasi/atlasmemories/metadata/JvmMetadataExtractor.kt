package md.vnastasi.atlasmemories.metadata

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import md.vnastasi.atlasmemories.file.Path
import org.apache.commons.imaging.Imaging
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants

@OptIn(FormatStringsInDatetimeFormats::class)
private val DATE_TIME_FORMAT = LocalDateTime.Format { byUnicodePattern("yyyy:MM:dd HH:mm:ss") }

class JvmMetadataExtractor : MetadataExtractor {

    override fun extract(path: Path): Metadata =
        when (val imageMetadata = Imaging.getMetadata(path.toFile())) {
            is JpegImageMetadata -> {
                val exif = imageMetadata.exif
                val dateTimeString = exif.getFieldValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL)?.getOrNull(0)
                val dateTime = dateTimeString?.let { LocalDateTime.parse(it, DATE_TIME_FORMAT) }

                val gpsInfo = exif.gpsInfo
                val latitude = gpsInfo?.latitudeAsDegreesNorth
                val longitude = gpsInfo?.longitudeAsDegreesEast

                Metadata(createdAt = dateTime, latitude = latitude, longitude = longitude)
            }

            else -> {
                Metadata(createdAt = null, latitude = null, longitude = null)
            }
        }
}
