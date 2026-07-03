package md.vnastasi.atlasmemories.metadata

import assertk.all
import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isDataClassEqualTo
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.deleteRecursively
import kotlin.io.path.outputStream
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalPathApi::class)
class JvmMetadataWriterTest {

    private val root = Paths.get(System.getProperty("java.io.tmpdir"), Uuid.generateV4().toHexString())

    @BeforeEach
    fun setUp() {
        root.createDirectory()
        copyImageFile("sample-no-gps-data.jpg")
    }

    @AfterEach
    fun tearDown() {
        root.deleteRecursively()
    }

    @Test
    fun metadataWrittenCorrectly() {
        val dateTimeCreated = LocalDateTime.orNull(year = 2016, month = 1, day = 5, hour = 11, minute = 3, second = 42, nanosecond = 0)
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

    private fun copyImageFile(fileName: String) {
        this::class.java.classLoader.getResourceAsStream(fileName)?.copyTo(root.resolve(fileName).createFile().outputStream())
    }
}