package com.example.juejin.lite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.juejin.lite.di.AppContainer
import com.example.juejin.lite.navigation.NavigationState
import com.example.juejin.lite.navigation.Screen
import com.example.juejin.lite.navigation.rememberNavigationState
import com.example.juejin.ui.components.AppTabBar
import com.example.juejin.ui.components.TabItem
import com.example.juejin.ui.theme.AppTheme
import com.example.juejin.ui.theme.ThemeManager
import com.example.juejin.ui.theme.isSystemInDarkTheme
import juejin.lite.generated.resources.Res
import juejin.lite.generated.resources.tab_category
import juejin.lite.generated.resources.tab_home
import juejin.lite.generated.resources.tab_message
import juejin.lite.generated.resources.tab_profile
import juejin.lite.generated.resources.tab_shopping_cart

/**
 * 掘金轻量版主应用
 * 采用 MVVM 架构：首页、分类、消息、购物车、个人中心
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
    
    // 导航状态
    val navigationState = rememberNavigationState()
    
    // 登录状态
    var isLoggedIn by remember { mutableStateOf(false) }
    
    // 记住从哪个标签页跳转到登录的
    var returnToTab by remember { mutableStateOf(4) } // 默认返回"我的"
    
    // 在 App 层级创建 ViewModels，避免导航时重新创建
    val appContainer = remember { AppContainer.getInstance() }
    val homeViewModel = remember { appContainer.provideHomeViewModel() }
    val categoryViewModel = remember { appContainer.provideCategoryViewModel() }
    val messageViewModel = remember { appContainer.provideMessageViewModel() }
    val cartViewModel = remember { appContainer.provideCartViewModel() }
    val profileViewModel = remember { appContainer.provideProfileViewModel() }
    
    AppTheme {
        when (navigationState.currentScreen) {
            is Screen.Main -> {
                LiteMainScreen(
                    isLoggedIn = isLoggedIn,
                    onNavigateToLogin = {
                        returnToTab = 4 // 从"我的"页面跳转到登录
                        navigationState.navigate(Screen.Login)
                    },
                    initialTab = returnToTab,
                    homeViewModel = homeViewModel,
                    categoryViewModel = categoryViewModel,
                    messageViewModel = messageViewModel,
                    cartViewModel = cartViewModel,
                    profileViewModel = profileViewModel
                )
            }
            is Screen.Login -> {
                com.example.juejin.lite.presentation.login.LoginScreen(
                    onLoginSuccess = {
                        isLoggedIn = true
                        // 登录成功后静默刷新个人信息（不显示 Loading）
                        profileViewModel.loadProfile(forceRefresh = false)
                        navigationState.popBackStack()
                    },
                    onClose = {
                        navigationState.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun LiteMainScreen(
    isLoggedIn: Boolean,
    onNavigateToLogin: () -> Unit,
    initialTab: Int = 0,
    homeViewModel: com.example.juejin.lite.presentation.home.HomeViewModel,
    categoryViewModel: com.example.juejin.lite.presentation.category.CategoryViewModel,
    messageViewModel: com.example.juejin.lite.presentation.message.MessageViewModel,
    cartViewModel: com.example.juejin.lite.presentation.cart.CartViewModel,
    profileViewModel: com.example.juejin.lite.presentation.profile.ProfileViewModel
) {
    var selectedTab by remember { mutableStateOf(initialTab) }
    
    // 定义轻量版的标签页
    val tabs = listOf(
        TabItem(
            title = Res.string.tab_home,
            icon = Icons.Default.Home
        ),
        TabItem(
            title = Res.string.tab_category,
            icon = Icons.Default.Category
        ),
        TabItem(
            title = Res.string.tab_message,
            icon = Icons.AutoMirrored.Filled.Message
        ),
        TabItem(
            title = Res.string.tab_shopping_cart,
            icon = Icons.Default.ShoppingCart
        ),
        TabItem(
            title = Res.string.tab_profile,
            icon = Icons.Default.Person
        )
    )
    
    Scaffold(
        bottomBar = {
            AppTabBar(
                tabs = tabs,
                selectedIndex = selectedTab,
                onTabSelected = { index -> selectedTab = index },
                selectedColor = Color(0xFFFF6900)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (selectedTab) {
                0 -> com.example.juejin.lite.presentation.home.HomeScreen(viewModel = homeViewModel)
                1 -> com.example.juejin.lite.presentation.category.CategoryScreen(viewModel = categoryViewModel)
                2 -> com.example.juejin.lite.presentation.message.MessageScreen(viewModel = messageViewModel)
                3 -> com.example.juejin.lite.presentation.cart.CartScreen(viewModel = cartViewModel)
                4 -> com.example.juejin.lite.presentation.profile.ProfileScreen(
                    viewModel = profileViewModel,
                    isLoggedIn = isLoggedIn,
                    onLoginClick = onNavigateToLogin
                )
            }
        }
    }
}






