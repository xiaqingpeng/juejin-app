package com.example.juejin.lite.domain.usecase

import com.example.juejin.lite.domain.model.UserProfile
import com.example.juejin.lite.domain.repository.UserRepository

/**
 * 获取用户资料用例
 */
class GetUserProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<UserProfile> {
        return userRepository.getUserProfile(userId)
    }
}
