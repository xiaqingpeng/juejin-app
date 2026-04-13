package com.example.juejin.lite.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * 简单的导航状态管理
 * 使用状态栈来管理页面导航
 */
sealed class Screen {
    object Main : Screen()
    object Login : Screen()
}

class NavigationState {
    private val backStack = mutableStateListOf<Screen>(Screen.Main)
    var currentScreen by mutableStateOf<Screen>(Screen.Main)
        private set
    
    fun navigate(screen: Screen) {
        backStack.add(screen)
        currentScreen = screen
    }
    
    fun popBackStack(): Boolean {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
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
