package md.vnastasi.atlasmemories.file

import md.vnastasi.atlasmemories.result.FailureReason

enum class FileSystemFailureReason(override val code: String): FailureReason {

    FILE_NOT_FOUND("FS-001"),
    DIRECTORY_NOT_FOUND("FS-002"),
    DIRECTORY_INSTEAD_OF_FILE("FS-003"),
}