package com.example.juejin.lite.domain.repository

import com.example.juejin.lite.domain.model.UserProfile

/**
 * 用户仓库接口
 */
interface UserRepository {
    /**
     * 获取用户资料
     */
    suspend fun getUserProfile(userId: String): Result<UserProfile>
    
    /**
     * 更新用户资料
     */
    suspend fun updateUserProfile(profile: UserProfile): Result<Unit>
}
