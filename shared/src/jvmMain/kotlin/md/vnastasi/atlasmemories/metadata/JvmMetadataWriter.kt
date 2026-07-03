package md.vnastasi.atlasmemories.metadata

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import md.vnastasi.atlasmemories.file.Path
import org.apache.commons.imaging.Imaging
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet
import java.nio.file.Paths
import kotlin.io.path.copyTo
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid



@OptIn(ExperimentalUuidApi::class)
class JvmMetadataWriter : MetadataWriter {

    private val tmpDir = Paths.get(System.getProperty("java.io.tmpdir"))
    private val exifRewriter = ExifRewriter()

    override fun write(path: Path, metadata: Metadata) {
        val sourceImageFile = path.copyTo(tmpDir.resolve("${Uuid.generateV4().toHexString()}.jpg")).toFile()
        val destinationImageFile = path.toFile()

        destinationImageFile.outputStream().buffered().use { outputStream ->
            val tiffOutputSet = (Imaging.getMetadata(sourceImageFile) as? JpegImageMetadata)?.exif?.outputSet ?: TiffOutputSet()
            tiffOutputSet.setGpsInDegreesSafely(metadata.longitude, metadata.latitude)

            val exifDirectory = tiffOutputSet.orCreateExifDirectory
            exifDirectory.setDateTimeOriginal(metadata.dateTimeCreated)

            exifRewriter.updateExifMetadataLossless(sourceImageFile, outputStream, tiffOutputSet)
        }

        sourceImageFile.delete()
    }

    private fun TiffOutputDirectory.setDateTimeOriginal(dateTime: LocalDateTime?) {
        removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL)
        add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, dateTime?.format(DATE_TIME_ORIGINAL_FORMAT))
    }

    private fun TiffOutputSet.setGpsInDegreesSafely(longitude: Double?, latitude: Double?) {
        if (longitude == null || latitude == null) return
        setGpsInDegrees(longitude, latitude)
    }
}
