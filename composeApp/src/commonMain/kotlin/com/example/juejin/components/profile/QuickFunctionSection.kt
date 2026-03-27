package com.example.juejin.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.ui.Colors

@Composable
fun QuickFunctionSection() {
    Column(Modifier.fillMaxWidth().background(Color.White).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            QuickFunctionItem(
                Icons.Default.CheckCircle,
                "每日签到",
                Colors.QuickFunctions.checkIn
            )
            QuickFunctionItem(
                Icons.Default.Casino,
                "幸运转盘",
                Colors.QuickFunctions.luckyWheel
            )
            QuickFunctionItem(
                Icons.Default.BugReport,
                "Bug挑战赛",
                Colors.QuickFunctions.bugChallenge
            )
            QuickFunctionItem(
                Icons.Default.Star,
                "福利兑换",
                Colors.QuickFunctions.welfare
            )
        }
    }
}

@Composable
private fun QuickFunctionItem(icon: ImageVector, label: String, tint: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = { println("点击了 $label") })) {
        Icon(icon, label, Modifier.size(24.dp), tint = tint)
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp)
    }
}