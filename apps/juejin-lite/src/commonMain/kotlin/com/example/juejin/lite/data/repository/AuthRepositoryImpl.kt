package com.example.juejin.lite.data.repository

import com.example.juejin.core.common.Logger
import com.example.juejin.lite.data.remote.YiwugoApi
import com.example.juejin.lite.domain.model.LoginResult
import com.example.juejin.lite.domain.repository.AuthRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class AuthRepositoryImpl(
    private val yiwugoApi: YiwugoApi
) : AuthRepository {
    
    override suspend fun login(userName: String, password: String): Result<LoginResult> {
        val apiResult = yiwugoApi.login(userName, password)
        
        return apiResult.mapCatching { dto ->
            
            // 检查登录结果
            if (dto.loginResult != "SUCCESS") {
                throw Exception("登录失败: ${dto.loginResult}")
            }
            
            val result = LoginResult(
                userId = dto.userId ?: "",
                userName = dto.userName ?: dto.nick ?: "",
                nick = dto.nick,
                mobile = dto.mobile,
                email = dto.email,
                avatar = dto.avatar,
                loginResult = dto.loginResult ?: "UNKNOWN",
                uuid = dto.uuid,
                openId = dto.openId,
                userPrime = dto.userPrime,
                shopId = dto.shopId,
                shopName = dto.shopName
            )
            
            Logger.e("AuthRepository", "映射后 - userId: ${result.userId}")
            Logger.e("AuthRepository", "映射后 - userName: ${result.userName}")
            Logger.e("AuthRepository", "========================================")
            
            result
        }
    }
}
