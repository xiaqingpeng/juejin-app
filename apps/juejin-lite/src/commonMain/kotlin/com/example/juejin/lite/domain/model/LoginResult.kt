package com.example.juejin.lite.domain.model

data class LoginResult(
    val userId: String,
    val userName: String,
    val nick: String?,
    val mobile: String?,
    val email: String?,
    val avatar: String?,
    val loginResult: String,
    val uuid: String?,
    val openId: String?,
    val userPrime: String?,
    val shopId: String?,
    val shopName: String?
)
