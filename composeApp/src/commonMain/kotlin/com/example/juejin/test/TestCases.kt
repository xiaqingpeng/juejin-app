package com.example.juejin.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.juejin.ui.Colors
// ColorGridScreen is in the same package, no import needed
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.loading
import juejin.composeapp.generated.resources.test_click_count
import juejin.composeapp.generated.resources.test_click_me
import juejin.composeapp.generated.resources.test_color_blue
import juejin.composeapp.generated.resources.test_color_green
import juejin.composeapp.generated.resources.test_color_red
import juejin.composeapp.generated.resources.test_random_username
import juejin.composeapp.generated.resources.test_reset_user
import org.jetbrains.compose.resources.stringResource

/**
 * 注册所有测试案例
 */
fun registerTestCases() {
    // 示例测试案例 1
    TestRegistry.register(
        TestCase(
            id = "test_button",
            title = "按钮测试",
            description = "测试按钮点击和状态变化"
        ) {
            var count by remember { mutableStateOf(0) }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement=Arrangement.Center
            ) {
                Text("点击次数: $count", fontSize = 24.sp)
                Button(
                    onClick = { count++ },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(stringResource(Res.string.test_click_me))
                }
            }
        }
    )
    
    // 示例测试案例 2
    TestRegistry.register(
        TestCase(
            id = "test_colors",
            title = "颜色测试",
            description = "测试不同颜色的显示效果"
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(Res.string.test_color_red), color = Colors.Text.white, fontSize = 20.sp)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(Color.Green),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(Res.string.test_color_green), color = Colors.Text.white, fontSize = 20.sp)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(Color.Blue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(Res.string.test_color_blue), color = Colors.Text.white, fontSize = 20.sp)
                }
            }
        }
    )
    
    // 示例测试案例 3
    TestRegistry.register(
        TestCase(
            id = "test_text",
            title = "文本测试",
            description = "测试不同字体大小和样式"
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("12sp 文本", fontSize = 12.sp)
                Text("16sp 文本", fontSize = 16.sp)
                Text("20sp 文本", fontSize = 20.sp)
                Text("24sp 文本", fontSize = 24.sp)
                Text("32sp 文本", fontSize = 32.sp)
            }
        }
    )
    
    // 测试案例 4：用户信息编辑测试
    TestRegistry.register(
        TestCase(
            id = "test_user_profile",
            title = "用户信息测试",
            description = "测试用户信息的显示和编辑功能"
        ) {
            val viewModel = com.example.juejin.viewmodel.UserViewModel()
            val user by viewModel.user.collectAsStateWithLifecycle()
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("用户信息测试", fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
                
                Text("用户名: ${user.username}", fontSize = 16.sp)
                Text("职位: ${user.position}", fontSize = 16.sp)
                Text("公司: ${user.company}", fontSize = 16.sp)
                Text("简介: ${user.bio}", fontSize = 16.sp)
                Text("等级: ${user.level}", fontSize = 16.sp)
                Text("点赞数: ${user.likeCount}", fontSize = 16.sp)
                Text("收藏数: ${user.collectCount}", fontSize = 16.sp)
                Text("关注数: ${user.followCount}", fontSize = 16.sp)
                
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { 
                        viewModel.updateUsername("测试用户${kotlin.random.Random.nextInt(0, 100)}")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.test_random_username))
                }
                
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = { 
                        viewModel.loadUserInfo()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.test_reset_user))
                }
            }
        }
    )
    
    // 测试案例 5：课程列表（导航入口）
    TestRegistry.register(
        TestCase(
            id = "test_course_list",
            title = "课程列表",
            description = "完整的课程列表页面，包含多平台标签切换"
        ) {
            // 这个测试案例只作为导航入口，实际内容在 CourseListScreen 中
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(Res.string.loading), fontSize = 16.sp)
            }
        }
    )
    
    // 测试案例 6：定时颜色变化卡片
    TestRegistry.register(
        TestCase(
            id = "test_color_grid",
            title = "定时颜色变化卡片",
            description = "随机改变卡片颜色"
        ) {
            ColorGridScreen()
        }
    )
    
    // 测试案例 7：图表数据展示
    TestRegistry.register(
        TestCase(
            id = "test_charts",
            title = "图表数据展示",
            description = "展示多种图表类型：柱状图、饼图、折线图，数据来源于课程列表接口"
        ) {
            // 这个测试案例只作为导航入口，实际内容在 ChartTestScreen 中
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(Res.string.loading), fontSize = 16.sp)
            }
        }
    )
    
    // 测试案例 8：WebView 测试
    TestRegistry.register(
        TestCase(
            id = "test_webview",
            title = "WebView 测试",
            description = "使用 WebView 打开网页，测试跨平台 WebView 组件"
        ) {
            // 这个测试案例只作为导航入口，实际内容在 WebViewTestScreen 中
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(Res.string.loading), fontSize = 16.sp)
            }
        }
    )
    
    // 测试案例 9：角标测试
    TestRegistry.register(
        TestCase(
            id = "test_badge",
            title = "角标测试",
            description = "测试跨平台应用角标功能，支持设置、清除和获取角标数量"
        ) {
            // 这个测试案例只作为导航入口，实际内容在 BadgeTestScreen 中
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(Res.string.loading), fontSize = 16.sp)
            }
        }
    )
    
    // 可以继续添加更多测试案例...
}
