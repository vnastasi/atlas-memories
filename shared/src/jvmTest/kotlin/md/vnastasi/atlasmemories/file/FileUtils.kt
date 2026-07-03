package md.vnastasi.atlasmemories.file

import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.outputStream

private object FileUtils

fun Path.copyFromResource(fileName: String) {
    FileUtils::class.java.classLoader.getResourceAsStream(fileName)?.copyTo(this.resolve(fileName).createFile().outputStream())
}
