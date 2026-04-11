package com.example.juejin.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Hot(
    val id: String,
    val author: String,
    val avatar: String,
    val title: String,
    val content: String,
    val time: String,
    val likeCount: Int,
    val commentCount: Int,
    val shareCount: Int,
    val images: List<String>? = null
)
