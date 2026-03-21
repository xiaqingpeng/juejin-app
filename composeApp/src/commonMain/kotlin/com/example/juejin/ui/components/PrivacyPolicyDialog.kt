package com.example.juejin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * 隐私政策弹窗
 * 首次启动时显示，用户同意后不再弹出
 */
@Composable
fun PrivacyPolicyDialog(
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onUserAgreementClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onBasicVersionClick: () -> Unit = {}
) {
    val linkColor = Color(0xFF1677FF)

    Dialog(onDismissRequest = { /* 禁止外部关闭 */ }) {
        Column(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "欢迎使用稀土掘金",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 第一行：用户协议与隐私政策（可点击）
            val userAgreementText = "《用户协议》"
            val privacyPolicyText = "《隐私政策》"
            val firstLineFullText = "欢迎使用稀土掘金！我们将通过$userAgreementText 与$privacyPolicyText 帮助您了解我们如何收集、处理个人信息。"
            val firstLineAnnotatedString = buildAnnotatedString {
                append(firstLineFullText)
                addStyle(
                    style = SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline),
                    start = firstLineFullText.indexOf(userAgreementText),
                    end = firstLineFullText.indexOf(userAgreementText) + userAgreementText.length
                )
                addStyle(
                    style = SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline),
                    start = firstLineFullText.indexOf(privacyPolicyText),
                    end = firstLineFullText.indexOf(privacyPolicyText) + privacyPolicyText.length
                )
            }
            
            ClickableText(
                text = firstLineAnnotatedString,
                onClick = { offset ->
                    val uaStart = firstLineFullText.indexOf(userAgreementText)
                    val uaEnd = uaStart + userAgreementText.length
                    val ppStart = firstLineFullText.indexOf(privacyPolicyText)
                    val ppEnd = ppStart + privacyPolicyText.length
                    
                    if (offset in uaStart..uaEnd) {
                        println("[PrivacyDialog] 用户点击《用户协议》")
                        onUserAgreementClick()
                    } else if (offset in ppStart..ppEnd) {
                        println("[PrivacyDialog] 用户点击《隐私政策》")
                        onPrivacyPolicyClick()
                    }
                },
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 第二行：基础版掘金（可点击）
            val basicVersionText = "设置 - 基础版掘金"
            val secondLineFullText = "我们尊重您的选择权，如果您希望仅使用基本功能，可随时在$basicVersionText 中进行选择。"
            val secondLineAnnotatedString = buildAnnotatedString {
                append(secondLineFullText)
                addStyle(
                    style = SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline),
                    start = secondLineFullText.indexOf(basicVersionText),
                    end = secondLineFullText.indexOf(basicVersionText) + basicVersionText.length
                )
            }
            
            ClickableText(
                text = secondLineAnnotatedString,
                onClick = { offset ->
                    val bvStart = secondLineFullText.indexOf(basicVersionText)
                    val bvEnd = bvStart + basicVersionText.length
                    
                    if (offset in bvStart..bvEnd) {
                        println("[PrivacyDialog] 用户点击设置 - 基础版掘金")
                        onBasicVersionClick()
                    }
                },
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "1、我们可能会申请相机（摄像头）、相册(存储)权限，以实现设置、更换头像、完成应用升级。\n\n2、我们可能会申请电话权限，以保障软件服务的安全运营及效率、判断帐户。",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAccept,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = linkColor),
                shape = RoundedCornerShape(4.dp),
            ) {
                Text("同意", color = Color.White)
            }

            TextButton(onClick = onDecline) {
                Text("退出应用", color = Color.Gray)
            }
        }
    }
}
