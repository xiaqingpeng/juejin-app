package com.example.juejin.platform

import kotlin.system.exitProcess

/**
 * Desktop (JVM) 平台退出应用
 */
actual fun exitApp() {
    exitProcess(0)
}
