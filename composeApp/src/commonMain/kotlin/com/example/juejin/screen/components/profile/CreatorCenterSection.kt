package com.example.juejin.screen.components.profile

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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.People
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
import com.example.juejin.ui.Colors
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.profile_creator_center
import juejin.composeapp.generated.resources.profile_enter_homepage
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreatorCenterSection() {
    Column(Modifier.fillMaxWidth().background(Color.White).padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(Res.string.profile_creator_center),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                stringResource(Res.string.profile_enter_homepage),
                fontSize = 12.sp,
                color = Colors.Text.secondary
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            CreatorCenterItem(Icons.Default.BarChart, "内容数据")
            CreatorCenterItem(Icons.Default.People, "粉丝数据")
            CreatorCenterItem(Icons.Default.Campaign, "创作活动")
            CreatorCenterItem(Icons.Default.Drafts, "草稿箱")
        }
    }
}

@Composable
private fun CreatorCenterItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = { println("点击了 $label") })) {
        Icon(icon, label, Modifier.size(24.dp))
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp)
    }
}

