package md.vnastasi.atlasmemories.metadata

actual fun MetadataWriter.Companion.default(): MetadataWriter = JvmMetadataWriter()
