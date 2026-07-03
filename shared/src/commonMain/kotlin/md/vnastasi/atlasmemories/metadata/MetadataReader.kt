package md.vnastasi.atlasmemories.metadata

import md.vnastasi.atlasmemories.file.Path

fun interface MetadataReader {

    fun read(path: Path): Metadata

    companion object
}

expect fun MetadataReader.Companion.default(): MetadataReader
