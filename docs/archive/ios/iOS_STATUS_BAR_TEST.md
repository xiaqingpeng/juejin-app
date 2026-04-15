# iOS 状态栏测试指南

## 已完成的配置

✅ Android 白色状态栏 + 深色图标
✅ iOS 深色图标（适配白色背景）
✅ 全局白色背景色
✅ Scaffold 容器白色背景

## 最新改动

1. **App.kt**
   - 添加 `MaterialTheme` 的 `colorScheme` 设置为白色
   - 添加 `Scaffold` 的 `containerColor = Colors.primaryWhite`
   - 确保整个应用背景为白色

2. **ContentView.swift**
   - 强制使用浅色模式 `.preferredColorScheme(.light)`
   - 这会让 iOS 状态栏图标自动变为深色

## 测试步骤

### iOS 测试
1. 在 Xcode 中清理构建：`Product > Clean Build Folder` (Cmd+Shift+K)
2. 重新运行应用
3. 观察状态栏：
   - ✅ 时间、电池、信号等图标应该是**深色/黑色**
   - ✅ 状态栏背景应该是**白色**（由应用内容提供）

### Android 测试
1. 卸载应用
2. 重新安装并运行
3. 观察状态栏：
   - ✅ 状态栏背景应该是**白色**
   - ✅ 图标应该是**深色/黑色**

## 如果 iOS 状态栏仍然不是深色

### 方案 1: 检查设备设置
- 确保设备不是在深色模式下
- 设置 > 显示与亮度 > 选择"浅色"

### 方案 2: 强制状态栏样式
在 `iosApp/iosApp/iOSApp.swift` 中添加：

```swift
import SwiftUI

@main
struct iOSApp: App {
    init() {
        // 强制状态栏为深色内容
        UIApplication.shared.statusBarStyle = .darkContent
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

### 方案 3: 检查 Info.plist
确保 `UIViewControllerBasedStatusBarAppearance` 设置为 `true`

## 预期效果

### iOS
- 状态栏图标：深色/黑色
- 状态栏背景：白色（由应用提供）
- 整体外观：浅色模式

### Android  
- 状态栏背景：白色
- 状态栏图标：深色/黑色
- 系统导航栏：跟随系统

## 日志确认

从你的日志中可以看到：
```
[iOS] 状态栏设置为浅色模式（深色文字/图标）
```

这表明配置已经生效。如果视觉上还没看到效果，请尝试：
1. 完全关闭应用
2. 清理 Xcode 缓存
3. 重新编译运行
