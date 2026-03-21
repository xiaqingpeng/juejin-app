package com.example.juejin.storage

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

/**
 * Android 平台隐私政策存储实现
 */
actual object PrivacyStorage {

    private lateinit var appContext: Context

    private val prefs: SharedPreferences by lazy {
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    actual fun isPrivacyPolicyAccepted(): Boolean {
        return prefs.getBoolean(KEY_PRIVACY_ACCEPTED, false)
    }

    actual fun setPrivacyPolicyAccepted(accepted: Boolean) {
        prefs.edit().putBoolean(KEY_PRIVACY_ACCEPTED, accepted).apply()
    }

    /**
     * 初始化，在 Application 或 MainActivity 中调用
     */
    fun init(context: Application) {
        appContext = context.applicationContext
    }

    private const val PREFS_NAME = "privacy_prefs"
    private const val KEY_PRIVACY_ACCEPTED = "privacy_accepted"
}
