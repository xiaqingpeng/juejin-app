# 🚀 Android Studio 多平台开发指南

## 📋 概述

本指南将帮助你在 Android Studio 中配置和使用 Android、iOS 和 Desktop 的开发、编译和调试功能。

## ✅ 前置要求

### 必需
- Android Studio Ladybug (2024.2.1) 或更高版本
- JDK 11+
- Kotlin Multiplatform Mobile (KMM) 插件

### Android 开发
- Android SDK (API 24+)
- Android Emulator 或真机

### iOS 开发（仅 macOS）
- Xcode 15+
- iOS Simulator 或真机
- CocoaPods（可选）

### Desktop 开发
- 无额外要求（JDK 已包含）

## 🔧 安装插件

### 1. Kotlin Multiplatform Mobile
1. 打开 Android Studio
2. `Settings/Preferences` → `Plugins`
3. 搜索 "Kotlin Multiplatform Mobile"
4. 点击 `Install`
5. 重启 Android Studio

### 2. Compose Multiplatform IDE Support（可选）
1. `Settings/Preferences` → `Plugins`
2. 搜索 "Compose Multiplatform"
3. 点击 `Install`

## 🎯 运行配置

Android Studio 已自动配置了以下运行配置：

### Android 配置
```
📱 juejin-main (Android)
📱 juejin-lite (Android)
```

### Desktop 配置
```
🖥️ juejin-main (Desktop)
🖥️ juejin-lite (Desktop)
```

### iOS 配置
```
🍎 juejin-main (iOS)
🍎 juejin-lite (iOS)
```

## 🚀 运行和调试

### Android

#### 运行
1. 选择运行配置：`juejin-main` 或 `juejin-lite`
2. 选择目标设备（模拟器或真机）
3. 点击 `Run` (▶️) 或按 `Shift + F10`

#### 调试
1. 设置断点（点击代码行号左侧）
2. 点击 `Debug` (🐛) 或按 `Shift + F9`
3. 使用调试工具栏控制执行

#### 快捷键
- `Shift + F10` - 运行
- `Shift + F9` - 调试
- `Ctrl + F2` - 停止
- `F8` - 单步跳过
- `F7` - 单步进入

### Desktop

#### 运行
1. 选择运行配置：`juejin-main (Desktop)` 或 `juejin-lite (Desktop)`
2. 点击 `Run` (▶️) 或按 `Shift + F10`
3. Desktop 窗口将自动打开

#### 调试
1. 设置断点
2. 点击 `Debug` (🐛) 或按 `Shift + F9`
3. 使用标准 JVM 调试功能

#### 特点
- ✅ 热重载支持（修改代码后自动刷新）
- ✅ 完整的 JVM 调试功能
- ✅ 快速启动（无需模拟器）
- ✅ 窗口大小可调整

### iOS

#### 编译 Framework
1. 选择运行配置：`juejin-main (iOS)` 或 `juejin-lite (iOS)`
2. 点击 `Run` (▶️)
3. Framework 将生成在 `build/bin/iosSimulatorArm64/debugFramework/`

#### 在 Xcode 中运行
1. 编译 Framework（如上）
2. 打开 Xcode 项目（需要单独创建）
3. 添加生成的 Framework
4. 在 Xcode 中运行和调试

#### 注意事项
- ⚠️ Android Studio 不能直接运行 iOS 应用
- ⚠️ 需要在 Xcode 中进行 iOS 调试
- ✅ 可以在 Android Studio 中编辑共享代码
- ✅ Framework 会自动重新编译

## 📊 平台对比

| 功能 | Android | Desktop | iOS |
|------|---------|---------|-----|
| 直接运行 | ✅ | ✅ | ❌ |
| 断点调试 | ✅ | ✅ | ❌* |
| 热重载 | ✅ | ✅ | ❌ |
| 代码编辑 | ✅ | ✅ | ✅ |
| 编译 | ✅ | ✅ | ✅ |
| 性能分析 | ✅ | ⚠️ | ❌ |

*iOS 调试需要在 Xcode 中进行

## 🎯 推荐工作流

### 日常开发（最快）
```
1. 使用 Desktop 配置进行快速开发和测试
   - 启动快（无需模拟器）
   - 调试方便
   - 热重载支持

2. 定期在 Android 上测试
   - 验证 Android 特定功能
   - 测试触摸交互

3. 最后在 iOS 上验证
   - 编译 Framework
   - 在 Xcode 中测试
```

### 跨平台测试
```
1. Desktop: 快速验证业务逻辑
2. Android: 测试移动端交互
3. iOS: 最终验证（通过 Xcode）
```

## 🔍 调试技巧

### 共享代码调试
```kotlin
// commonMain 中的代码可以在所有平台调试
fun calculateTotal(items: List<Item>): Double {
    // 在这里设置断点
    return items.sumOf { it.price }
}
```

### 平台特定调试
```kotlin
// androidMain
actual fun getPlatformName(): String {
    // Android 断点
    return "Android"
}

// jvmMain (Desktop)
actual fun getPlatformName(): String {
    // Desktop 断点
    return "Desktop"
}

// iosMain
actual fun getPlatformName(): String {
    // 需要在 Xcode 中调试
    return "iOS"
}
```

### 日志调试
```kotlin
import co.touchlab.kermit.Logger

// 所有平台都支持
Logger.d { "Debug message" }
Logger.i { "Info message" }
Logger.e { "Error message" }
```

## 🛠️ 常用操作

### 切换运行配置
```
方法 1: 顶部工具栏下拉菜单
方法 2: Alt + Shift + F10 (Windows/Linux)
方法 2: Ctrl + Option + R (macOS)
```

### 清理构建
```
Build → Clean Project
或
./gradlew clean
```

### 重新同步 Gradle
```
File → Sync Project with Gradle Files
或
Ctrl + Shift + O (Windows/Linux)
Cmd + Shift + O (macOS)
```

### 查看构建输出
```
View → Tool Windows → Build
或
Alt + 6 (Windows/Linux)
Cmd + 6 (macOS)
```

## 📱 设备管理

### Android
```
Tools → Device Manager
或
View → Tool Windows → Device Manager
```

### iOS（需要 Xcode）
```
在 Xcode 中管理:
Window → Devices and Simulators
```

## 🎨 代码导航

### 跨平台代码跳转
```
Ctrl + Click (Windows/Linux)
Cmd + Click (macOS)
```

### 查找 expect/actual 实现
```
Ctrl + Alt + B (Windows/Linux)
Cmd + Option + B (macOS)
```

### 查找用法
```
Alt + F7 (Windows/Linux)
Option + F7 (macOS)
```

## 🔧 配置优化

### 增加内存（推荐）
编辑 `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=1024m
```

### 启用并行编译
```properties
org.gradle.parallel=true
org.gradle.caching=true
```

### 配置 Gradle Daemon
```properties
org.gradle.daemon=true
org.gradle.configureondemand=true
```

## 📊 编译时间优化

### 开发时
```bash
# 只编译需要的平台
./gradlew :apps:juejin-lite:assembleDebug  # Android only
```

### 使用 Configuration Cache
```bash
./gradlew --configuration-cache :apps:juejin-lite:assembleDebug
```

### 增量编译
```
默认启用，修改代码后只重新编译变更部分
```

## 🐛 常见问题

### Q1: Desktop 配置找不到
**A**: 检查模块名称是否正确：`Juejin.apps.juejin-lite.jvmMain`

### Q2: iOS 配置运行后没有应用启动
**A**: iOS 配置只编译 Framework，需要在 Xcode 中运行应用

### Q3: Android 模拟器启动失败
**A**: 
1. 检查 HAXM/KVM 是否安装
2. 在 Device Manager 中重新创建模拟器
3. 尝试使用真机

### Q4: Desktop 应用无法启动
**A**: 
1. 检查 JDK 版本（需要 11+）
2. 清理构建：`./gradlew clean`
3. 重新同步 Gradle

### Q5: 代码修改后不生效
**A**: 
1. 重新构建：`Build → Rebuild Project`
2. 清理缓存：`File → Invalidate Caches / Restart`

## 📚 快捷键速查

### 运行和调试
| 操作 | Windows/Linux | macOS |
|------|--------------|-------|
| 运行 | `Shift + F10` | `Ctrl + R` |
| 调试 | `Shift + F9` | `Ctrl + D` |
| 停止 | `Ctrl + F2` | `Cmd + F2` |
| 选择配置 | `Alt + Shift + F10` | `Ctrl + Option + R` |

### 调试
| 操作 | Windows/Linux | macOS |
|------|--------------|-------|
| 单步跳过 | `F8` | `F8` |
| 单步进入 | `F7` | `F7` |
| 单步跳出 | `Shift + F8` | `Shift + F8` |
| 继续执行 | `F9` | `F9` |
| 查看变量 | `Alt + F8` | `Option + F8` |

### 导航
| 操作 | Windows/Linux | macOS |
|------|--------------|-------|
| 跳转到定义 | `Ctrl + B` | `Cmd + B` |
| 查找用法 | `Alt + F7` | `Option + F7` |
| 最近文件 | `Ctrl + E` | `Cmd + E` |
| 搜索文件 | `Ctrl + Shift + N` | `Cmd + Shift + O` |

## 🎯 最佳实践

### 1. 使用 Desktop 进行快速开发
```
Desktop 启动最快，适合：
- 快速验证业务逻辑
- UI 布局调整
- 性能测试
```

### 2. 定期在 Android 上测试
```
Android 是主要目标平台：
- 每天至少测试一次
- 验证触摸交互
- 测试 Android 特定功能
```

### 3. 最后在 iOS 上验证
```
iOS 编译较慢：
- 功能完成后再测试
- 使用 Xcode 进行最终调试
- 验证 iOS 特定行为
```

### 4. 共享代码优先
```
尽量在 commonMain 中实现：
- 业务逻辑
- UI 组件
- 数据模型
- 网络请求
```

### 5. 平台特定代码最小化
```
只在必要时使用平台特定代码：
- 系统 API 调用
- 平台特定 UI
- 硬件访问
```

## 📖 相关文档

- [README.md](README.md) - 项目主文档
- [QUICK_COMMANDS.md](QUICK_COMMANDS.md) - 快速命令
- [ALL_PLATFORMS_COMPLETE.md](ALL_PLATFORMS_COMPLETE.md) - 多平台指南
- [ANDROID_STUDIO_RUN_CONFIG.md](ANDROID_STUDIO_RUN_CONFIG.md) - Android 配置

## 🎉 总结

现在你可以在 Android Studio 中：

✅ 运行和调试 Android 应用  
✅ 运行和调试 Desktop 应用  
✅ 编译 iOS Framework  
✅ 编辑所有平台的共享代码  
✅ 使用统一的开发环境  

推荐工作流：Desktop 开发 → Android 测试 → iOS 验证

---

**更新时间**: 2026-04-11  
**状态**: ✅ 多平台开发环境配置完成
