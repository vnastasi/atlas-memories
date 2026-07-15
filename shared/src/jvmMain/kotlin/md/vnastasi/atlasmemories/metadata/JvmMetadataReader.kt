package md.vnastasi.atlasmemories.metadata

import kotlinx.datetime.LocalDateTime
import md.vnastasi.atlasmemories.file.FileSystemFailureReason
import md.vnastasi.atlasmemories.file.Path
import md.vnastasi.atlasmemories.result.Result
import org.apache.commons.imaging.Imaging
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists

class JvmMetadataReader : MetadataReader {

    override fun read(path: Path): Result<Metadata> {
        if (path.notExists()) {
            return Result.error(FileSystemFailureReason.FILE_NOT_FOUND, NoSuchFileException(path.toFile()))
        }

        if (path.isDirectory()) {
            return Result.error(FileSystemFailureReason.DIRECTORY_INSTEAD_OF_FILE, IllegalArgumentException("$path is a directory, file expected"))
        }

        val imageMetadata = try {
            Imaging.getMetadata(path.toFile())
        } catch (e: Exception) {
            return Result.error(MetadataFailureReason.METADATA_READ_FAILURE, e)
        }

        return when (imageMetadata) {
            is JpegImageMetadata -> {
                val exif = imageMetadata.exif
                val dateTimeString = exif.getFieldValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL)?.getOrNull(0)
                val dateTime = dateTimeString?.let { LocalDateTime.parse(it, DATE_TIME_ORIGINAL_FORMAT) }
                val gpsInfo = exif.gpsInfo
                val latitude = gpsInfo?.latitudeAsDegreesNorth
                val longitude = gpsInfo?.longitudeAsDegreesEast
                val metadata = Metadata(dateTimeCreated = dateTime, latitude = latitude, longitude = longitude)

                Result.success(metadata)
            }

            else -> {
                Result.error(MetadataFailureReason.NON_JPEG_IMAGE, IllegalArgumentException("$path does not contain JPEG metadata"))
            }
        }
    }
}
