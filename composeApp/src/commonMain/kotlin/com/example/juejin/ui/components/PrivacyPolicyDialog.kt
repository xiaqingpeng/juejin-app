package com.example.juejin.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.juejin.platform.openUrl
import com.example.juejin.ui.Colors
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.privacy_dialog_agree
import juejin.composeapp.generated.resources.privacy_dialog_basic_version
import juejin.composeapp.generated.resources.privacy_dialog_basic_version_text
import juejin.composeapp.generated.resources.privacy_dialog_exit
import juejin.composeapp.generated.resources.privacy_dialog_full_text
import juejin.composeapp.generated.resources.privacy_dialog_permission_camera
import juejin.composeapp.generated.resources.privacy_dialog_permission_phone
import juejin.composeapp.generated.resources.privacy_dialog_privacy_policy
import juejin.composeapp.generated.resources.privacy_dialog_user_agreement
import juejin.composeapp.generated.resources.privacy_dialog_welcome
import org.jetbrains.compose.resources.stringResource

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
    val linkColor = Colors.primaryBlue
    
    // URL 定义
    val userAgreementUrl = "https://cdn.lifesense.com/sportsAppWebViews/webpack/simple_page/agreement.html"
    val privacyPolicyUrl = "https://cdn.lifesense.com/common/#/privacyPolicy"

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
                text = stringResource(Res.string.privacy_dialog_welcome),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 第一行：用户协议与隐私政策（可点击）
            val userAgreementText = stringResource(Res.string.privacy_dialog_user_agreement)
            val privacyPolicyText = stringResource(Res.string.privacy_dialog_privacy_policy)
            val firstLineFullText = stringResource(Res.string.privacy_dialog_full_text)
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
                        openUrl(userAgreementUrl)
                    } else if (offset in ppStart..ppEnd) {
                        println("[PrivacyDialog] 用户点击《隐私政策》")
                        openUrl(privacyPolicyUrl)
                    }
                },
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 第二行：基础版掘金（可点击）
            val basicVersionText = stringResource(Res.string.privacy_dialog_basic_version)
            val secondLineFullText = stringResource(Res.string.privacy_dialog_basic_version_text)
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
                text = buildString {
                    append(stringResource(Res.string.privacy_dialog_permission_camera))
                    append("\n\n")
                    append(stringResource(Res.string.privacy_dialog_permission_phone))
                },
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
                Text(stringResource(Res.string.privacy_dialog_agree), color = Colors.primaryWhite)
            }

            TextButton(onClick = onDecline) {
                Text(stringResource(Res.string.privacy_dialog_exit), color = Colors.primaryGray)
            }
        }
    }
}
