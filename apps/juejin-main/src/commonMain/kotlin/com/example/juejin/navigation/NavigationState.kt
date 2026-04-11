package com.example.juejin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * 简单的导航状态管理
 * 使用状态栈来管理页面导航，避免使用 Navigation Compose 在 iOS 上的兼容性问题
 */
sealed class Screen {
    object Main : Screen()
    object Settings : Screen()
    data class SettingDetail(val settingTitle: String) : Screen()
    object EditProfile : Screen()
    object QrScanner : Screen()
    object TestList : Screen()
    data class TestDetail(val testId: String) : Screen()
    object CourseList : Screen()
    data class CourseDetail(val courseId: String) : Screen()
    object ChartTest : Screen()
    object WebViewTest : Screen()
    object BadgeTest : Screen()
    data class WebView(val title: String, val url: String) : Screen()
    object DeviceInfo : Screen()
}

class NavigationState {
    private val backStack = mutableStateListOf<Screen>(Screen.Main)
    var currentScreen by mutableStateOf<Screen>(Screen.Main)
        private set
    
    fun navigate(screen: Screen) {
        com.example.juejin.util.Logger.d("NavigationState", "导航到: $screen, 当前栈大小: ${backStack.size}")
        backStack.add(screen)
        currentScreen = screen
        com.example.juejin.util.Logger.d("NavigationState", "导航后栈大小: ${backStack.size}, 当前页面: $currentScreen")
    }
    
    fun popBackStack(): Boolean {
        com.example.juejin.util.Logger.d("NavigationState", "尝试返回, 当前栈大小: ${backStack.size}, 当前页面: $currentScreen")
        if (backStack.size > 1) {
            val removedScreen = backStack.removeAt(backStack.size - 1)
            currentScreen = backStack.last()
            com.example.juejin.util.Logger.d("NavigationState", "返回成功, 移除: $removedScreen, 新页面: $currentScreen, 剩余栈大小: ${backStack.size}")
            return true
        }
        com.example.juejin.util.Logger.w("NavigationState", "无法返回, 已在根页面")
        return false
    }
    
    fun printBackStack() {
        com.example.juejin.util.Logger.d("NavigationState", "当前导航栈: ${backStack.joinToString(" -> ")}")
    }
}

@Composable
fun rememberNavigationState(): NavigationState {
    return remember { NavigationState() }
}
