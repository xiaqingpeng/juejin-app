package com.example.juejin.test

import androidx.compose.runtime.Composable
import com.example.juejin.ui.components.WebViewScreen

/**
 * WebView 测试页面
 * 展示如何在 Compose Multiplatform 中使用 WebView
 */
@Composable
fun WebViewTestScreen(
    onLeftClick: () -> Unit
) {
    WebViewScreen(
        title = "WebView 测试",
        url = "https://juejin.cn",
        onLeftClick = onLeftClick
    )
}