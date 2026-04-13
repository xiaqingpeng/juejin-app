package com.example.juejin.lite.domain.usecase

import com.example.juejin.lite.domain.model.Message
import com.example.juejin.lite.domain.model.MessageType
import com.example.juejin.lite.domain.repository.MessageRepository

/**
 * 获取消息列表用例
 */
class GetMessagesUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(type: MessageType? = null, page: Int, pageSize: Int = 20): Result<List<Message>> {
        return messageRepository.getMessages(type, page, pageSize)
    }
}
