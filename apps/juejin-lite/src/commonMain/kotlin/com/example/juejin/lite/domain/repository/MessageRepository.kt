package com.example.juejin.lite.domain.repository

import com.example.juejin.lite.domain.model.Message
import com.example.juejin.lite.domain.model.MessageType

/**
 * 消息仓库接口
 */
interface MessageRepository {
    /**
     * 获取消息列表
     */
    suspend fun getMessages(type: MessageType?, page: Int, pageSize: Int): Result<List<Message>>
    
    /**
     * 标记消息为已读
     */
    suspend fun markAsRead(messageId: String): Result<Unit>
    
    /**
     * 删除消息
     */
    suspend fun deleteMessage(messageId: String): Result<Unit>
    
    /**
     * 获取未读消息数量
     */
    suspend fun getUnreadCount(): Result<Int>
}
