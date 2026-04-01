package com.example.juejin.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView
import platform.darwin.NSObject

/**
 * iOS 平台 WebView 实现
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebView(
    url: String,
    modifier: Modifier,
    onLoadingChanged: ((Boolean) -> Unit)?
) {
    val navigationDelegate = remember {
        object : NSObject(), WKNavigationDelegateProtocol {
            override fun webView(webView: WKWebView, didStartProvisionalNavigation: platform.WebKit.WKNavigation?) {
                onLoadingChanged?.invoke(true)
            }
            
            override fun webView(webView: WKWebView, didFinishNavigation: platform.WebKit.WKNavigation?) {
                onLoadingChanged?.invoke(false)
            }
            
            override fun webView(
                webView: WKWebView,
                didFailNavigation: platform.WebKit.WKNavigation?,
                withError: platform.Foundation.NSError
            ) {
                onLoadingChanged?.invoke(false)
            }
        }
    }
    
    UIKitView(
        factory = {
            WKWebView().apply {
                navigationDelegate = navigationDelegate
                opaque = false
                backgroundColor = platform.UIKit.UIColor.whiteColor
                val nsUrl = NSURL.URLWithString(url)
                if (nsUrl != null) {
                    val request = NSURLRequest.requestWithURL(nsUrl)
                    loadRequest(request)
                }
            }
        },
        modifier = modifier
    )
    
    DisposableEffect(Unit) {
        onDispose {
            // Cleanup if needed
        }
    }
}
