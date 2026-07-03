package md.vnastasi.atlasmemories.metadata

import kotlinx.datetime.LocalDateTime
import md.vnastasi.atlasmemories.file.Path
import org.apache.commons.imaging.Imaging
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants

class JvmMetadataReader : MetadataReader {

    override fun read(path: Path): Metadata =
        when (val imageMetadata = Imaging.getMetadata(path.toFile())) {
            is JpegImageMetadata -> {
                val exif = imageMetadata.exif
                val dateTimeString = exif.getFieldValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL)?.getOrNull(0)
                val dateTime = dateTimeString?.let { LocalDateTime.parse(it, DATE_TIME_ORIGINAL_FORMAT) }

                val gpsInfo = exif.gpsInfo
                val latitude = gpsInfo?.latitudeAsDegreesNorth
                val longitude = gpsInfo?.longitudeAsDegreesEast

                Metadata(dateTimeCreated = dateTime, latitude = latitude, longitude = longitude)
            }

            else -> {
                Metadata(dateTimeCreated = null, latitude = null, longitude = null)
            }
        }
}
