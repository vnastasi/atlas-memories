package md.vnastasi.atlasmemories.playground

import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import md.vnastasi.atlasmemories.file.ImageFilesProvider
import md.vnastasi.atlasmemories.file.default
import md.vnastasi.atlasmemories.metadata.MetadataReader
import md.vnastasi.atlasmemories.metadata.default
import java.nio.file.Paths

private const val IMAGE_ROOT_DIR = "/Users/valentinnastasi/Pictures/Travels"

@OptIn(FormatStringsInDatetimeFormats::class)
fun main() {
    var counter = 0

    val imageFilesProvider = ImageFilesProvider.default()
    val metadataReader = MetadataReader.default()

    imageFilesProvider.get(Paths.get(IMAGE_ROOT_DIR))
        .forEach { path ->
            val metadata = metadataReader.read(path)
            println("File: $path")
            println("Created on: ${metadata.dateTimeCreated}")
            println("Latitude: ${metadata.latitude}")
            println("Longitude: ${metadata.longitude}")
            println("=======")
            counter++
        }

    println("Total: $counter")
}
