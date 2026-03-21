package com.example.juejin.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.notification_dialog_allow
import juejin.composeapp.generated.resources.notification_dialog_deny
import juejin.composeapp.generated.resources.notification_dialog_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationPermissionDialog(
    onDismiss: () -> Unit,
    onAllow: () -> Unit,
    onDeny: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(0.85f),
        containerColor = Color(0xFFF2F2F2),
        titleContentColor = Color.Black,
        textContentColor = Color.Black,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        confirmButton = {
            TextButton(
                onClick = onAllow,
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF007AFF))
            ) {
                Text(
                    text = stringResource(Res.string.notification_dialog_allow),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDeny,
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF007AFF))
            ) {
                Text(
                    text = stringResource(Res.string.notification_dialog_deny),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        },
        title = {
            Text(
                text = stringResource(Res.string.notification_dialog_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    )
}
