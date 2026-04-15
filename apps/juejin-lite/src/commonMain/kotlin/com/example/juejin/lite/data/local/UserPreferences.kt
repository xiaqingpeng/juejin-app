package com.example.juejin.lite.data.local

import com.example.juejin.lite.domain.model.LoginResult

/**
 * 用户偏好设置（简单实现，使用内存存储）
 * TODO: 后续可以使用 DataStore 或其他持久化方案
 */
object UserPreferences {
    private var currentUser: LoginResult? = null
    
    fun saveUser(user: LoginResult) {
        currentUser = user
    }
    
    fun getUser(): LoginResult? {
        return currentUser
    }
    
    fun clearUser() {
        currentUser = null
    }
    
    fun isLoggedIn(): Boolean {
        return currentUser != null
    }
}
