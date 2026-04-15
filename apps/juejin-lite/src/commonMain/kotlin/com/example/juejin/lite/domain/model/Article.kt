package com.example.juejin.lite.domain.model

/**
 * 文章领域模型
 */
data class Article(
    val id: String,
    val title: String,
    val summary: String,
    val coverImage: String?,
    val author: Author,
    val viewCount: Int,
    val likeCount: Int,
    val commentCount: Int,
    val publishTime: Long,
    val tags: List<String>
)

/**
 * 作者信息
 */
data class Author(
    val id: String,
    val name: String,
    val avatar: String?
)
