package com.example.juejin.viewmodel

import androidx.lifecycle.ViewModel
import com.example.juejin.model.User
import com.example.juejin.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 用户信息 ViewModel
 * 管理全局用户信息状态
 */
class UserViewModel : ViewModel() {
    
    // 用户信息状态
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user.asStateFlow()
    
    // 加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        // 初始化时加载用户信息
        loadUserInfo()
    }
    
    /**
     * 加载用户信息
     * 模拟从接口获取数据
     */
    fun loadUserInfo() {
        _isLoading.value = true
        
        // TODO: 实际项目中这里应该调用 API
        // 模拟网络请求延迟
        try {
            // 模拟数据
            _user.value = User(
                avatar = "",
                username = "qingpengxia",
                position = "前端工程师",
                company = "加里敦",
                bio = "哈哈",
                blogUrl = "",
                github = "",
                weibo = "",
                level = "Lv5",
                likeCount = 128,
                collectCount = 256,
                followCount = 89,
                fansCount = 512
            )
        } finally {
            _isLoading.value = false
        }
    }
    
    /**
     * 更新用户名
     */
    fun updateUsername(username: String) {
        _user.value = _user.value.copy(username = username)
    }
    
    /**
     * 更新职位
     */
    fun updatePosition(position: String) {
        _user.value = _user.value.copy(position = position)
    }
    
    /**
     * 更新公司
     */
    fun updateCompany(company: String) {
        _user.value = _user.value.copy(company = company)
    }
    
    /**
     * 更新简介
     */
    fun updateBio(bio: String) {
        _user.value = _user.value.copy(bio = bio)
    }
    
    /**
     * 更新博客地址
     */
    fun updateBlogUrl(blogUrl: String) {
        _user.value = _user.value.copy(blogUrl = blogUrl)
    }
    
    /**
     * 更新 GitHub
     */
    fun updateGithub(github: String) {
        _user.value = _user.value.copy(github = github)
    }
    
    /**
     * 更新微博
     */
    fun updateWeibo(weibo: String) {
        _user.value = _user.value.copy(weibo = weibo)
    }
    
    /**
     * 更新头像
     */
    fun updateAvatar(avatar: String) {
        _user.value = _user.value.copy(avatar = avatar)
    }
    
    /**
     * 保存用户信息到服务器
     */
    fun saveUserInfo(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        _isLoading.value = true
        
        try {
            // TODO: 实际项目中这里应该调用 API 保存数据
            // 模拟保存成功
            Logger.d("UserViewModel", "保存用户信息: ${_user.value}")
            onSuccess()
        } catch (e: Exception) {
            onError(e.message ?: "保存失败")
        } finally {
            _isLoading.value = false
        }
    }
    
    /**
     * 更新整个用户对象
     */
    fun updateUser(user: User) {
        _user.value = user
    }
}
