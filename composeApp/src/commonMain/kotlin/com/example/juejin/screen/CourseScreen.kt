package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.ui.Colors
import com.example.juejin.ui.typography.Typography
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.tab_courses
import org.jetbrains.compose.resources.stringResource

/**
 * 课程页面（简化版，与首页类似）
 * 完整的课程列表功能已迁移到测试区域
 */
@Composable
fun CourseScreen(onItemClick: (com.example.juejin.model.LogStatsItem) -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier =
                Modifier.size(64.dp)
                    .background(Colors.primaryBlue, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Filled.School,
                contentDescription = stringResource(Res.string.tab_courses),
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            stringResource(Res.string.tab_courses), 
            style = Typography.largeTitle, 
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
