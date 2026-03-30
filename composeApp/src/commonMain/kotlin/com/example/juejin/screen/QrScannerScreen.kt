package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.TopNavigationBar

@Composable
fun QrScannerScreen(
    onBack: () -> Unit,
    onQrCodeScanned: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopNavigationBar(
                title = "二维码扫描",
                onLeftClick = onBack
            )
        }
    ) { padding ->
        Box(
            modifier =
                Modifier.fillMaxSize()
                    .padding(padding)
                    .background(Colors.Background.primary),
            contentAlignment = Alignment.Center
        ) {
            QrScannerPreview(
                modifier = Modifier.fillMaxSize(),
                onQrCodeScanned = onQrCodeScanned
            )

            Text(
                text = "将二维码放入取景框内自动识别",
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
            )
        }
    }
}

@Composable
expect fun QrScannerPreview(
    modifier: Modifier,
    onQrCodeScanned: (String) -> Unit
)

