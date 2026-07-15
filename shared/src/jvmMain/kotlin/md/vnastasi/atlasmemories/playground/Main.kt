package md.vnastasi.atlasmemories.playground

import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import md.vnastasi.atlasmemories.file.ImageFilesProvider
import md.vnastasi.atlasmemories.file.default
import md.vnastasi.atlasmemories.metadata.Metadata
import md.vnastasi.atlasmemories.metadata.MetadataReader
import md.vnastasi.atlasmemories.metadata.default
import md.vnastasi.atlasmemories.result.Result
import java.nio.file.Paths

private const val IMAGE_ROOT_DIR = "/Users/valentinnastasi/Pictures/Travels"

@OptIn(FormatStringsInDatetimeFormats::class)
fun main() {
    var counter = 0

    val imageFilesProvider = ImageFilesProvider.default()
    val metadataReader = MetadataReader.default()

    imageFilesProvider.get(Paths.get(IMAGE_ROOT_DIR))
        .forEach { path ->
            val result = metadataReader.read(path)

            println("File: $path")
            when (result) {
                is Result.Success<Metadata> -> {
                    println("Created on: ${result.data.dateTimeCreated}")
                    println("Latitude: ${result.data.latitude}")
                    println("Longitude: ${result.data.longitude}")
                    println("=======")
                    counter++
                }

                is Result.Error -> {
                    println("Error: ${result.failureReason}")
                }
            }
        }

    println("Total: $counter")
}
