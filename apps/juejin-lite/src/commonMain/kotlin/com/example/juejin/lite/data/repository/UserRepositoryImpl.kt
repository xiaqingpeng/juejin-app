package com.example.juejin.lite.data.repository

import com.example.juejin.lite.domain.model.UserProfile
import com.example.juejin.lite.domain.repository.UserRepository

/**
 * 用户仓库实现（Mock 数据）
 */
class UserRepositoryImpl : UserRepository {
    
    private var cachedProfile: UserProfile? = null
    
    override suspend fun getUserProfile(userId: String): Result<UserProfile> {
        return try {
            kotlinx.coroutines.delay(300)
            
            val profile = cachedProfile ?: UserProfile(
                id = userId,
                username = "掘金用户",
                avatar = null,
                bio = "热爱技术，分享知识",
                level = 5,
                followCount = 128,
                followerCount = 256,
                articleCount = 42,
                likeCount = 1024
            )
            
            cachedProfile = profile
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            kotlinx.coroutines.delay(300)
            cachedProfile = profile
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
