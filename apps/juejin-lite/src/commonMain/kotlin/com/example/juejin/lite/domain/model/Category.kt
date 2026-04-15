package com.example.juejin.lite.domain.model

/**
 * 分类领域模型
 */
data class Category(
    val id: String,
    val name: String,
    val icon: String?,
    val articleCount: Int
)
