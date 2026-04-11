package com.example.juejin.domain.model

import kotlinx.serialization.Serializable

/**
 * 发现页面的数据模型
 */
@Serializable
data class DiscoverModule(
    val id: String,
    val title: String,
    val iconName: String = "", // 使用字符串表示图标名称，而不是 ImageVector
    val route: String
)

@Serializable
data class Article(
    val id: String,
    val title: String,
    val author: String,
    val summary: String,
    val publishTime: String,
    val likeCount: Int,
    val commentCount: Int
)

@Serializable
data class Circle(
    val id: String,
    val name: String,
    val memberCount: Int,
    val hotCount: Int,
    val description: String,
    val avatar: String
)
