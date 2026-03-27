package com.example.juejin.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.ui.Colors
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.learn_more
import org.jetbrains.compose.resources.stringResource

@Composable
fun MemberBanner() {
    Box(modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Colors.UI.memberBanner),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "年度会员限时五折 领小册周边福利",
                    fontSize = 14.sp,
                    color = Colors.Text.white,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {},
                    modifier = Modifier.height(32.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Colors.UI.memberButton
                        ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        stringResource(Res.string.learn_more),
                        fontSize = 12.sp,
                        color = Colors.UI.memberButtonText
                    )
                }
            }
        }
    }
}