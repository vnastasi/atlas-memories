package md.vnastasi.atlasmemories.metadata

import assertk.all
import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.prop
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.deleteRecursively
import kotlin.io.path.outputStream
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalPathApi::class)
class JvmMetadataReaderTest {

    private val root = Paths.get(System.getProperty("java.io.tmpdir"), Uuid.generateV4().toHexString())

    @BeforeEach
    fun setUp() {
        root.createDirectory()
        copyImageFile("sample.jpg")
        copyImageFile("sample-no-gps-data.jpg")
    }

    @AfterEach
    fun tearDown() {
        root.deleteRecursively()
    }

    @Test
    @DisplayName(
        """
        Given image file with GPS data
        When extracting metadata
        Then return metadata with GPS data
    """
    )
    fun metadataWithGpsExtractedCorrectly() {
        val metadata = MetadataReader.default().read(root.resolve("sample.jpg"))
        assertThat(metadata).all {
            prop(Metadata::dateTimeCreated).isNotNull().isEqualTo(LocalDateTime.orNull(year = 2025, month = 3, day = 10, hour = 13, minute = 29, second = 32, nanosecond = 0))
            prop(Metadata::latitude).isNotNull().isCloseTo(value = 25.74028, delta = 0.0001)
            prop(Metadata::longitude).isNotNull().isCloseTo(value = 32.60169, delta = 0.0001)
        }
    }

    @Test
    @DisplayName(
        """
        Given image file with no GPS data
        When extracting metadata
        Then return metadata without GPS data
    """
    )
    fun metadataWithNoGpsExtractedCorrectly() {
        val metadata = MetadataReader.default().read(root.resolve("sample-no-gps-data.jpg"))
        assertThat(metadata).all {
            prop(Metadata::dateTimeCreated).isNotNull().isEqualTo(LocalDateTime.orNull(year = 2016, month = 1, day = 5, hour = 11, minute = 3, second = 42, nanosecond = 0))
            prop(Metadata::latitude).isNull()
            prop(Metadata::longitude).isNull()
        }
    }

    private fun copyImageFile(fileName: String) {
        this::class.java.classLoader.getResourceAsStream(fileName)?.copyTo(root.resolve(fileName).createFile().outputStream())
    }
}
