package com.example.juejin.storage

import platform.Foundation.NSUserDefaults

/**
 * iOS 平台隐私政策存储实现
 */
actual object PrivacyStorage {

    private val userDefaults: NSUserDefaults by lazy {
        NSUserDefaults.standardUserDefaults
    }

    actual fun isPrivacyPolicyAccepted(): Boolean {
        return userDefaults.boolForKey(KEY_PRIVACY_ACCEPTED)
    }

    actual fun setPrivacyPolicyAccepted(accepted: Boolean) {
        userDefaults.setBool(accepted, forKey = KEY_PRIVACY_ACCEPTED)
    }
    
    actual fun putString(key: String, value: String) {
        userDefaults.setObject(value, forKey = key)
    }
    
    actual fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }

    private const val KEY_PRIVACY_ACCEPTED = "privacy_accepted"
}