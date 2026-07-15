package md.vnastasi.atlasmemories.metadata

import md.vnastasi.atlasmemories.result.FailureReason

enum class MetadataFailureReason(override val code: String) : FailureReason {

    METADATA_READ_FAILURE("MET-001"),
    METADATA_WRITE_FAILURE("MET-002"),
    NON_JPEG_IMAGE("MET-003")
}