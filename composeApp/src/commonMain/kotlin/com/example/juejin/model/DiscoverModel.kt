package com.example.juejin.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 发现页面的数据模型
 */

data class DiscoverModule(
    val id: String,
    val title: String,
    val iconRes: ImageVector,
    val route: String
)

data class Article(
    val id: String,
    val title: String,
    val author: String,
    val summary: String,
    val publishTime: String,
    val likeCount: Int,
    val commentCount: Int
)

data class Circle(
    val id: String,
    val name: String,
    val memberCount: Int,
    val hotCount: Int,
    val description: String,
    val avatar: String
)
