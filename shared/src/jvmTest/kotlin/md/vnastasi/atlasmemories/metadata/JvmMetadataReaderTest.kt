package md.vnastasi.atlasmemories.metadata

import assertk.all
import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.prop
import kotlinx.datetime.LocalDateTime
import md.vnastasi.atlasmemories.file.FileSystemFailureReason
import md.vnastasi.atlasmemories.file.copyFromResource
import md.vnastasi.atlasmemories.result.Result
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectory
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalPathApi::class)
class JvmMetadataReaderTest {

    @Test
    @DisplayName(
        """
        Given image file with GPS data
        When reading metadata
        Then return metadata with GPS data
    """
    )
    fun metadataWithGpsExtractedCorrectly(@TempDir root: Path) {
        root.copyFromResource("sample.jpg")

        val result = MetadataReader.default().read(root.resolve("sample.jpg"))
        assertThat(result)
            .isInstanceOf<Result.Success<Metadata>>()
            .prop(Result.Success<Metadata>::data).all {
                prop(Metadata::dateTimeCreated).isNotNull().isEqualTo(LocalDateTime.orNull(year = 2025, month = 3, day = 10, hour = 13, minute = 29, second = 32, nanosecond = 0))
                prop(Metadata::latitude).isNotNull().isCloseTo(value = 25.74028, delta = 0.0001)
                prop(Metadata::longitude).isNotNull().isCloseTo(value = 32.60169, delta = 0.0001)
            }
    }

    @Test
    @DisplayName(
        """
        Given image file with no GPS data
        When reading metadata
        Then return metadata without GPS data
    """
    )
    fun metadataWithNoGpsExtractedCorrectly(@TempDir root: Path) {
        root.copyFromResource("sample-no-gps-data.jpg")

        val result = MetadataReader.default().read(root.resolve("sample-no-gps-data.jpg"))
        assertThat(result)
            .isInstanceOf<Result.Success<Metadata>>()
            .prop(Result.Success<Metadata>::data).all {
                prop(Metadata::dateTimeCreated).isNotNull().isEqualTo(LocalDateTime.orNull(year = 2016, month = 1, day = 5, hour = 11, minute = 3, second = 42, nanosecond = 0))
                prop(Metadata::latitude).isNull()
                prop(Metadata::longitude).isNull()
            }
    }

    @Test
    @DisplayName(
        """
        Given file does not exist
        When reading metadata
        Then return error
    """
    )
    fun fileDoesntExist(@TempDir root: Path) {
        val result = MetadataReader.default().read(root.resolve("sample-no-gps-data.jpg"))
        assertThat(result)
            .isInstanceOf<Result.Error>()
            .prop(Result.Error::failureReason).isEqualTo(FileSystemFailureReason.FILE_NOT_FOUND)
    }

    @Test
    @DisplayName(
        """
        Given file is a directory
        When reading metadata
        Then return error
    """
    )
    fun fileIsADirectory(@TempDir root: Path) {
        root.resolve("sample-no-gps-data.jpg").createDirectory()
        val result = MetadataReader.default().read(root.resolve("sample-no-gps-data.jpg"))
        assertThat(result)
            .isInstanceOf<Result.Error>()
            .prop(Result.Error::failureReason).isEqualTo(FileSystemFailureReason.DIRECTORY_INSTEAD_OF_FILE)
    }

    @Test
    @DisplayName(
        """
        Given file is not image
        When reading metadata
        Then return error
    """
    )
    fun fileNotImage(@TempDir root: Path) {
        root.copyFromResource("sample.txt")
        val result = MetadataReader.default().read(root.resolve("sample.txt"))
        assertThat(result)
            .isInstanceOf<Result.Error>()
            .prop(Result.Error::failureReason).isEqualTo(MetadataFailureReason.METADATA_READ_FAILURE)
    }
}
