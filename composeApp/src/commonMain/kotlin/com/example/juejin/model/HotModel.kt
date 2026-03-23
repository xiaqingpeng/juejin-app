package com.example.juejin.model

// Hot.kt
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