package md.vnastasi.atlasmemories.file

import java.nio.file.Files
import kotlin.io.path.PathWalkOption
import kotlin.io.path.isDirectory
import kotlin.io.path.walk

class JvmImageFilesProvider : ImageFilesProvider {

    override fun get(root: Path): Sequence<Path> =
        root.walk(PathWalkOption.INCLUDE_DIRECTORIES)
            .filterNot { it.isDirectory() }
            .filter { path ->
                val contentType = Files.probeContentType(path)
                contentType != null && contentType.startsWith("image/")
            }
}
