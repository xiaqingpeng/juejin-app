package com.example.juejin.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.juejin.screen.QrScannerScreen
import com.example.juejin.screen.SettingsScreen
import com.example.juejin.viewmodel.UserViewModel

/**
 * 应用导航图
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    mainContent: @Composable () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Main,
        modifier = modifier
    ) {
        // 主页面（包含底部导航的 Tab 页面）
        composable<NavRoutes.Main> {
            mainContent()
        }
        
        // 设置页面
        composable<NavRoutes.Settings> {
            Box(modifier = Modifier.fillMaxSize()) {
                SettingsScreen(
                    onLeftClick = { navController.navigateUp() },
                    userViewModel = userViewModel,
                    onNavigateToDetail = { settingTitle ->
                        navController.navigate(NavRoutes.SettingDetail(settingTitle))
                    },
                    onNavigateToEditProfile = {
                        navController.navigate(NavRoutes.EditProfile)
                    }
                )
            }
        }
        
        // 设置详情页面
        composable<NavRoutes.SettingDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<NavRoutes.SettingDetail>()
            Box(modifier = Modifier.fillMaxSize()) {
                com.example.juejin.screen.SettingDetailScreen(
                    settingItem = com.example.juejin.model.SettingItem(
                        title = args.settingTitle,
                        type = com.example.juejin.model.SettingType.NORMAL
                    ),
                    onLeftClick = { navController.navigateUp() }
                )
            }
        }
        
        // 编辑资料页面
        composable<NavRoutes.EditProfile> {
            Box(modifier = Modifier.fillMaxSize()) {
                com.example.juejin.screen.EditProfileDetailScreen(
                    onLeftClick = { navController.navigateUp() },
                    viewModel = userViewModel
                )
            }
        }
        
        // 二维码扫描页面
        composable<NavRoutes.QrScanner> {
            Box(modifier = Modifier.fillMaxSize()) {
                QrScannerScreen(
                    onBack = { navController.navigateUp() },
                    onQrCodeScanned = { /* 扫码结果在 QrScannerScreen 内部处理 */ }
                )
            }
        }
        
        // 测试列表页面
        composable<NavRoutes.TestList> {
            Box(modifier = Modifier.fillMaxSize()) {
                com.example.juejin.test.TestListScreen(
                    onLeftClick = { navController.navigateUp() },
                    onTestClick = { testCase ->
                        when (testCase.id) {
                            "test_course_list" -> navController.navigate(NavRoutes.CourseList)
                            "test_charts" -> navController.navigate(NavRoutes.ChartTest)
                            else -> navController.navigate(NavRoutes.TestDetail(testCase.id))
                        }
                    }
                )
            }
        }
        
        // 测试详情页面
        composable<NavRoutes.TestDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<NavRoutes.TestDetail>()
            val testCase = com.example.juejin.test.TestRegistry.getTestCase(args.testId)
            
            if (testCase != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    com.example.juejin.test.TestDetailScreen(
                        testCase = testCase,
                        onLeftClick = { navController.navigateUp() }
                    )
                }
            }
        }
        
        // 课程列表页面
        composable<NavRoutes.CourseList> {
            Box(modifier = Modifier.fillMaxSize()) {
                com.example.juejin.test.CourseListScreen(
                    onLeftClick = { navController.navigateUp() },
                    onItemClick = { logStat ->
                        // 使用 id 作为课程标识
                        val courseId = logStat.id?.toString() ?: "0"
                        navController.navigate(NavRoutes.CourseDetail(courseId))
                    }
                )
            }
        }
        
        // 课程详情页面
        composable<NavRoutes.CourseDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<NavRoutes.CourseDetail>()
            // 从 TestRegistry 获取课程数据
            val course = com.example.juejin.test.TestRegistry.getCourse(args.courseId)
            
            if (course != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    com.example.juejin.test.CourseDetailScreen(
                        logStat = course,
                        onLeftClick = { navController.navigateUp() }
                    )
                }
            }
        }
        
        // 图表测试页面
        composable<NavRoutes.ChartTest> {
            Box(modifier = Modifier.fillMaxSize()) {
                com.example.juejin.test.ChartTestScreen(
                    onLeftClick = { navController.navigateUp() }
                )
            }
        }
    }
}
