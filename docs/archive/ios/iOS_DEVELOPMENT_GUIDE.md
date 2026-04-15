# 🍎 iOS 开发指南

## 📋 概述

本指南介绍如何在 KMP 项目中进行 iOS 开发，包括 Framework 编译、Xcode 集成和模拟器管理。

## 🚀 快速开始

### 前置要求

- macOS 系统
- Xcode 15+
- CocoaPods（可选）
- iOS Simulator 或真机

### 编译 iOS Framework

#### 使用 Gradle 命令

```bash
# 模拟器 (Simulator)
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

# 真机 (Device)
./gradlew :apps:juejin-lite:linkDebugFrameworkIosArm64

# Release 版本
./gradlew :apps:juejin-lite:linkReleaseFrameworkIosArm64
```

#### 使用 Android Studio

1. 选择运行配置：`juejin-lite (iOS)`
2. 点击运行按钮 (▶️)
3. 查看 Build 窗口确认编译成功

### Framework 位置

```
apps/juejin-lite/build/bin/
├── iosSimulatorArm64/
│   └── debugFramework/
│       └── JuejinLite.framework
└── iosArm64/
    └── debugFramework/
        └── JuejinLite.framework
```

## 📱 iOS 模拟器管理

### 查看所有模拟器

```bash
# 列出所有模拟器
xcrun simctl list devices

# 只显示可用的模拟器
xcrun simctl list devices available

# 显示运行中的模拟器
xcrun simctl list devices | grep Booted
```

### 清理模拟器

#### 方法 1: 删除不可用的模拟器（推荐）

```bash
# 删除所有标记为 "unavailable" 的模拟器
xcrun simctl delete unavailable
```

这个命令会：
- ✅ 删除已损坏的模拟器
- ✅ 删除无效的模拟器数据
- ✅ 释放磁盘空间
- ✅ 不影响正常的模拟器

#### 方法 2: 重置特定模拟器

```bash
# 查找模拟器 UUID
xcrun simctl list devices

# 重置指定模拟器（清除数据但保留模拟器）
xcrun simctl erase <DEVICE_UUID>

# 示例
xcrun simctl erase 12345678-1234-1234-1234-123456789012
```

#### 方法 3: 重置所有模拟器

```bash
# ⚠️ 警告：这会清除所有模拟器的数据
xcrun simctl erase all
```

#### 方法 4: 删除特定模拟器

```bash
# 删除指定模拟器
xcrun simctl delete <DEVICE_UUID>

# 删除所有不可用的模拟器
xcrun simctl delete unavailable
```

#### 方法 5: 在 Xcode 中管理

```
1. 打开 Xcode
2. Window → Devices and Simulators (Cmd + Shift + 2)
3. 选择 Simulators 标签
4. 右键点击模拟器
   - Delete: 删除模拟器
   - Erase All Content and Settings: 重置模拟器
```

### 创建新模拟器

```bash
# 列出可用的设备类型
xcrun simctl list devicetypes

# 列出可用的运行时
xcrun simctl list runtimes

# 创建新模拟器
xcrun simctl create "iPhone 15 Pro" "iPhone 15 Pro" "iOS17.0"
```

### 启动和关闭模拟器

```bash
# 启动模拟器
xcrun simctl boot <DEVICE_UUID>

# 或者直接打开模拟器应用
open -a Simulator

# 关闭模拟器
xcrun simctl shutdown <DEVICE_UUID>

# 关闭所有模拟器
xcrun simctl shutdown all
```

## 🔧 常见问题

### Q1: 模拟器启动失败
**解决方案**:
```bash
# 1. 删除不可用的模拟器
xcrun simctl delete unavailable

# 2. 重启 Simulator 应用
killall Simulator
open -a Simulator

# 3. 如果还是失败，重置所有模拟器
xcrun simctl erase all
```

### Q2: Framework 编译失败
**解决方案**:
```bash
# 1. 清理构建
./gradlew clean

# 2. 删除 Kotlin 缓存
rm -rf ~/.konan

# 3. 重新编译
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

### Q3: 模拟器占用空间过大
**解决方案**:
```bash
# 查看模拟器数据大小
du -sh ~/Library/Developer/CoreSimulator/Devices/*

# 删除不可用的模拟器
xcrun simctl delete unavailable

# 清理所有模拟器数据
xcrun simctl erase all
```

### Q4: 找不到模拟器
**解决方案**:
```bash
# 1. 检查 Xcode 命令行工具
xcode-select -p

# 2. 如果路径不正确，重新设置
sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer

# 3. 重新安装命令行工具
xcode-select --install
```

### Q5: 模拟器运行缓慢
**解决方案**:
```bash
# 1. 关闭所有模拟器
xcrun simctl shutdown all

# 2. 重置模拟器
xcrun simctl erase all

# 3. 在 Xcode 中创建新的模拟器
# 选择较新的 iOS 版本和较少的内存占用
```

## 📊 模拟器维护建议

### 定期清理（推荐）

```bash
# 每周运行一次
xcrun simctl delete unavailable

# 每月运行一次（如果空间不足）
xcrun simctl erase all
```

### 磁盘空间管理

```bash
# 查看模拟器占用空间
du -sh ~/Library/Developer/CoreSimulator/Devices

# 查看每个模拟器的大小
du -sh ~/Library/Developer/CoreSimulator/Devices/*

# 删除旧的模拟器数据
xcrun simctl delete unavailable
```

### 性能优化

1. **只保留需要的模拟器**
   - 删除不常用的 iOS 版本
   - 删除不常用的设备型号

2. **定期重置模拟器**
   - 清除应用数据
   - 释放缓存空间

3. **使用最新的 Xcode**
   - 更好的性能
   - 更少的 bug

## 🎯 开发工作流

### 推荐流程

```
1. 在 Android Studio 中开发共享代码
   ├─ 使用 Desktop 进行快速测试
   └─ 使用 Android 验证移动端功能

2. 编译 iOS Framework
   └─ ./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

3. 在 Xcode 中集成和测试
   ├─ 添加 Framework 到项目
   ├─ 在模拟器中运行
   └─ 调试 iOS 特定功能

4. 定期清理模拟器
   └─ xcrun simctl delete unavailable
```

### 时间分配

```
Android Studio 开发: 80%
├─ Desktop 测试: 50%
├─ Android 测试: 25%
└─ 编译 iOS Framework: 5%

Xcode 开发: 20%
├─ iOS 特定功能: 10%
├─ UI 调整: 5%
└─ 测试验证: 5%
```

## 🔍 调试技巧

### Framework 调试

在 Xcode 中调试 KMP 代码：

1. **设置断点**
   - 在 Swift/Objective-C 代码中设置断点
   - 可以查看 Kotlin 函数的调用栈

2. **查看日志**
   ```swift
   // 在 Swift 中调用 Kotlin 代码
   let result = KotlinClass().someFunction()
   print("Result: \(result)")
   ```

3. **使用 Kermit 日志**
   ```kotlin
   // 在 Kotlin 中
   Logger.d { "iOS specific log" }
   ```

### 性能分析

```bash
# 使用 Instruments 分析性能
# Xcode → Open Developer Tool → Instruments
```

## 📚 相关命令速查

### 模拟器管理
```bash
xcrun simctl list devices              # 列出所有模拟器
xcrun simctl delete unavailable        # 删除不可用的模拟器
xcrun simctl erase all                 # 重置所有模拟器
xcrun simctl shutdown all              # 关闭所有模拟器
```

### Framework 编译
```bash
# juejin-lite
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
./gradlew :apps:juejin-lite:linkDebugFrameworkIosArm64

# juejin-main
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64
./gradlew :apps:juejin-main:linkDebugFrameworkIosArm64
```

### 清理和重置
```bash
./gradlew clean                        # 清理 Gradle 构建
rm -rf ~/.konan                        # 删除 Kotlin Native 缓存
xcrun simctl delete unavailable        # 清理模拟器
```

## 🎨 Xcode 项目集成

### 步骤 1: 创建 iOS 项目

1. 打开 Xcode
2. File → New → Project
3. 选择 "App"
4. 填写项目信息

### 步骤 2: 添加 Framework

1. 编译 Framework
   ```bash
   ./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
   ```

2. 在 Xcode 中添加
   - 选择项目 → Target → General
   - Frameworks, Libraries, and Embedded Content
   - 点击 "+" 添加 Framework
   - 选择 `JuejinLite.framework`

### 步骤 3: 配置 Build Phases

添加脚本自动编译 Framework：

```bash
cd "$SRCROOT/.."
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

### 步骤 4: 使用 Framework

```swift
import SwiftUI
import JuejinLite

struct ContentView: View {
    var body: some View {
        // 使用 Kotlin 代码
        Text(Platform().name)
    }
}
```

## 📖 相关文档

- [README.md](README.md) - 项目主文档
- [ALL_PLATFORMS_COMPLETE.md](ALL_PLATFORMS_COMPLETE.md) - 多平台指南
- [ANDROID_STUDIO_MULTIPLATFORM.md](ANDROID_STUDIO_MULTIPLATFORM.md) - Android Studio 配置

## 🔗 有用的链接

- [Xcode Documentation](https://developer.apple.com/documentation/xcode)
- [iOS Simulator Guide](https://developer.apple.com/documentation/xcode/running-your-app-in-simulator-or-on-a-device)
- [Kotlin Multiplatform for iOS](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html)

---

**更新时间**: 2026-04-11  
**状态**: ✅ iOS 开发指南完成
