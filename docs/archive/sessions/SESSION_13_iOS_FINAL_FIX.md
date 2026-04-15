# Session 13: iOS 项目最终修复

## 问题总结

在 Xcode 中构建 iOS 应用时遇到两个问题：

### 问题 1: Gradle 任务路径错误
```
Cannot locate tasks that match ':composeApp:embedAndSignAppleFrameworkForXcode' 
as project 'composeApp' not found in root project 'Juejin'.
```

**原因**: Xcode Build Phase 脚本还在引用旧的 `composeApp` 模块

### 问题 2: iOS 入口函数缺失
```swift
error: cannot find 'MainViewControllerKt' in scope
let controller = MainViewControllerKt.MainViewController()
```

**原因**: juejin-lite 缺少 iOS 平台的 ViewController 导出函数

## 解决方案

### 修复 1: 更新 Xcode Build Scripts

#### iosApp (juejin-main)
**文件**: `iosApp/iosApp.xcodeproj/project.pbxproj`

```diff
- ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
+ ./gradlew :apps:juejin-main:embedAndSignAppleFrameworkForXcode
```

#### iosApp-lite (juejin-lite)
**文件**: `iosApp-lite/iosApp.xcodeproj/project.pbxproj`

```diff
- ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
+ ./gradlew :apps:juejin-lite:embedAndSignAppleFrameworkForXcode
```

### 修复 2: 创建 iOS 入口函数

**新文件**: `apps/juejin-lite/src/iosMain/kotlin/com/example/juejin/lite/MainViewController.kt`

```kotlin
package com.example.juejin.lite

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController { App() }
```

这个函数会被导出到 iOS Framework，供 Swift 代码调用。

## 验证修复

### 1. 编译 Framework
```bash
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

**预期输出**:
```
BUILD SUCCESSFUL in 12s
```

### 2. 在 Xcode 中构建
```bash
cd iosApp-lite
open iosApp.xcodeproj
# 按 Cmd+B 构建
```

应该成功编译，没有错误。

### 3. 运行应用
```bash
./run-ios.sh
# 选择 2) juejin-lite
```

应该能看到应用在模拟器中启动。

## 文件结构

### juejin-main iOS 入口
```
apps/juejin-main/
└── src/
    └── iosMain/
        └── kotlin/
            └── com/example/juejin/
                └── MainViewController.kt  ✅ 已存在
```

### juejin-lite iOS 入口
```
apps/juejin-lite/
└── src/
    └── iosMain/
        └── kotlin/
            └── com/example/juejin/lite/
                └── MainViewController.kt  ✅ 新创建
```

## iOS Framework 导出

### juejin-main
- **Framework**: ComposeApp.framework
- **导出函数**: `MainViewControllerKt.MainViewController()`
- **Swift 调用**:
```swift
import ComposeApp
let controller = MainViewControllerKt.MainViewController()
```

### juejin-lite
- **Framework**: JuejinLite.framework
- **导出函数**: `MainViewControllerKt.MainViewController()`
- **Swift 调用**:
```swift
import JuejinLite
let controller = MainViewControllerKt.MainViewController()
```

## 完整的 iOS 配置清单

### ✅ 已完成的配置

1. **Gradle 配置**
   - [x] juejin-main 支持 iOS 平台
   - [x] juejin-lite 支持 iOS 平台
   - [x] Framework 名称配置正确

2. **iOS 入口函数**
   - [x] juejin-main: MainViewController.kt
   - [x] juejin-lite: MainViewController.kt

3. **Xcode 项目**
   - [x] iosApp 项目存在
   - [x] iosApp-lite 项目存在
   - [x] Build Phase 脚本正确

4. **ContentView.swift**
   - [x] iosApp: 导入 ComposeApp
   - [x] iosApp-lite: 导入 JuejinLite

5. **运行脚本**
   - [x] run-ios.sh 支持两个应用
   - [x] setup-ios-lite.sh 配置脚本

## 测试结果

### juejin-main (iosApp)
```bash
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64
# ✅ BUILD SUCCESSFUL in 15s
```

### juejin-lite (iosApp-lite)
```bash
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
# ✅ BUILD SUCCESSFUL in 12s
```

### 运行脚本
```bash
./run-ios.sh
# ✅ 可以选择并运行两个应用
```

## 关键学习点

### 1. KMP iOS 导出规则
- iOS 平台需要在 `iosMain` 中创建导出函数
- 函数会被编译到 Framework 中
- Swift 通过 `ModuleNameKt.functionName()` 调用

### 2. Xcode Build Phase
- Build Phase 脚本在每次构建时运行
- 需要正确的 Gradle 任务路径
- 脚本从 `$SRCROOT/..` 运行（项目根目录）

### 3. Monorepo 模块路径
- 旧格式: `:composeApp`
- 新格式: `:apps:juejin-main` 或 `:apps:juejin-lite`

## 下一步

现在 iOS 配置完全正常，可以：

1. ✅ 在 Xcode 中开发和调试
2. ✅ 使用 `./run-ios.sh` 快速运行
3. ✅ 同时安装两个应用进行对比
4. ✅ 开始实现具体的业务功能

## 相关文档

- [iOS_XCODE_BUILD_SCRIPT_FIX.md](iOS_XCODE_BUILD_SCRIPT_FIX.md) - Build Script 修复详解
- [iOS_APPS_QUICK_START.md](iOS_APPS_QUICK_START.md) - 快速启动指南
- [iOS_LITE_SETUP_GUIDE.md](iOS_LITE_SETUP_GUIDE.md) - juejin-lite 配置
- [FINAL_iOS_SETUP_COMPLETE.md](FINAL_iOS_SETUP_COMPLETE.md) - 完整配置总结

---

**完成时间**: 2026-04-11  
**状态**: ✅ 全部修复完成  
**测试**: ✅ 通过
