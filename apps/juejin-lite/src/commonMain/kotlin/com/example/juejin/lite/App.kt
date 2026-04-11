package com.example.juejin.lite

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.juejin.ui.theme.AppTheme
import com.example.juejin.ui.theme.ThemeManager
import com.example.juejin.ui.theme.isSystemInDarkTheme

/**
 * 掘金轻量版主应用
 * 只包含核心功能：首页、热门、我的
 */
@Composable
fun App() {
    // 初始化主题
    LaunchedEffect(Unit) {
        ThemeManager.initialize()
    }
    
    // 监听系统主题变化
    val systemDarkMode = isSystemInDarkTheme()
    LaunchedEffect(systemDarkMode) {
        ThemeManager.isSystemDarkMode = systemDarkMode
    }
    
    AppTheme {
        LiteMainScreen()
    }
}

@Composable
fun LiteMainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                LiteTab.entries.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> HotScreen()
                2 -> ProfileScreen()
            }
        }
    }
}

/**
 * 轻量版标签页
 */
enum class LiteTab(val label: String, val icon: ImageVector) {
    HOME("首页", Icons.Default.Home),
    HOT("热门", Icons.Default.Whatshot),
    PROFILE("我的", Icons.Default.Person)
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("首页", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("掘金轻量版 - 核心功能")
    }
}

@Composable
fun HotScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("热门", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("热门内容列表")
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("我的", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("个人中心")
    }
}
