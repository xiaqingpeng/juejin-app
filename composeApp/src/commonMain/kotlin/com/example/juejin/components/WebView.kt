package com.example.juejin.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 跨平台 WebView 组件
 * @param url 要加载的网页 URL
 * @param modifier 修饰符
 * @param onLoadingChanged 加载状态变化回调
 */
@Composable
expect fun WebView(
    url: String,
    modifier: Modifier = Modifier,
    onLoadingChanged: ((Boolean) -> Unit)? = null
)
