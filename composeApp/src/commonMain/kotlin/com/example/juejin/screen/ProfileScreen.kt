package com.example.juejin.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.model.UserInfo
import com.example.juejin.ui.components.TopNavigationBar
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.tab_profile
import org.jetbrains.compose.resources.stringResource

// TODO: Implement onSettingsClick functionality



@Composable
fun ProfileScreen(onSettingsClick: () -> Unit = {}) {
    JuejinProfilePage()
    // TODO: Implement onSettingsClick functionality
}

@Composable
fun JuejinProfilePage(userInfo: UserInfo = UserInfo()) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Color(0xFFF5F5F5),
            surface = Color.White
        )
    ) {
        Scaffold(
            topBar = {
                TopNavigationBar(
                    title = stringResource(Res.string.tab_profile),
                    onRightClick = { /* onSettingsClick() */ },
                    rightIcon = Icons.Filled.Settings
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {
                // 用户信息
                ProfileHeader(userInfo)

                // 会员横幅
                MemberBanner()

                // 快捷功能
                QuickFunctionSection()

                // 创作者中心
                CreatorCenterSection()

                // 更多功能
                MoreFunctionSection()
            }
        }
    }
}

@Composable
private fun ProfileHeader(userInfo: UserInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 头像
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Text("QP", color = Color.DarkGray, fontSize = 20.sp)
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    userInfo.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Row {
                    Surface(
                        modifier = Modifier.height(20.dp),

                        color = Color(0xFFE8F4FF),
                        shape = RoundedCornerShape(10.dp),

                    ) {
                        Text(
                            userInfo.level,
                            fontSize = 10.sp,
                            color = Color(0xFF1377EB),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("0 徽章 >", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(Modifier.weight(1f))
            Text("个人主页 >", fontSize = 12.sp, color = Color.Gray)
        }

        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ProfileStatItem("点赞", userInfo.likeCount.toString())
            ProfileStatItem("收藏", userInfo.collectCount.toString())
            ProfileStatItem("关注", userInfo.followCount.toString())
        }
    }
}

@Composable
private fun ProfileStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
private fun MemberBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C54)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "年度会员限时五折 领小册周边福利",
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {},
                modifier = Modifier.height(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("了解一下", fontSize = 12.sp, color = Color.Black)
            }
        }
    }
}

@Composable
private fun QuickFunctionSection() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            QuickFunctionItem(Icons.Default.CheckCircle, "每日签到", Color(0xFF1377EB))
            QuickFunctionItem(Icons.Default.Casino, "幸运转盘", Color(0xFFFF9800))
            QuickFunctionItem(Icons.Default.BugReport, "Bug挑战赛", Color(0xFF9C27B0))
            QuickFunctionItem(Icons.Default.Star, "福利兑换", Color(0xFFE91E63))
        }
    }
}

@Composable
private fun QuickFunctionItem(icon: ImageVector, label: String, tint: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, Modifier.size(24.dp), tint = tint)
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp)
    }
}

@Composable
private fun CreatorCenterSection() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("创作者中心", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("进入首页 >", fontSize = 12.sp, color = Color.Gray)
        }
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CreatorCenterItem(Icons.Default.BarChart, "内容数据")
            CreatorCenterItem(Icons.Default.People, "粉丝数据")
            CreatorCenterItem(Icons.Default.Campaign, "创作活动")
            CreatorCenterItem(Icons.Default.Drafts, "草稿箱")
        }
    }
}

@Composable
private fun CreatorCenterItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, Modifier.size(24.dp))
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp)
    }
}

@Composable
private fun MoreFunctionSection() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("更多功能", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MoreFunctionItem(Icons.Default.School, "课程中心")
            MoreFunctionItem(Icons.Default.Share, "推广中心")
            MoreFunctionItem(Icons.Default.LocalOffer, "我的优惠券")
            MoreFunctionItem(Icons.Default.Group, "我的圈子")
        }

        Spacer(Modifier.height(24.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MoreFunctionItem(Icons.Default.Star, "阅读记录")
            MoreFunctionItem(Icons.AutoMirrored.Filled.Label, "标签管理")
            MoreFunctionItem(Icons.AutoMirrored.Filled.EventNote, "我的报名")
            MoreFunctionItem(Icons.Default.Feedback, "意见反馈")
        }
    }
}

@Composable
private fun MoreFunctionItem(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, Modifier.size(24.dp))
        Spacer(Modifier.height(4.dp))
        Text(label, fontSize = 12.sp)
    }
}
