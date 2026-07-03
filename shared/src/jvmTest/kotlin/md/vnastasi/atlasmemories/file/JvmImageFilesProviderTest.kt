package md.vnastasi.atlasmemories.file

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.extracting
import assertk.assertions.isEmpty
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile

class JvmImageFilesProviderTest {

    @Test
    @DisplayName(
        """
        Given no image files available
        When getting image files
        Then return empty sequence
    """
    )
    fun noImageFilesAvailable(@TempDir root: Path) {
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
    fun imageFilesProvidedCorrectly(@TempDir root: Path) {
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
