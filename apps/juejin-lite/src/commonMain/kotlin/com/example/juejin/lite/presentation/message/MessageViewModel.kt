package com.example.juejin.lite.presentation.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.juejin.lite.domain.model.Message
import com.example.juejin.lite.domain.model.MessageType
import com.example.juejin.lite.domain.repository.MessageRepository
import com.example.juejin.lite.domain.usecase.GetMessagesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 消息 ViewModel
 */
class MessageViewModel(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val messageRepository: MessageRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<MessageUiState>(MessageUiState.Loading)
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()
    
    private var currentType: MessageType? = null
    private var currentPage = 0
    private val messages = mutableListOf<Message>()
    
    init {
        loadMessages()
    }
    
    fun filterByType(type: MessageType?) {
        currentType = type
        currentPage = 0
        messages.clear()
        loadMessages()
    }
    
    fun loadMessages() {
        viewModelScope.launch {
            _uiState.value = if (messages.isEmpty()) {
                MessageUiState.Loading
            } else {
                MessageUiState.Success(messages, isLoadingMore = true)
            }
            
            getMessagesUseCase(currentType, currentPage)
                .onSuccess { newMessages ->
                    messages.addAll(newMessages)
                    currentPage++
                    _uiState.value = MessageUiState.Success(messages, isLoadingMore = false)
                }
                .onFailure { error ->
                    _uiState.value = if (messages.isEmpty()) {
                        MessageUiState.Error(error.message ?: "加载失败")
                    } else {
                        MessageUiState.Success(messages, isLoadingMore = false)
                    }
                }
        }
    }
    
    fun markAsRead(messageId: String) {
        viewModelScope.launch {
            messageRepository.markAsRead(messageId)
                .onSuccess {
                    val index = messages.indexOfFirst { it.id == messageId }
                    if (index != -1) {
                        messages[index] = messages[index].copy(isRead = true)
                        _uiState.value = MessageUiState.Success(messages, isLoadingMore = false)
                    }
                }
        }
    }
    
    fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            messageRepository.deleteMessage(messageId)
                .onSuccess {
                    messages.removeAll { it.id == messageId }
                    _uiState.value = MessageUiState.Success(messages, isLoadingMore = false)
                }
        }
    }
    
    fun refresh() {
        currentPage = 0
        messages.clear()
        loadMessages()
    }
}

/**
 * 消息 UI 状态
 */
sealed class MessageUiState {
    object Loading : MessageUiState()
    data class Success(
        val messages: List<Message>,
        val isLoadingMore: Boolean = false
    ) : MessageUiState()
    data class Error(val message: String) : MessageUiState()
}
