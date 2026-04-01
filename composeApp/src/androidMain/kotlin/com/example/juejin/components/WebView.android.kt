package com.example.juejin.components

import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Android 平台 WebView 实现
 */
@Composable
actual fun WebView(
    url: String,
    modifier: Modifier,
    onLoadingChanged: ((Boolean) -> Unit)?
) {
    AndroidView(
        factory = { context ->
            android.webkit.WebView(context).apply {
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(
                        view: android.webkit.WebView?,
                        url: String?,
                        favicon: android.graphics.Bitmap?
                    ) {
                        onLoadingChanged?.invoke(true)
                    }
                    
                    override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                        onLoadingChanged?.invoke(false)
                    }
                }
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                setBackgroundColor(android.graphics.Color.WHITE)
                loadUrl(url)
            }
        },
        modifier = modifier
    )
}
