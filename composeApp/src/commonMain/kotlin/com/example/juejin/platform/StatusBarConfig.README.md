# 状态栏配置使用说明

## 概述
这个模块提供了统一的 Android 和 iOS 状态栏配置接口，可以轻松控制状态栏的样式和颜色。

## 使用方法

### 1. 在 Composable 中使用

```kotlin
import com.example.juejin.ui.components.StatusBarEffect
import com.example.juejin.ui.Colors

@Composable
fun MyScreen() {
    // 设置浅色状态栏（深色文字/图标）
    StatusBarEffect(isDark = false, color = Colors.primaryWhite)
    
    // 或设置深色状态栏（浅色文字/图标）
    // StatusBarEffect(isDark = true, color = Color.Black)
    
    // 你的 UI 内容
}
```

### 2. 直接使用 API

```kotlin
import com.example.juejin.platform.getStatusBarConfig

val statusBarConfig = getStatusBarConfig()

// 设置浅色模式（深色文字）
statusBarConfig.setLightStatusBar()

// 设置深色模式（浅色文字）
statusBarConfig.setDarkStatusBar()

// 设置状态栏颜色（仅 Android）
statusBarConfig.setStatusBarColor(0xFFFFFFFF)
```

## 平台差异

### Android
- 支持设置状态栏文字/图标颜色（浅色/深色）
- 支持设置状态栏背景色
- Android 11+ 使用新的 WindowInsetsController API
- Android 11 以下使用 systemUiVisibility

### iOS
- 支持设置状态栏文字/图标颜色（浅色/深色）
- 不支持直接设置状态栏背景色（背景色由应用内容决定）
- 需要在 Info.plist 中配置 `UIViewControllerBasedStatusBarAppearance = NO`

## iOS 配置

在 iOS 项目的 `Info.plist` 中添加：

```xml
<key>UIViewControllerBasedStatusBarAppearance</key>
<false/>
```

或者在 `iosApp/Configuration/Config.xcconfig` 中添加：

```
INFOPLIST_KEY_UIViewControllerBasedStatusBarAppearance = NO
```

## 注意事项

1. `StatusBarEffect` 会在 Composable 进入时设置状态栏，离开时不会恢复
2. 如果多个屏幕需要不同的状态栏样式，在每个屏幕的根 Composable 中调用 `StatusBarEffect`
3. iOS 的状态栏背景色由应用内容决定，建议在顶部使用相应颜色的 Surface 或 Box
