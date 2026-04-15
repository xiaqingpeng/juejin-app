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
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.ui.theme.ThemeColors
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.profile_more_functions
import org.jetbrains.compose.resources.stringResource

@Composable
fun MoreFunctionSection() {
    Column(Modifier.fillMaxWidth().background(ThemeColors.primaryWhite).padding(16.dp)) {
        Text(
            stringResource(Res.string.profile_more_functions),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = ThemeColors.Text.primary
        )
        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            MoreFunctionItem(
                Icons.Default.School,
                "课程中心"
            )
            MoreFunctionItem(
                Icons.Default.Share,
                "推广中心"
            )
            MoreFunctionItem(
                Icons.Default.LocalOffer,
                "我的优惠券"
            )
            MoreFunctionItem(
                Icons.Default.Group,
                "我的圈子"
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            MoreFunctionItem(
                Icons.Default.Star,
                "阅读记录"
            )
            MoreFunctionItem(
                Icons.AutoMirrored.Filled.Label,
                "标签管理"
            )
            MoreFunctionItem(
                Icons.AutoMirrored.Filled.EventNote,
                "我的报名"
            )
            MoreFunctionItem(
                Icons.Default.Feedback,
                "意见反馈"
            )
        }
    }
}

@Composable
private fun MoreFunctionItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = { println("点击了 $label") })) {
        Icon(icon, label, Modifier.size(24.dp), tint = ThemeColors.Text.primary)
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, color = ThemeColors.Text.primary)
    }
}