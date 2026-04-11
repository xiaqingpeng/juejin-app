package com.example.juejin.test

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.ui.Colors
import com.example.juejin.ui.theme.ThemeColors
import com.example.juejin.ui.components.TopNavigationBarWithBack

/**
 * 测试案例列表页面
 * 双列网格布局展示所有测试案例
 */
@Composable
fun TestListScreen(
    onLeftClick: () -> Unit,
    onTestClick: (TestCase) -> Unit
) {
    val testCases = TestRegistry.getAll()
    
    com.example.juejin.ui.theme.AppTheme {
        Scaffold(
            topBar = {
                TopNavigationBarWithBack(
                    title = "测试案例列表",
                    onLeftClick = onLeftClick
                )
            }
        ) { padding ->
            if (testCases.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "暂无测试案例",
                        color = ThemeColors.Text.secondary,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(ThemeColors.Background.primary),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(testCases) { testCase ->
                        TestCaseCard(
                            testCase = testCase,
                            onClick = { onTestClick(testCase) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 测试案例卡片
 */
@Composable
private fun TestCaseCard(
    testCase: TestCase,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = ThemeColors.primaryWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = testCase.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ThemeColors.Text.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            if (testCase.description.isNotEmpty()) {
                Text(
                    text = testCase.description,
                    fontSize = 12.sp,
                    color = ThemeColors.Text.secondary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
