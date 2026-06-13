package md.vnastasi.atlasmemories

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
