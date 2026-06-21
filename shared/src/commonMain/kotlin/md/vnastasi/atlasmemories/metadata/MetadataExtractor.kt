package md.vnastasi.atlasmemories.metadata

import md.vnastasi.atlasmemories.file.Path

fun interface MetadataExtractor {

    fun extract(path: Path): Metadata

    companion object
}

expect fun MetadataExtractor.Companion.default(): MetadataExtractor
