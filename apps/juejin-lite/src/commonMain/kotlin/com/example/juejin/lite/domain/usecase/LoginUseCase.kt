package com.example.juejin.lite.domain.usecase

import com.example.juejin.lite.domain.model.LoginResult
import com.example.juejin.lite.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userName: String, password: String): Result<LoginResult> {
        return authRepository.login(userName, password)
    }
}
