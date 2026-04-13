package com.example.juejin.lite.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.juejin.lite.domain.model.Article
import com.example.juejin.lite.domain.model.UserProfile
import com.example.juejin.ui.theme.ThemeColors
import juejin.lite.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    isLoggedIn: Boolean = false,
    onLoginClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        when (val state = uiState) {
            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFFF6900))
                }
            }
            
            is ProfileUiState.Success -> {
                ProfileContent(
                    profile = state.profile,
                    recommendedProducts = state.recommendedProducts,
                    isLoggedIn = isLoggedIn,
                    onLoginClick = onLoginClick
                )
            }
            
            is ProfileUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.message,
                            color = ThemeColors.Text.secondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text(stringResource(Res.string.retry))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    profile: UserProfile,
    recommendedProducts: List<Article>,
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        // 顶部用户信息栏
        item {
            UserHeader(
                profile = profile,
                isLoggedIn = isLoggedIn,
                onLoginClick = onLoginClick
            )
        }
        
        // 我的订单模块
        item {
            OrderSection()
        }
        
        // 功能入口模块
        item {
            FunctionSection()
        }
        
        // 精选推荐标题
        item {
            RecommendTitle()
        }
        
        // 精选推荐商品网格
        item {
            RecommendationGrid(products = recommendedProducts)
        }
        
        // 没有更多了
        item {
            Text(
                text = "没有更多了",
                fontSize = 14.sp,
                color = Color(0xFF999999),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

// 顶部用户信息栏
@Composable
private fun UserHeader(
    profile: UserProfile,
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF6900))
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 头像 - 点击跳转登录
            Surface(
                modifier = Modifier
                    .size(64.dp)
                    .clickable(enabled = !isLoggedIn) { onLoginClick() },
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.3f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isLoggedIn) {
                        Text(
                            text = profile.username.take(1),
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "登录",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // 用户名 + 编辑资料
            Column {
                Text(
                    text = if (isLoggedIn) profile.username else "点击登录",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (isLoggedIn) {
                    Button(
                        onClick = { /* 编辑资料 */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "编辑资料",
                            fontSize = 13.sp,
                            color = Color(0xFFFF6900)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // 等级 + 设置
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isLoggedIn) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFAA00)
                        ) {
                            Text(
                                text = "Lv.${profile.level}",
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "设置",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* 切换语言 */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "English",
                        fontSize = 13.sp,
                        color = Color(0xFFFF6900)
                    )
                }
            }
        }
    }
}

// 我的订单模块
@Composable
private fun OrderSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 订单标题栏
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "我的订单",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ThemeColors.Text.primary
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "查看全部 >",
                    fontSize = 13.sp,
                    color = Color(0xFF999999),
                    modifier = Modifier.clickable { /* 查看全部 */ }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 订单标签
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OrderTabItem("待付款", Icons.Default.Payment)
                OrderTabItem("待发货", Icons.Default.LocalShipping)
                OrderTabItem("待收货", Icons.Default.Inventory)
                OrderTabItem("待评价", Icons.Default.RateReview)
                OrderTabItem("退款/售后", Icons.Default.AssignmentReturn)
            }
        }
    }
}

@Composable
private fun OrderTabItem(title: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { /* 点击跳转 */ }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFFFF6900),
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            color = ThemeColors.Text.primary
        )
    }
}

// 功能入口模块
@Composable
private fun FunctionSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 第一行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FunctionItem("商品收藏", Icons.Default.Favorite)
                FunctionItem("商铺关注", Icons.Default.Store)
                FunctionItem("账户资金", Icons.Default.AccountBalance)
                FunctionItem("浏览历史", Icons.Default.History)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // 第二行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FunctionItem("评价管理", Icons.Default.Star)
                FunctionItem("收货地址", Icons.Default.LocationOn)
                FunctionItem("领券中心", Icons.Default.LocalOffer)
                FunctionItem("官方客服", Icons.Default.SupportAgent)
            }
        }
    }
}

@Composable
private fun FunctionItem(title: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(70.dp)
            .clickable { /* 点击跳转 */ }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF666666),
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            color = ThemeColors.Text.primary,
            textAlign = TextAlign.Center
        )
    }
}

// 精选推荐标题
@Composable
private fun RecommendTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "●",
            fontSize = 14.sp,
            color = Color(0xFFFF6900)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "精选推荐",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = ThemeColors.Text.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "●",
            fontSize = 14.sp,
            color = Color(0xFFFF6900)
        )
    }
}

// 推荐商品网格
@Composable
private fun RecommendationGrid(products: List<Article>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 2000.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        userScrollEnabled = false
    ) {
        items(products) { product ->
            ProductCard(product = product)
        }
    }
}

// 商品卡片
@Composable
private fun ProductCard(product: Article) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 商品图片
            if (product.coverImage?.isNotEmpty() == true) {
                AsyncImage(
                    model = product.coverImage,
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(ThemeColors.Background.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = ThemeColors.Text.placeholder
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 商品标题
            Text(
                text = product.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = ThemeColors.Text.primary,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            // 价格 + 起购量
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¥4.6",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF3333)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = product.summary,
                    fontSize = 11.sp,
                    color = Color(0xFF999999)
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // 商家信息
            Text(
                text = "${product.author.name} | 义乌",
                fontSize = 11.sp,
                color = Color(0xFF999999),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
            )
        }
    }
}
