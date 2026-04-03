package com.example.juejin.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.juejin.ui.Colors
import com.example.juejin.theme.ThemeColors
import com.example.juejin.ui.components.TopNavigationBar

@Composable
fun QrScannerScreen(
    onBack: () -> Unit,
    onQrCodeScanned: (String) -> Unit
) {
    // 扫码结果状态
    var scannedQrCode by remember { mutableStateOf<String?>(null) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // 全屏扫码预览
        QrScannerPreview(
            modifier = Modifier.fillMaxSize(),
            onQrCodeScanned = { code ->
                scannedQrCode = code
                onQrCodeScanned(code)
            }
        )
        
        // 顶部导航栏（浮动在扫码界面上方）
        TopNavigationBar(
            title = "二维码扫描",
            onLeftClick = onBack,
            backgroundColor = Color.Transparent,
            contentColor = ThemeColors.Text.white
        )

        // 底部提示文字
        Text(
            text = "将二维码放入取景框内自动识别",
            color = ThemeColors.Text.white,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        )
        
        // 扫码结果弹窗
        if (scannedQrCode != null) {
            Dialog(
                onDismissRequest = { scannedQrCode = null },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ThemeColors.Background.primary
                    )
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        // Header with title and close button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "二维码结果",
                                style = MaterialTheme.typography.headlineMedium,
                                color = ThemeColors.primaryBlue,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(
                                onClick = { scannedQrCode = null },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "关闭",
                                    tint = ThemeColors.Text.secondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // QR code content
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = ThemeColors.Background.secondary
                        ) {
                            Text(
                                text = scannedQrCode.orEmpty(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = ThemeColors.Text.primary,
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Action button
                        Button(
                            onClick = { scannedQrCode = null },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ThemeColors.primaryBlue
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "确定",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = ThemeColors.Text.white
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
expect fun QrScannerPreview(
    modifier: Modifier,
    onQrCodeScanned: (String) -> Unit
)

