package md.vnastasi.atlasmemories

internal class JVMPlatform: Platform {

    override val name: String = "Java ${System.getProperty("java.version")}"
}
