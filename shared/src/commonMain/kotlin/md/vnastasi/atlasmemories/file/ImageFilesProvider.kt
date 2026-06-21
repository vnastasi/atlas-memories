package md.vnastasi.atlasmemories.file

fun interface ImageFilesProvider {

    fun get(root: Path): Sequence<Path>

    companion object
}

expect fun ImageFilesProvider.Companion.default(): ImageFilesProvider
