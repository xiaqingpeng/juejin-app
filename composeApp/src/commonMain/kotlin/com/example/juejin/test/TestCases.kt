package com.example.juejin.test

import androidx.compose.foundation.background
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("点击次数: $count", fontSize = 24.sp)
                Button(
                    onClick = { count++ },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("点击我")
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
                    Text("红色", color = Color.White, fontSize = 20.sp)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(Color.Green),
                    contentAlignment = Alignment.Center
                ) {
                    Text("绿色", color = Color.White, fontSize = 20.sp)
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(Color.Blue),
                    contentAlignment = Alignment.Center
                ) {
                    Text("蓝色", color = Color.White, fontSize = 20.sp)
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
                        viewModel.updateUsername("测试用户${(0..100).random()}")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("随机更改用户名")
                }
                
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = { 
                        viewModel.loadUserInfo()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("重置用户信息")
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
                Text("正在加载...", fontSize = 16.sp)
            }
        }
    )
    
    // 可以继续添加更多测试案例...
}
