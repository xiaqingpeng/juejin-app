package com.example.juejin.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.juejin.components.WebView
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.TopNavigationBarWithBack

/**
 * WebView 测试页面
 * 展示如何在 Compose Multiplatform 中使用 WebView
 */
@Composable
fun WebViewTestScreen(
    onLeftClick: () -> Unit
) {
    var currentUrl by remember { mutableStateOf("https://juejin.cn") }
    var isLoading by remember { mutableStateOf(true) }
    
    Scaffold(
        topBar = {
            TopNavigationBarWithBack(
                title = "WebView 测试",
                onLeftClick = onLeftClick
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Colors.primaryWhite)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // WebView 组件
                WebView(
                    url = currentUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onLoadingChanged = { loading ->
                        isLoading = loading
                    }
                )
            }
            
            // 加载指示器
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Colors.primaryBlue)
                }
            }
        }
    }
}
