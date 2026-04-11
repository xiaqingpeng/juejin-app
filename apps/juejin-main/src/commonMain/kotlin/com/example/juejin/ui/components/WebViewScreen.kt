package com.example.juejin.ui.components

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
import com.example.juejin.ui.theme.ThemeColors

/**
 * 通用 WebView 页面组件
 * @param title 页面标题
 * @param url 要加载的网页 URL
 * @param onLeftClick 返回按钮点击回调
 */
@Composable
fun WebViewScreen(
    title: String,
    url: String,
    onLeftClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    
    Scaffold(
        topBar = {
            TopNavigationBarWithBack(
                title = title,
                onLeftClick = onLeftClick
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(ThemeColors.primaryWhite)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // WebView 组件
                WebView(
                    url = url,
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
                    CircularProgressIndicator(color = ThemeColors.primaryBlue)
                }
            }
        }
    }
}
