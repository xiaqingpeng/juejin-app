package com.example.juejin.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.juejin.model.User

import com.example.juejin.ui.Colors
import com.example.juejin.theme.ThemeColors
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.profile_personal_homepage
import juejin.composeapp.generated.resources.view_more
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileHeader(user: User) {
    Column(
        modifier = Modifier.fillMaxWidth().background(ThemeColors.primaryWhite).padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // 头像
            Box(
                modifier = Modifier.size(60.dp).clip(CircleShape).background(ThemeColors.UI.avatar),
                contentAlignment = Alignment.Center
            ) {
                if (user.avatar.isNotEmpty()) {
                    AsyncImage(
                        model = user.avatar,
                        contentDescription = "用户头像",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // 默认显示用户名首字母
                    val initials = if (user.username.isNotEmpty()) {
                        user.username.take(2).uppercase()
                    } else {
                        "QP"
                    }
                    Text(initials, color = ThemeColors.Text.darkGray, fontSize = 20.sp)
                }
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(user.username, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ThemeColors.Text.primary)
                Spacer(Modifier.height(4.dp))
                Row {
                    Surface(
                        modifier = Modifier.height(20.dp),
                        color = ThemeColors.UI.levelBadge,
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text(
                            user.level,
                            fontSize = 10.sp,
                            color = ThemeColors.UI.levelText,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        stringResource(Res.string.view_more),
                        fontSize = 12.sp,
                        color = ThemeColors.Text.secondary
                    )
                }
            }

            Spacer(Modifier.weight(1f))
            Text(
                stringResource(Res.string.profile_personal_homepage),
                fontSize = 12.sp,
                color = ThemeColors.Text.secondary
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            ProfileStatItem(
                "点赞",
                user.likeCount.toString()
            )
            ProfileStatItem(
                "收藏",
                user.collectCount.toString()
            )
            ProfileStatItem(
                "关注",
                user.followCount.toString()
            )
        }
    }
}

@Composable
private fun ProfileStatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = { println("点击了 $label-$value") })
    ) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ThemeColors.Text.primary)
        Text(label, fontSize = 12.sp, color = ThemeColors.Text.secondary)
    }
}
