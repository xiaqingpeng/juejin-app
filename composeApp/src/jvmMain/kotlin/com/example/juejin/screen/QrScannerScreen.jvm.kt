package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.juejin.ui.Colors
import com.example.juejin.theme.ThemeColors

@Composable
actual fun QrScannerPreview(
    modifier: Modifier,
    onQrCodeScanned: (String) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize().background(ThemeColors.Background.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "当前平台不支持二维码扫描")
    }
}

