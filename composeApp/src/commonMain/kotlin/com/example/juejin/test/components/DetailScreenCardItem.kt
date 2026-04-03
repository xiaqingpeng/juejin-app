package com.example.juejin.test.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.ui.Colors
import com.example.juejin.theme.ThemeColors
import com.example.juejin.ui.typography.Typography

/** 详情项组件 */
@Composable
fun DetailScreenCardItem(label: String, value: String) {
    Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:", style = Typography.caption, color = ThemeColors.Text.secondary)
        Text(text = value, style = Typography.body, color = ThemeColors.Text.primary)
    }
}
