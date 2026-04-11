package com.example.juejin.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.ui.Colors
import com.example.juejin.ui.theme.ThemeColors
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.notification_bell
import juejin.composeapp.generated.resources.notification_dialog_allow
import juejin.composeapp.generated.resources.notification_dialog_deny
import juejin.composeapp.generated.resources.notification_dialog_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationPermissionDialog(
    onDismiss: () -> Unit,
    onAllow: () -> Unit,
    onDeny: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.98f)  // 调宽到 98%
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        color = ThemeColors.Background.dialog,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),  // 调整内边距
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 铃铛图标
            Icon(
                painter = painterResource(Res.drawable.notification_bell),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 标题
            Text(
                text = stringResource(Res.string.notification_dialog_title),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = ThemeColors.Text.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 按钮行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 禁止按钮 - 灰色背景，蓝色文字
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = ThemeColors.Button.secondary
                ) {
                    TextButton(
                        onClick = onDeny,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColors(contentColor = ThemeColors.primaryBlue)
                    ) {
                        Text(
                            text = stringResource(Res.string.notification_dialog_deny),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // 始终允许按钮 - 灰色背景
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = ThemeColors.primaryGray
                ) {
                    TextButton(
                        onClick = onAllow,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColors(contentColor = ThemeColors.primaryBlue)
                    ) {
                        Text(
                            text = stringResource(Res.string.notification_dialog_allow),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}
