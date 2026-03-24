package com.example.juejin.model

/**
 * 用户资料数据模型
 */
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
