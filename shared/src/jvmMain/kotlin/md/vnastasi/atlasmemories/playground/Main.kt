package md.vnastasi.atlasmemories.playground

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toInstant
import org.apache.commons.imaging.Imaging
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.PathWalkOption
import kotlin.io.path.isDirectory
import kotlin.io.path.walk

private const val IMAGE_ROOT_DIR = "/Users/valentinnastasi/Pictures/Travels"

@OptIn(FormatStringsInDatetimeFormats::class)
private val DATE_TIME_FORMAT = LocalDateTime.Format { byUnicodePattern("yyyy:MM:dd HH:mm:ss") }

@OptIn(FormatStringsInDatetimeFormats::class)
fun main() {
    var counter = 0
    val root = Paths.get(IMAGE_ROOT_DIR)
    root.walk(PathWalkOption.INCLUDE_DIRECTORIES)
        .filterNot { it.isDirectory() }
        .filter { Files.probeContentType(it)?.startsWith("image") == true }
        .forEach { path ->
            println("File: $path")
            val metadata = Imaging.getMetadata(path.toFile())
            when (metadata) {
                is JpegImageMetadata -> {
                    val exif = metadata.exif
                    val dateTimeString = exif.getFieldValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL)?.getOrNull(0)
                    val dateTime = dateTimeString?.let { LocalDateTime.parse(it, DATE_TIME_FORMAT) }

                    val gpsInfo = exif.gpsInfo
                    val latitude = gpsInfo?.latitudeAsDegreesNorth
                    val longitude = gpsInfo?.longitudeAsDegreesEast

                    println("Created on: $dateTime")
                    println("Latitude: $latitude")
                    println("Longitude: $longitude")
                }
            }
            println("=======")
            counter++
        }
    println("Total: $counter")
}
