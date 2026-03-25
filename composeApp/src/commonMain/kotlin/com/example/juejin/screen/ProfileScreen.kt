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
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juejin.model.UserInfo
import com.example.juejin.ui.Colors
import com.example.juejin.ui.components.TopNavigationBar
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.learn_more
import juejin.composeapp.generated.resources.profile_creator_center
import juejin.composeapp.generated.resources.profile_enter_homepage
import juejin.composeapp.generated.resources.profile_more_functions
import juejin.composeapp.generated.resources.profile_personal_homepage
import juejin.composeapp.generated.resources.tab_profile
import juejin.composeapp.generated.resources.view_more
import org.jetbrains.compose.resources.stringResource

// TODO: Implement onSettingsClick functionality



@Composable
fun ProfileScreen(
    onSettingsClick: () -> Unit = {},
    userViewModel: com.example.juejin.viewmodel.UserViewModel = com.example.juejin.viewmodel.UserViewModel()
) {
    JuejinProfilePage(
        onSettingsClick = onSettingsClick,
        userViewModel = userViewModel
    )
}

@Composable
fun JuejinProfilePage(
    onSettingsClick: () -> Unit = {},
    userViewModel: com.example.juejin.viewmodel.UserViewModel = com.example.juejin.viewmodel.UserViewModel()
) {
    val user by userViewModel.user.collectAsStateWithLifecycle()
    MaterialTheme(
        colorScheme = lightColorScheme(
            background = Colors.Background.primary,
            surface = Colors.Background.surface
        )
    ) {
        Scaffold(
            topBar = {
                TopNavigationBar(
                    title = stringResource(Res.string.tab_profile),
                    onRightClick = { onSettingsClick()  },
                    rightIcon = Icons.Filled.Settings
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Colors.Background.primary)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {
                // 用户信息
                ProfileHeader(user)

                // 分组间距
                Spacer(modifier = Modifier.height(8.dp).fillMaxWidth().background(Colors.Background.primary))

                // 会员横幅
                MemberBanner()

                // 分组间距
                Spacer(modifier = Modifier.height(8.dp).fillMaxWidth().background(Colors.Background.primary))

                // 快捷功能
                QuickFunctionSection()

                // 分组间距
                Spacer(modifier = Modifier.height(8.dp).fillMaxWidth().background(Colors.Background.primary))

                // 创作者中心
                CreatorCenterSection()

                // 分组间距
                Spacer(modifier = Modifier.height(8.dp).fillMaxWidth().background(Colors.Background.primary))

                // 更多功能
                MoreFunctionSection()
            }
        }
    }
}

@Composable
private fun ProfileHeader(user: com.example.juejin.model.User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Colors.Background.surface)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 头像
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Colors.UI.avatar),
                contentAlignment = Alignment.Center
            ) {
                if (user.avatar.isNotEmpty()) {
                    // TODO: 使用 AsyncImage 加载头像
                    Text("QP", color = Colors.Text.darkGray, fontSize = 20.sp)
                } else {
                    Text("QP", color = Colors.Text.darkGray, fontSize = 20.sp)
                }
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(
                    user.username,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Row {
                    Surface(
                        modifier = Modifier.height(20.dp),
                        color = Colors.UI.levelBadge,
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text(
                            user.level,
                            fontSize = 10.sp,
                            color = Colors.UI.levelText,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(Res.string.view_more), fontSize = 12.sp, color = Colors.Text.secondary)
                }
            }

            Spacer(Modifier.weight(1f))
            Text(stringResource(Res.string.profile_personal_homepage), fontSize = 12.sp, color = Colors.Text.secondary)
        }

        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ProfileStatItem("点赞", user.likeCount.toString())
            ProfileStatItem("收藏", user.collectCount.toString())
            ProfileStatItem("关注", user.followCount.toString())
        }
    }
}

@Composable
private fun ProfileStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 12.sp, color = Colors.Text.secondary)
    }
}

@Composable
private fun MemberBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Colors.UI.memberBanner),
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
                    color = Colors.Text.white,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {},
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Colors.UI.memberButton),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(stringResource(Res.string.learn_more), fontSize = 12.sp, color = Colors.UI.memberButtonText)
                }
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
            QuickFunctionItem(Icons.Default.CheckCircle, "每日签到", Colors.QuickFunctions.checkIn)
            QuickFunctionItem(Icons.Default.Casino, "幸运转盘", Colors.QuickFunctions.luckyWheel)
            QuickFunctionItem(Icons.Default.BugReport, "Bug挑战赛", Colors.QuickFunctions.bugChallenge)
            QuickFunctionItem(Icons.Default.Star, "福利兑换", Colors.QuickFunctions.welfare)
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
            Text(stringResource(Res.string.profile_creator_center), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(stringResource(Res.string.profile_enter_homepage), fontSize = 12.sp, color = Colors.Text.secondary)
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
        Text(stringResource(Res.string.profile_more_functions), fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
