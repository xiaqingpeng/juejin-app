# 主题模式功能完成说明

## 功能概述

已完成"跟随系统"、"浅色模式"、"深色模式"三种主题模式的完整实现。

## 实现的功能

### 1. 主题模式选择
- ✅ 跟随系统：自动跟随系统的深色/浅色模式
- ✅ 浅色模式：强制使用浅色主题
- ✅ 深色模式：强制使用深色主题

### 2. 设置页面集成
- ✅ 在设置页面显示当前主题模式
- ✅ 下拉菜单选择主题模式
- ✅ 选择后立即生效并保存

### 3. 系统主题监听
- ✅ Android：自动检测系统主题变化
- ✅ iOS：自动检测系统主题变化
- ✅ JVM/Desktop：基础实现（可扩展）

### 4. iOS 状态栏同步
- ✅ 主题切换时自动更新 iOS 状态栏样式
- ✅ 通过 NSNotificationCenter 与 SwiftUI 通信

## 文件修改清单

### 新增文件
1. `composeApp/src/iosMain/kotlin/com/example/juejin/theme/SystemTheme.ios.kt`
   - iOS 平台系统主题检测实现

2. `composeApp/src/jvmMain/kotlin/com/example/juejin/theme/SystemTheme.jvm.kt`
   - JVM/Desktop 平台系统主题检测实现

### 修改文件
1. `composeApp/src/commonMain/kotlin/com/example/juejin/viewmodel/SettingViewModel.kt`
   - 初始化时从 ThemeManager 读取当前主题模式
   - updateDarkMode() 方法调用 ThemeManager.setThemeModeByString()

2. `composeApp/src/commonMain/kotlin/com/example/juejin/App.kt`
   - 监听系统主题变化
   - 更新 ThemeManager.isSystemDarkMode
   - 使用 remember 缓存 backgroundColor 避免重组

## 工作流程

```
用户在设置页面选择主题
    ↓
SettingViewModel.updateDarkMode()
    ↓
ThemeManager.setThemeModeByString()
    ↓
更新 ThemeManager.themeMode
    ↓
保存到 PrivacyStorage
    ↓
notifyThemeChangedPlatform() (iOS)
    ↓
NSNotificationCenter 发送通知
    ↓
ContentView.swift 接收通知
    ↓
更新 iOS 状态栏样式
```

## 系统主题监听流程

```
App.kt 启动
    ↓
isSystemInDarkTheme() 检测系统主题
    ↓
LaunchedEffect 监听变化
    ↓
更新 ThemeManager.isSystemDarkMode
    ↓
ThemeManager.isDarkMode 重新计算
    ↓
UI 自动更新
```

## 使用方式

### 用户操作
1. 打开"我的" → "设置"
2. 找到"深色模式"选项
3. 点击右侧的当前模式（如"跟随系统"）
4. 在下拉菜单中选择：
   - 跟随系统
   - 浅色模式
   - 深色模式
5. 选择后立即生效

### 开发者使用
```kotlin
// 获取当前主题模式
val currentMode = ThemeManager.themeMode

// 设置主题模式
ThemeManager.setThemeMode(ThemeMode.DARK)

// 通过字符串设置
ThemeManager.setThemeModeByString("深色模式")

// 获取当前是否为深色模式
val isDark = ThemeManager.isDarkMode

// 切换主题（用于快捷按钮）
ThemeManager.toggleTheme()
```

## 技术细节

### ThemeMode 枚举
```kotlin
enum class ThemeMode {
    SYSTEM,  // 跟随系统
    LIGHT,   // 浅色模式
    DARK     // 深色模式
}
```

### 主题计算逻辑
```kotlin
val isDarkMode: Boolean
    get() = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemDarkMode
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
```

### 平台特定实现

#### Android
```kotlin
@Composable
actual fun isSystemInDarkTheme(): Boolean {
    return isSystemInDarkTheme()
}
```

#### iOS
```kotlin
@Composable
actual fun isSystemInDarkTheme(): Boolean {
    val traitCollection = UIScreen.mainScreen.traitCollection
    return traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
}
```

## 测试建议

### Android 测试
1. 在设置中选择"跟随系统"
2. 在系统设置中切换深色模式
3. 返回应用，确认主题已自动切换

### iOS 测试
1. 在设置中选择"跟随系统"
2. 在控制中心切换深色模式
3. 返回应用，确认主题和状态栏已自动切换
4. 测试"浅色模式"和"深色模式"固定选项

## 注意事项

1. 主题设置会自动保存到本地存储
2. 应用重启后会恢复上次的主题设置
3. "跟随系统"模式下，系统主题变化会实时响应
4. iOS 状态栏样式会自动跟随主题变化
5. 使用 remember 缓存避免不必要的重组

## 相关文件

- `composeApp/src/commonMain/kotlin/com/example/juejin/theme/ThemeManager.kt`
- `composeApp/src/commonMain/kotlin/com/example/juejin/theme/SystemTheme.kt`
- `composeApp/src/androidMain/kotlin/com/example/juejin/theme/SystemTheme.android.kt`
- `composeApp/src/iosMain/kotlin/com/example/juejin/theme/SystemTheme.ios.kt`
- `composeApp/src/jvmMain/kotlin/com/example/juejin/theme/SystemTheme.jvm.kt`
- `composeApp/src/iosMain/kotlin/com/example/juejin/theme/ThemeNotification.ios.kt`
- `composeApp/src/commonMain/kotlin/com/example/juejin/viewmodel/SettingViewModel.kt`
- `composeApp/src/commonMain/kotlin/com/example/juejin/screen/SettingsScreen.kt`
- `composeApp/src/commonMain/kotlin/com/example/juejin/App.kt`
- `iosApp/iosApp/ContentView.swift`
