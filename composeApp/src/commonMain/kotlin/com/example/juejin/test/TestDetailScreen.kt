package com.example.juejin.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.TopNavigationBarWithBack

/**
 * 测试案例详情页面
 * 显示具体的测试内容
 */
@Composable
fun TestDetailScreen(
    testCase: TestCase,
    onLeftClick: () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Colors.Background.primary,
            surface = Colors.primaryWhite
        )
    ) {
        Scaffold(
            topBar = {
                TopNavigationBarWithBack(
                    title = testCase.title,
                    onLeftClick = onLeftClick
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Colors.Background.primary),
                contentAlignment = Alignment.Center
            ) {
                // 渲染测试案例的内容
                testCase.content()
            }
        }
    }
}
