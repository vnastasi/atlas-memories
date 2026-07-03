package md.vnastasi.atlasmemories.metadata

actual fun MetadataReader.Companion.default(): MetadataReader = JvmMetadataReader()
