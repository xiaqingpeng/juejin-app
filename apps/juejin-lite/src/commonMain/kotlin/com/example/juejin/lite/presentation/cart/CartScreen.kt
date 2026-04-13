package com.example.juejin.lite.presentation.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.juejin.lite.domain.model.CartItem
import com.example.juejin.lite.domain.model.CartSummary
import com.example.juejin.ui.theme.ThemeColors
import juejin.lite.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: CartViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(Res.string.shopping_cart_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = ThemeColors.Text.secondary
                    )
                }
            }
            
            is CartUiState.Success -> {
                CartContent(
                    summary = state.summary,
                    onToggleSelection = { viewModel.toggleSelection(it) },
                    onSelectAll = { viewModel.selectAll(it) },
                    onUpdateQuantity = { id, qty -> viewModel.updateQuantity(id, qty) },
                    onRemove = { viewModel.removeItem(it) },
                    onCheckout = { viewModel.checkout() }
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

@Composable
private fun CartContent(
    summary: CartSummary,
    onToggleSelection: (String) -> Unit,
    onSelectAll: (Boolean) -> Unit,
    onUpdateQuantity: (String, Int) -> Unit,
    onRemove: (String) -> Unit,
    onCheckout: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
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
        
        // 底部结算栏
        Surface(
            shadowElevation = 8.dp,
            color = ThemeColors.Background.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.shopping_cart_total),
                        style = MaterialTheme.typography.bodyMedium,
                        color = ThemeColors.Text.secondary
                    )
                    Text(
                        text = "¥${(summary.totalPrice * 100).toInt() / 100.0}",
                        style = MaterialTheme.typography.titleLarge,
                        color = ThemeColors.primaryBlue
                    )
                }
                
                Button(
                    onClick = onCheckout,
                    enabled = summary.selectedCount > 0
                ) {
                    Text(stringResource(Res.string.shopping_cart_checkout))
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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = ThemeColors.Background.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isSelected,
                onCheckedChange = { onToggleSelection() }
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.productName,
                    style = MaterialTheme.typography.titleSmall,
                    color = ThemeColors.Text.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "¥${item.price}",
                    style = MaterialTheme.typography.titleMedium,
                    color = ThemeColors.primaryBlue
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onUpdateQuantity(item.quantity - 1) },
                        enabled = item.quantity > 1
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "减少")
                    }
                    
                    Text(
                        text = item.quantity.toString(),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    IconButton(
                        onClick = { onUpdateQuantity(item.quantity + 1) }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "增加")
                    }
                }
            }
        }
    }
}
