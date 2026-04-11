package com.example.juejin.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.juejin.ui.typography.Typography

/**
 * 全局可复用的顶部导航栏组件
 *
 * @param title 中间标题文本
 * @param onLeftClick 返回按钮点击回调（为null时不显示返回按钮）
 * @param leftIcon 返回按钮图标（默认为空，使用系统返回图标）
 * @param onRightClick 右侧按钮点击回调（为null时不显示右侧按钮）
 * @param rightIcon 右侧按钮图标
 * @param rightContent 右侧自定义内容（优先级高于rightIcon）
 * @param backgroundColor 背景颜色
 * @param contentColor 内容颜色
 */
@Composable
fun TopNavigationBar(
    title: String,
    onLeftClick: (() -> Unit)? = null,
    leftIcon: ImageVector? = null,
    onRightClick: (() -> Unit)? = null,
    rightIcon: ImageVector? = null,
    rightContent: @Composable (() -> Unit)? = null,
    backgroundColor: Color = com.example.juejin.ui.theme.ThemeColors.primaryWhite,
    contentColor: Color = com.example.juejin.ui.theme.ThemeColors.Text.primary
) {
    Surface(
        color = backgroundColor,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {


            if (onLeftClick != null) {
                IconButton(
                    onClick = onLeftClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    if (leftIcon != null) {
                        Icon(
                            imageVector = leftIcon,
                            contentDescription = "Back",
                            tint = contentColor
                        )
                    } else {
                        // 使用默认返回图标
                        androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = contentColor
                        )
                    }
                }
            }

            // 中间标题
            Text(
                text = title,
                style = Typography.largeTitle,
                color = contentColor,
                modifier = Modifier.align(Alignment.Center)
            )

            // 右侧按钮或自定义内容
            if (rightContent != null) {
                Box(
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    rightContent()
                }
            } else if (onRightClick != null && rightIcon != null) {
                IconButton(
                    onClick = onRightClick,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = rightIcon,
                        contentDescription = "Right Action",
                        tint = contentColor
                    )
                }
            }
        }
    }
}

/**
 * 简化的顶部导航栏 - 只有标题
 */
@Composable
fun TopNavigationBarSimple(
    title: String,
    backgroundColor: Color = com.example.juejin.ui.theme.ThemeColors.primaryWhite,
    contentColor: Color = com.example.juejin.ui.theme.ThemeColors.Text.primary
) {
    TopNavigationBar(
        title = title,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )
}

/**
 * 带返回按钮的顶部导航栏
 */
@Composable
fun TopNavigationBarWithBack(
    title: String,
    onLeftClick: () -> Unit,
    backgroundColor: Color = com.example.juejin.ui.theme.ThemeColors.primaryWhite,
    contentColor: Color = com.example.juejin.ui.theme.ThemeColors.Text.primary
) {
    TopNavigationBar(
        title = title,
        onLeftClick = onLeftClick,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )
}

/**
 * 带右侧按钮的顶部导航栏
 */
@Composable
fun TopNavigationBarWithRightAction(
    title: String,
    onRightClick: () -> Unit,
    rightIcon: ImageVector,
    backgroundColor: Color = com.example.juejin.ui.theme.ThemeColors.primaryWhite,
    contentColor: Color = com.example.juejin.ui.theme.ThemeColors.Text.primary
) {
    TopNavigationBar(
        title = title,
        onRightClick = onRightClick,
        rightIcon = rightIcon,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )
}
