package md.vnastasi.atlasmemories.file

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.extracting
import assertk.assertions.isEmpty
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.deleteRecursively
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalPathApi::class, ExperimentalUuidApi::class)
class JvmImageFilesProviderTest {

    private val root = Paths.get(System.getProperty("java.io.tmpdir"), Uuid.generateV4().toHexString())

    @BeforeEach
    fun setUp() {
        root.createDirectory()
    }

    @AfterEach
    fun tearDown() {
        root.deleteRecursively()
    }

    @Test
    @DisplayName(
        """
        Given no image files available
        When getting image files
        Then return empty sequence
    """
    )
    fun noImageFilesAvailable() {
        val sequence = ImageFilesProvider.default().get(root)
        assertThat(sequence.toList()).isEmpty()
    }

    @Test
    @DisplayName(
        """
        Given image files available
        When getting image files
        Then return sequence with image files
    """
    )
    fun imageFilesProvidedCorrectly() {
        root.apply {
            resolve("image1.jpg").createFile()
            resolve("video.mp4").createFile()
            resolve("nested").createDirectory().apply {
                resolve("image2.jpg").createFile()
                resolve("document.pdf").createFile()
            }
        }

        val sequence = ImageFilesProvider.default().get(root)
        assertThat(sequence.toList())
            .extracting(Path::absolutePathString)
            .extracting { it.split("/").last() }
            .containsExactlyInAnyOrder("image1.jpg", "image2.jpg")
    }
}
