package com.example.juejin.lite.domain.model

/**
 * 消息领域模型
 */
data class Message(
    val id: String,
    val type: MessageType,
    val title: String,
    val content: String,
    val sender: Author,
    val isRead: Boolean,
    val createTime: Long
)

/**
 * 消息类型
 */
enum class MessageType {
    SYSTEM,    // 系统消息
    LIKE,      // 点赞
    COMMENT,   // 评论
    FOLLOW     // 关注
}
