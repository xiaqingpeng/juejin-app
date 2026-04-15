package com.example.juejin.lite.domain.repository

import com.example.juejin.lite.domain.model.LoginResult

interface AuthRepository {
    suspend fun login(userName: String, password: String): Result<LoginResult>
}
