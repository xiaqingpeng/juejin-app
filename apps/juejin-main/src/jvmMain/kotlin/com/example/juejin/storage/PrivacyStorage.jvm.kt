package com.example.juejin.storage

import java.io.File
import java.util.Properties

/**
 * Desktop (JVM) 平台隐私政策存储实现
 */
actual object PrivacyStorage {

    private val prefsFile: File by lazy {
        val userHome = System.getProperty("user.home")
        val appDir = File(userHome, ".juejin-app")
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        File(appDir, "privacy.prefs")
    }

    private val properties: Properties by lazy {
        val props = Properties()
        if (prefsFile.exists()) {
            try {
                prefsFile.inputStream().use { props.load(it) }
            } catch (e: Exception) {
                // 忽略错误
            }
        }
        props
    }

    actual fun isPrivacyPolicyAccepted(): Boolean {
        return properties.getProperty(KEY_PRIVACY_ACCEPTED, "false") == "true"
    }

    actual fun setPrivacyPolicyAccepted(accepted: Boolean) {
        properties.setProperty(KEY_PRIVACY_ACCEPTED, accepted.toString())
        try {
            prefsFile.outputStream().use { properties.store(it, "Privacy Policy Preferences") }
        } catch (e: Exception) {
            // 忽略错误
        }
    }
    
    actual fun putString(key: String, value: String) {
        properties.setProperty(key, value)
        try {
            prefsFile.outputStream().use { properties.store(it, "App Preferences") }
        } catch (e: Exception) {
            // 忽略错误
        }
    }
    
    actual fun getString(key: String, defaultValue: String): String {
        return properties.getProperty(key, defaultValue)
    }

    private const val KEY_PRIVACY_ACCEPTED = "privacy_accepted"
}