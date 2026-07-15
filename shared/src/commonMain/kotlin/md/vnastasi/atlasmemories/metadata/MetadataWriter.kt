package md.vnastasi.atlasmemories.metadata

import md.vnastasi.atlasmemories.file.Path
import md.vnastasi.atlasmemories.result.Result

fun interface MetadataWriter {

    fun write(path: Path, metadata: Metadata): Result<Unit>

    companion object
}

expect fun MetadataWriter.Companion.default(): MetadataWriter
