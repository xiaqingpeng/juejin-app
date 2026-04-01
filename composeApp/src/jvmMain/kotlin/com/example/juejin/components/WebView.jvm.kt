package com.example.juejin.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * JVM/Desktop 平台 WebView 实现
 * 注意：Desktop 平台需要使用 JavaFX WebView 或其他第三方库
 * 这里提供一个简单的占位实现
 */
@Composable
actual fun WebView(
    url: String,
    modifier: Modifier,
    onLoadingChanged: ((Boolean) -> Unit)?
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Desktop WebView 暂不支持\n请在 Android 或 iOS 平台测试\nURL: $url")
    }
}
