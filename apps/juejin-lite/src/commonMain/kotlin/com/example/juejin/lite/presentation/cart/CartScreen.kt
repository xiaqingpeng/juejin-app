package com.example.juejin.lite.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.juejin.lite.domain.model.Article
import com.example.juejin.lite.domain.model.CartItem
import com.example.juejin.lite.domain.model.CartSummary
import com.example.juejin.ui.theme.ThemeColors
import juejin.lite.generated.resources.Res
import juejin.lite.generated.resources.guess_you_like
import juejin.lite.generated.resources.retry
import juejin.lite.generated.resources.select_all
import juejin.lite.generated.resources.shopping_cart
import juejin.lite.generated.resources.shopping_cart_checkout
import juejin.lite.generated.resources.shopping_cart_empty
import juejin.lite.generated.resources.shopping_cart_total
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: CartViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.shopping_cart),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                },
                actions = {
                    // 只在有商品时显示管理按钮
                    if (uiState is CartUiState.Success) {
                        val isManageMode = (uiState as CartUiState.Success).isManageMode
                        TextButton(onClick = { viewModel.toggleManageMode() }) {
                            Text(
                                text = if (isManageMode) "完成" else "管理",
                                color = ThemeColors.Text.secondary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ThemeColors.Background.surface
                )
            )
        },
        bottomBar = {
            when (val state = uiState) {
                is CartUiState.Success -> {
                    CartBottomBar(
                        summary = state.summary,
                        isManageMode = state.isManageMode,
                        onSelectAll = { viewModel.selectAll(it) },
                        onCheckout = { viewModel.checkout() },
                        onDelete = { viewModel.deleteSelectedItems() }
                    )
                }
                else -> {}
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is CartUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ThemeColors.primaryBlue)
                    }
                }
                
                is CartUiState.Empty -> {
                    EmptyCartWithRecommendations(
                        recommendedProducts = state.recommendedProducts
                    )
                }
                
                is CartUiState.Success -> {
                    CartContentWithRecommendations(
                        summary = state.summary,
                        recommendedProducts = state.recommendedProducts,
                        onToggleSelection = { viewModel.toggleSelection(it) },
                        onUpdateQuantity = { id, qty -> viewModel.updateQuantity(id, qty) },
                        onRemove = { viewModel.removeItem(it) }
                    )
                }
                
                is CartUiState.Error -> {
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
}

@Composable
private fun EmptyCartContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = ThemeColors.Text.placeholder
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.shopping_cart_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = ThemeColors.Text.secondary
        )
    }
}

@Composable
private fun EmptyCartWithRecommendations(
    recommendedProducts: List<Article>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // 空购物车状态
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = ThemeColors.Text.placeholder
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.shopping_cart_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    color = ThemeColors.Text.secondary
                )
            }
        }
        
        // 猜你喜欢标题
        item {
            RecommendationTitle()
        }
        
        // 推荐商品网格
        item {
            RecommendationGrid(products = recommendedProducts)
        }
    }
}

@Composable
private fun CartContentWithRecommendations(
    summary: CartSummary,
    recommendedProducts: List<Article>,
    onToggleSelection: (String) -> Unit,
    onUpdateQuantity: (String, Int) -> Unit,
    onRemove: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 0.dp)
    ) {
        // 购物车商品列表
        items(summary.items) { item ->
            CartItemCard(
                item = item,
                onToggleSelection = { onToggleSelection(item.id) },
                onUpdateQuantity = { qty -> onUpdateQuantity(item.id, qty) },
                onRemove = { onRemove(item.id) }
            )
        }
        
        // 猜你喜欢标题
        item {
            Spacer(modifier = Modifier.height(16.dp))
            RecommendationTitle()
        }
        
        // 推荐商品网格
        item {
            RecommendationGrid(products = recommendedProducts)
        }
    }
}

@Composable
private fun RecommendationTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "—",
            style = MaterialTheme.typography.bodyMedium,
            color = ThemeColors.Text.placeholder
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color(0xFFFF6900),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(Res.string.guess_you_like),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFFFF6900)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "—",
            style = MaterialTheme.typography.bodyMedium,
            color = ThemeColors.Text.placeholder
        )
    }
}

@Composable
private fun RecommendationGrid(products: List<Article>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 2000.dp), // 限制最大高度
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        userScrollEnabled = false // 禁用内部滚动，使用外层 LazyColumn 的滚动
    ) {
        items(products) { product ->
            RecommendationProductCard(product = product)
        }
    }
}

@Composable
private fun CartContent(
    summary: CartSummary,
    onToggleSelection: (String) -> Unit,
    onUpdateQuantity: (String, Int) -> Unit,
    onRemove: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(summary.items) { item ->
            CartItemCard(
                item = item,
                onToggleSelection = { onToggleSelection(item.id) },
                onUpdateQuantity = { qty -> onUpdateQuantity(item.id, qty) },
                onRemove = { onRemove(item.id) }
            )
        }
    }
}

@Composable
private fun CartBottomBar(
    summary: CartSummary,
    isManageMode: Boolean,
    onSelectAll: (Boolean) -> Unit,
    onCheckout: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        shadowElevation = 8.dp,
        color = ThemeColors.Background.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 全选
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onSelectAll(summary.selectedCount != summary.items.size) }
            ) {
                Checkbox(
                    checked = summary.selectedCount == summary.items.size && summary.items.isNotEmpty(),
                    onCheckedChange = { onSelectAll(it) }
                )
                Text(
                    text = stringResource(Res.string.select_all),
                    style = MaterialTheme.typography.bodyMedium,
                    color = ThemeColors.Text.primary
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            if (isManageMode) {
                // 管理模式：显示删除按钮
                Button(
                    onClick = onDelete,
                    enabled = summary.selectedCount > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6900),
                        disabledContainerColor = Color(0xFFFFB380)
                    ),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        text = "删除",
                        color = Color.White
                    )
                }
            } else {
                // 正常模式：显示合计和结算按钮
                Text(
                    text = "合计：¥${summary.totalPrice}",
                    style = MaterialTheme.typography.titleMedium,
                    color = ThemeColors.Text.primary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Button(
                    onClick = onCheckout,
                    enabled = summary.selectedCount > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6900),
                        disabledContainerColor = Color(0xFFFFB380)
                    ),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.shopping_cart_checkout),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItem,
    onToggleSelection: () -> Unit,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = ThemeColors.Background.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Checkbox
            Checkbox(
                checked = item.isSelected,
                onCheckedChange = { onToggleSelection() },
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Product Image Placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ThemeColors.Background.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = ThemeColors.Text.placeholder
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Product Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ThemeColors.Text.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "¥${item.price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFFF6900),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Quantity Controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(ThemeColors.Background.secondary)
                ) {
                    IconButton(
                        onClick = { onUpdateQuantity(item.quantity - 1) },
                        enabled = item.quantity > 1,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "减少",
                            modifier = Modifier.size(16.dp),
                            tint = if (item.quantity > 1) ThemeColors.Text.primary else ThemeColors.Text.placeholder
                        )
                    }
                    
                    Text(
                        text = item.quantity.toString(),
                        modifier = Modifier.padding(horizontal = 12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = ThemeColors.Text.primary
                    )
                    
                    IconButton(
                        onClick = { onUpdateQuantity(item.quantity + 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "增加",
                            modifier = Modifier.size(16.dp),
                            tint = ThemeColors.Text.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendationProductCard(product: Article) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = ThemeColors.Background.surface
        ),
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
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // 占位图
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
                style = MaterialTheme.typography.bodyMedium,
                color = ThemeColors.Text.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 价格和起购量
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¥4.6",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFFF6900),
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = product.summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = ThemeColors.Text.secondary
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // 商家信息
            Text(
                text = "${product.author.name}",
                style = MaterialTheme.typography.bodySmall,
                color = ThemeColors.Text.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            
            // 标签（产地/年限）
            if (product.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    product.tags.take(2).forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = ThemeColors.Background.secondary
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall,
                                color = ThemeColors.Text.secondary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
