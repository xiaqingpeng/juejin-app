package com.example.juejin

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun getCurrentPlatformType(): PlatformType = PlatformType.ANDROID
