package com.example.juejin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
}

class NavigationState {
    private val backStack = mutableListOf<Screen>(Screen.Main)
    var currentScreen by mutableStateOf<Screen>(Screen.Main)
        private set
    
    fun navigate(screen: Screen) {
        backStack.add(screen)
        currentScreen = screen
    }
    
    fun popBackStack(): Boolean {
        if (backStack.size > 1) {
            backStack.removeLast()
            currentScreen = backStack.last()
            return true
        }
        return false
    }
}

@Composable
fun rememberNavigationState(): NavigationState {
    return remember { NavigationState() }
}
