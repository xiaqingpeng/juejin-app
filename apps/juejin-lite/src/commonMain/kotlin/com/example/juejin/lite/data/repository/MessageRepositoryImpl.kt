package com.example.juejin.lite.data.repository

import com.example.juejin.core.common.DateTimeUtil
import com.example.juejin.lite.domain.model.Author
import com.example.juejin.lite.domain.model.Message
import com.example.juejin.lite.domain.model.MessageType
import com.example.juejin.lite.domain.repository.MessageRepository

/**
 * 消息仓库实现（Mock 数据）
 */
class MessageRepositoryImpl : MessageRepository {
    
    private val messages = mutableListOf<Message>()
    
    init {
        // 初始化一些 Mock 数据
        messages.addAll(
            (1..50).map { index ->
                Message(
                    id = index.toString(),
                    type = MessageType.entries.random(),
                    title = when (MessageType.entries.random()) {
                        MessageType.SYSTEM -> "系统通知 $index"
                        MessageType.LIKE -> "有人点赞了你的文章"
                        MessageType.COMMENT -> "有人评论了你的文章"
                        MessageType.FOLLOW -> "有人关注了你"
                    },
                    content = "这是消息内容 $index",
                    sender = Author("sender_$index", "用户${index}", null),
                    isRead = index % 3 == 0,
                    createTime = DateTimeUtil.currentTimeMillis() - (index * 1800000L)
                )
            }
        )
    }
    
    override suspend fun getMessages(
        type: MessageType?,
        page: Int,
        pageSize: Int
    ): Result<List<Message>> {
        return try {
            kotlinx.coroutines.delay(300)
            
            val filtered = if (type != null) {
                messages.filter { it.type == type }
            } else {
                messages
            }
            
            val start = page * pageSize
            val end = minOf(start + pageSize, filtered.size)
            
            Result.success(filtered.subList(start, minOf(end, filtered.size)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun markAsRead(messageId: String): Result<Unit> {
        return try {
            kotlinx.coroutines.delay(200)
            val index = messages.indexOfFirst { it.id == messageId }
            if (index != -1) {
                messages[index] = messages[index].copy(isRead = true)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteMessage(messageId: String): Result<Unit> {
        return try {
            kotlinx.coroutines.delay(200)
            messages.removeAll { it.id == messageId }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUnreadCount(): Result<Int> {
        return try {
            Result.success(messages.count { !it.isRead })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
