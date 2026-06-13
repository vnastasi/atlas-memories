package md.vnastasi.atlasmemories

import android.os.Build

internal class AndroidPlatform : Platform {

    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}
