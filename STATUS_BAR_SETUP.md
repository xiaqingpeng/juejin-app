# 状态栏设置完成说明

## 已完成的配置

### Android
✅ 在 `MainActivity.kt` 中使用 `SystemBarStyle.light()` 设置白色状态栏
✅ 状态栏背景：白色
✅ 状态栏图标/文字：深色

### iOS
✅ 在 `ContentView.swift` 中设置 `.preferredColorScheme(.light)` 强制浅色模式
✅ 在 `Info.plist` 中设置 `UIViewControllerBasedStatusBarAppearance = true`
✅ 状态栏图标/文字：深色（适配白色背景）

## 文件修改清单

### Android 平台
1. `composeApp/src/androidMain/kotlin/com/example/juejin/MainActivity.kt`
   - 使用 `enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(...))`
   
2. `composeApp/src/androidMain/kotlin/com/example/juejin/platform/StatusBarConfig.android.kt`
   - 实现动态状态栏控制

### iOS 平台
1. `iosApp/iosApp/ContentView.swift`
   - 添加 `.preferredColorScheme(.light)` 强制浅色模式
   - 设置 `controller.overrideUserInterfaceStyle = .light`

2. `iosApp/iosApp/Info.plist`
   - 设置 `UIViewControllerBasedStatusBarAppearance = true`

3. `composeApp/src/iosMain/kotlin/com/example/juejin/MainViewController.kt`
   - 简化为直接返回 ComposeUIViewController

4. `composeApp/src/iosMain/kotlin/com/example/juejin/platform/StatusBarConfig.ios.kt`
   - 简化实现（iOS 主要通过 Swift 层控制）

### 通用代码
1. `composeApp/src/commonMain/kotlin/com/example/juejin/platform/StatusBarConfig.kt`
   - 定义跨平台接口

2. `composeApp/src/commonMain/kotlin/com/example/juejin/ui/components/StatusBarEffect.kt`
   - 提供 Composable 函数方便使用

3. `composeApp/src/commonMain/kotlin/com/example/juejin/App.kt`
   - 在应用启动时调用 `StatusBarEffect(isDark = false, color = Colors.primaryWhite)`

## 使用方法

### 全局设置（已配置）
在 `App.kt` 中已经设置了全局白色状态栏：
```kotlin
StatusBarEffect(isDark = false, color = Colors.primaryWhite)
```

### 在特定屏幕使用不同样式
如果某个屏幕需要深色状态栏，可以在该屏幕添加：
```kotlin
@Composable
fun MyDarkScreen() {
    StatusBarEffect(isDark = true, color = Color.Black)
    // 你的 UI 内容
}
```

## 测试建议

1. **清理并重新构建**
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

2. **Android 测试**
   - 卸载应用后重新安装
   - 检查状态栏是否为白色背景、深色图标

3. **iOS 测试**
   - 在 Xcode 中清理构建文件夹（Product > Clean Build Folder）
   - 重新运行应用
   - 检查状态栏图标是否为深色

## 注意事项

- **Android**: 状态栏背景色可以自定义，图标颜色会自动适配
- **iOS**: 状态栏背景色由应用内容决定，无法直接设置。通过强制浅色模式来确保深色图标
- 如果需要动态切换状态栏样式，在对应的屏幕调用 `StatusBarEffect` 即可
