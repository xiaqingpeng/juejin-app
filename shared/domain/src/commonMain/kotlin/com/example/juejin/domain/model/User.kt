package com.example.juejin.domain.model

import kotlinx.serialization.Serializable

/**
 * 用户资料数据模型
 */
@Serializable
data class User(
    val avatar: String = "", // 头像URL
    val username: String = "qingpengxia",
    val position: String = "前端工程师",
    val company: String = "加里敦",
    val bio: String = "哈哈",
    val blogUrl: String = "",
    val github: String = "",
    val weibo: String = "",
    
    // 个人主页相关字段
    val level: String = "Lv5", // 等级
    val likeCount: Int = 0, // 点赞数
    val collectCount: Int = 0, // 收藏数
    val followCount: Int = 0, // 关注数
    val fansCount: Int = 0 // 粉丝数
)

@Serializable
data class UserInfo(
    val name: String = "Qing Peng",
    val level: String = "JY.6",
    val likeCount: Int = 13,
    val collectCount: Int = 1929,
    val followCount: Int = 315
)
