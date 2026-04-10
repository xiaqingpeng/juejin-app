package com.example.juejin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.juejin.model.SettingItem
import com.example.juejin.model.SettingType
import com.example.juejin.theme.ThemeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 为了在 SwiftUI 中使用，需要添加 @ObservableObject 注解
// 但是这个注解在 Kotlin/Native 中不可用，所以我们需要创建一个 iOS 专用的包装器
class SettingViewModel : ViewModel() {
    private val _darkMode = MutableStateFlow(ThemeManager.getThemeModeString())
    val darkMode: StateFlow<String> = _darkMode.asStateFlow()

    private val _pushNotification = MutableStateFlow(true)
    val pushNotification: StateFlow<Boolean> = _pushNotification.asStateFlow()

    val settings = listOf(
        SettingItem("编辑资料", type = SettingType.NORMAL),
        SettingItem("账号设置", type = SettingType.NORMAL),
        SettingItem("消息设置", type = SettingType.NORMAL),
        SettingItem("屏蔽管理", type = SettingType.NORMAL),
        SettingItem("个性化推荐设置", type = SettingType.NORMAL),
        SettingItem("推送通知设置", subTitle = if (_pushNotification.value) "已开启" else "已关闭", type = SettingType.SWITCH),
        SettingItem("深色模式", subTitle = _darkMode.value, type = SettingType.SELECTOR),
        SettingItem("个人信息查阅与管理", type = SettingType.NORMAL),
        SettingItem("基础版掘金", type = SettingType.NORMAL),
        SettingItem("检查更新", type = SettingType.NORMAL),
        SettingItem("关于", type = SettingType.NORMAL),
        SettingItem("退出登录", type = SettingType.NORMAL, isDestructive = true)
    )

    fun updateDarkMode(mode: String) {
        _darkMode.value = mode
        // 更新 ThemeManager
        ThemeManager.setThemeModeByString(mode)
    }

    fun updatePushNotification(enabled: Boolean) {
        _pushNotification.value = enabled
    }

    fun getUpdatedSettings(): List<SettingItem> {
        return settings.map { item ->
            when (item.title) {
                "推送通知设置" -> item.copy(subTitle = if (_pushNotification.value) "已开启" else "已关闭")
                "深色模式" -> item.copy(subTitle = _darkMode.value)
                else -> item
            }
        }
    }
}
