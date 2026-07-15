package md.vnastasi.atlasmemories.metadata

import md.vnastasi.atlasmemories.file.Path
import md.vnastasi.atlasmemories.result.Result

fun interface MetadataReader {

    fun read(path: Path): Result<Metadata>

    companion object
}

expect fun MetadataReader.Companion.default(): MetadataReader
