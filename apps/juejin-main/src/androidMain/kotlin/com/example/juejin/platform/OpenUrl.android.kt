package com.example.juejin.platform

import android.content.Intent
import android.net.Uri
import com.example.juejin.MainActivity

/**
 * Android 平台打开 URL
 */
actual fun openUrl(url: String) {
    val context = MainActivity.instance ?: return
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
