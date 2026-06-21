package md.vnastasi.atlasmemories.metadata

actual fun MetadataExtractor.Companion.default(): MetadataExtractor = JvmMetadataExtractor()
