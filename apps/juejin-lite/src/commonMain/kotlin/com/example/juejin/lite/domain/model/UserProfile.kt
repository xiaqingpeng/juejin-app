package com.example.juejin.lite.domain.model

/**
 * 用户资料领域模型
 */
data class UserProfile(
    val id: String,
    val username: String,
    val avatar: String?,
    val bio: String?,
    val level: Int,
    val followCount: Int,
    val followerCount: Int,
    val articleCount: Int,
    val likeCount: Int
)
