package com.example.juejin.lite.presentation.message

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.juejin.lite.domain.model.Message
import com.example.juejin.lite.domain.model.MessageType
import com.example.juejin.ui.theme.ThemeColors
import juejin.lite.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun MessageScreen(viewModel: MessageViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is MessageUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ThemeColors.primaryBlue)
                }
            }
            
            is MessageUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.messages) { message ->
                        MessageItem(
                            message = message,
                            onMarkRead = { viewModel.markAsRead(message.id) },
                            onDelete = { viewModel.deleteMessage(message.id) }
                        )
                    }
                    
                    if (state.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = ThemeColors.primaryBlue
                                )
                            }
                        }
                    }
                }
            }
            
            is MessageUiState.Error -> {
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
private fun MessageItem(
    message: Message,
    onMarkRead: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (message.isRead) {
                ThemeColors.Background.surface
            } else {
                ThemeColors.primaryBlue.copy(alpha = 0.05f)
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = message.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = ThemeColors.Text.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                if (!message.isRead) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = ThemeColors.primaryBlue
                    ) {
                        Text(
                            text = "新",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = ThemeColors.Text.white
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = ThemeColors.Text.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message.sender.name,
                style = MaterialTheme.typography.bodySmall,
                color = ThemeColors.Text.tertiary
            )
        }
    }
}
