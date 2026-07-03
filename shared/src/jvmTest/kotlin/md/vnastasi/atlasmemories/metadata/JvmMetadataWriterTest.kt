package md.vnastasi.atlasmemories.metadata

import assertk.all
import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import kotlinx.datetime.LocalDateTime
import md.vnastasi.atlasmemories.file.copyFromResource
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class, ExperimentalPathApi::class)
class JvmMetadataWriterTest {

    @Test
    @DisplayName(
        """
        Given image file
        When writing metadata
        Then expect metadata to be written successfully
    """
    )
    fun metadataWrittenCorrectly(@TempDir root: Path) {
        root.copyFromResource("sample-no-gps-data.jpg")

        val dateTimeCreated = LocalDateTime.orNull(year = 2178, month = 5, day = 30, hour = 9, minute = 37, second = 59, nanosecond = 0)
        val latitude = -81.4322
        val longitude = -123.0087

        val metadata = Metadata(dateTimeCreated, latitude, longitude)

        val imageFilePath = root.resolve("sample-no-gps-data.jpg")

        assertDoesNotThrow { MetadataWriter.default().write(imageFilePath, metadata) }

        assertThat(MetadataReader.default().read(imageFilePath)).all {
            prop(Metadata::dateTimeCreated).isEqualTo(dateTimeCreated)
            prop(Metadata::latitude).isNotNull().isCloseTo(latitude, delta = 0.0001)
            prop(Metadata::longitude).isNotNull().isCloseTo(longitude, delta = 0.0001)
        }
    }
}