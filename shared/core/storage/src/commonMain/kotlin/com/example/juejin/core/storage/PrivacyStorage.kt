package com.example.juejin.core.storage

/**
 * 隐私政策存储接口
 * 跨平台存储用户是否同意隐私政策
 */
expect object PrivacyStorage {
    /**
     * 检查用户是否已同意隐私政策
     */
    fun isPrivacyPolicyAccepted(): Boolean

    /**
     * 设置用户同意隐私政策
     */
    fun setPrivacyPolicyAccepted(accepted: Boolean)
    
    /**
     * 存储字符串值
     */
    fun putString(key: String, value: String)
    
    /**
     * 获取字符串值
     */
    fun getString(key: String, defaultValue: String): String
}
