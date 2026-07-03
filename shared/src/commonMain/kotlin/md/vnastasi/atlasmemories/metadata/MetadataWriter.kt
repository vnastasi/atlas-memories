package md.vnastasi.atlasmemories.metadata

import md.vnastasi.atlasmemories.file.Path

fun interface MetadataWriter {

    fun write(path: Path, metadata: Metadata)

    companion object
}

expect fun MetadataWriter.Companion.default(): MetadataWriter
