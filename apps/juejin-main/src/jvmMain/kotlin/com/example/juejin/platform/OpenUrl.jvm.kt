package com.example.juejin.platform

import java.awt.Desktop
import java.net.URI

/**
 * Desktop (JVM) 平台打开 URL
 */
actual fun openUrl(url: String) {
    if (Desktop.isDesktopSupported()) {
        val desktop = Desktop.getDesktop()
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(URI(url))
        }
    }
}
