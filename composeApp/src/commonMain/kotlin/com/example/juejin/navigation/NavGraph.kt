package com.example.juejin.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.juejin.screen.QrScannerScreen
import com.example.juejin.screen.SettingsScreen
import com.example.juejin.viewmodel.UserViewModel

/**
 * 应用导航图
 * 使用简单的状态管理来避免 Navigation Compose 在 iOS 上的兼容性问题
 */
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    navigationState: NavigationState,
    mainContent: @Composable (
        onNavigateToSettings: () -> Unit,
        onNavigateToQrScanner: () -> Unit,
        onNavigateToTestList: () -> Unit
    ) -> Unit
) {
    Box(modifier = modifier) {
        when (val screen = navigationState.currentScreen) {
            is Screen.Main -> {
                mainContent(
                    { navigationState.navigate(Screen.Settings) },
                    { navigationState.navigate(Screen.QrScanner) },
                    { navigationState.navigate(Screen.TestList) }
                )
            }
            
            is Screen.Settings -> {
                SettingsScreen(
                    onLeftClick = { navigationState.popBackStack() },
                    userViewModel = userViewModel,
                    onNavigateToDetail = { settingTitle ->
                        navigationState.navigate(Screen.SettingDetail(settingTitle))
                    },
                    onNavigateToEditProfile = {
                        navigationState.navigate(Screen.EditProfile)
                    }
                )
            }
            
            is Screen.SettingDetail -> {
                com.example.juejin.screen.SettingDetailScreen(
                    settingItem = com.example.juejin.model.SettingItem(
                        title = screen.settingTitle,
                        type = com.example.juejin.model.SettingType.NORMAL
                    ),
                    onLeftClick = { navigationState.popBackStack() },
                    onNavigateToDeviceInfo = {
                        navigationState.navigate(Screen.DeviceInfo)
                    }
                )
            }
            
            is Screen.EditProfile -> {
                com.example.juejin.screen.EditProfileDetailScreen(
                    onLeftClick = { navigationState.popBackStack() },
                    viewModel = userViewModel
                )
            }
            
            is Screen.QrScanner -> {
                QrScannerScreen(
                    onBack = { navigationState.popBackStack() },
                    onQrCodeScanned = { /* 扫码结果在 QrScannerScreen 内部处理 */ }
                )
            }
            
            is Screen.TestList -> {
                com.example.juejin.test.TestListScreen(
                    onLeftClick = { navigationState.popBackStack() },
                    onTestClick = { testCase ->
                        when (testCase.id) {
                            "test_course_list" -> navigationState.navigate(Screen.CourseList)
                            "test_charts" -> navigationState.navigate(Screen.ChartTest)
                            "test_webview" -> navigationState.navigate(Screen.WebViewTest)
                            else -> navigationState.navigate(Screen.TestDetail(testCase.id))
                        }
                    }
                )
            }
            
            is Screen.TestDetail -> {
                val testCase = com.example.juejin.test.TestRegistry.getTestCase(screen.testId)
                if (testCase != null) {
                    com.example.juejin.test.TestDetailScreen(
                        testCase = testCase,
                        onLeftClick = { navigationState.popBackStack() }
                    )
                }
            }
            
            is Screen.CourseList -> {
                com.example.juejin.test.CourseListScreen(
                    onLeftClick = { navigationState.popBackStack() },
                    onItemClick = { logStat ->
                        val courseId = logStat.id?.toString() ?: "0"
                        navigationState.navigate(Screen.CourseDetail(courseId))
                    }
                )
            }
            
            is Screen.CourseDetail -> {
                val course = com.example.juejin.test.TestRegistry.getCourse(screen.courseId)
                if (course != null) {
                    com.example.juejin.test.CourseDetailScreen(
                        logStat = course,
                        onLeftClick = { navigationState.popBackStack() }
                    )
                }
            }
            
            is Screen.ChartTest -> {
                com.example.juejin.test.ChartTestScreen(
                    onLeftClick = { navigationState.popBackStack() }
                )
            }
            
            is Screen.WebViewTest -> {
                com.example.juejin.test.WebViewTestScreen(
                    onLeftClick = { navigationState.popBackStack() }
                )
            }
            
            is Screen.WebView -> {
                com.example.juejin.ui.components.WebViewScreen(
                    title = screen.title,
                    url = screen.url,
                    onLeftClick = { navigationState.popBackStack() }
                )
            }
            
            is Screen.DeviceInfo -> {
                com.example.juejin.screen.DeviceInfoScreen(
                    onLeftClick = { navigationState.popBackStack() }
                )
            }
        }
    }
}
