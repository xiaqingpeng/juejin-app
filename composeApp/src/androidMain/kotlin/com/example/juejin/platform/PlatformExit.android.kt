package com.example.juejin.platform

import android.app.Activity
import android.os.Process
import kotlin.system.exitProcess

/**
 * Android 平台退出应用
 */
actual fun exitApp() {
    // 结束当前进程
    exitProcess(0)
}
