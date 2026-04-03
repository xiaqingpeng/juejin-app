package com.example.juejin.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.platform.ImageSourceType
import com.example.juejin.ui.Colors
import com.example.juejin.theme.ThemeColors
import com.example.juejin.util.Logger

/**
 * 图片来源选择底部弹窗
 * 让用户选择从相册选择或拍照
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSourceBottomSheet(
    onDismiss: () -> Unit,
    onSourceSelected: (ImageSourceType) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ThemeColors.primaryWhite,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 拍照选项
            Text(
                text = "拍照",
                fontSize = 18.sp,
                color = ThemeColors.Text.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Logger.d("ImageSourceBottomSheet", "点击了拍照选项")
                        onSourceSelected(ImageSourceType.CAMERA)
                        onDismiss()
                    }
                    .padding(vertical = 16.dp)
            )
            
            HorizontalDivider(color = ThemeColors.UI.divider)
            
            // 从相册选择
            Text(
                text = "从相册选择",
                fontSize = 18.sp,
                color = ThemeColors.Text.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Logger.d("ImageSourceBottomSheet", "点击了相册选项")
                        onSourceSelected(ImageSourceType.GALLERY)
                        onDismiss()
                    }
                    .padding(vertical = 16.dp)
            )
            
            HorizontalDivider(
                color = ThemeColors.Background.primary,
                thickness = 8.dp
            )
            
            // 取消按钮
            Text(
                text = "取消",
                fontSize = 18.sp,
                color = ThemeColors.Text.secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDismiss() }
                    .padding(vertical = 16.dp)
            )
        }
    }
}
