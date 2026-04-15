# iOS Xcode Build Script 修复

## 问题描述

在 Xcode 中构建 `iosApp-lite` 时出现错误：

```
Cannot locate tasks that match ':composeApp:embedAndSignAppleFrameworkForXcode' 
as project 'composeApp' not found in root project 'Juejin'.
```

## 根本原因

Xcode 项目中的 Build Phase 脚本还在引用旧的模块名称 `composeApp`，但项目已经重构为 Monorepo 架构，模块路径改为：
- `apps/juejin-main`
- `apps/juejin-lite`

## 解决方案

### ✅ 已修复

已更新两个 Xcode 项目中的构建脚本：

**iosApp-lite/iosApp.xcodeproj/project.pbxproj:**
```diff
- ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
+ ./gradlew :apps:juejin-lite:embedAndSignAppleFrameworkForXcode
```

**iosApp/iosApp.xcodeproj/project.pbxproj:**
```diff
- ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
+ ./gradlew :apps:juejin-main:embedAndSignAppleFrameworkForXcode
```

### 手动修复方法（如果需要）

如果需要手动修复或修复 `iosApp` 项目：

1. **在 Xcode 中打开项目**
```bash
cd iosApp-lite  # 或 iosApp
open iosApp.xcodeproj
```

2. **编辑 Build Phase**
   - 选择项目 → Target → Build Phases
   - 找到 "Compile Kotlin Framework" 脚本
   - 修改脚本内容：

**iosApp-lite (juejin-lite):**
```bash
cd "$SRCROOT/.."
./gradlew :apps:juejin-lite:embedAndSignAppleFrameworkForXcode
```

**iosApp (juejin-main):**
```bash
cd "$SRCROOT/.."
./gradlew :apps:juejin-main:embedAndSignAppleFrameworkForXcode
```

3. **保存并重新构建**
   - Cmd+B 或点击运行按钮

## 验证修复

### 方法 1: 在 Xcode 中构建

```bash
cd iosApp-lite
open iosApp.xcodeproj
# 按 Cmd+B 构建
```

应该看到：
```
Configuration on demand is an incubating feature.
BUILD SUCCESSFUL in Xs
```

### 方法 2: 使用脚本

```bash
./run-ios.sh
# 选择 2) juejin-lite
```

## 相关文件

### ✅ 已修复的文件
- `iosApp-lite/iosApp.xcodeproj/project.pbxproj` - ✅ 已修复
- `iosApp/iosApp.xcodeproj/project.pbxproj` - ✅ 已修复

两个 iOS 项目的 Build Phase 脚本都已更新为正确的 Gradle 任务路径。

## 其他可能的问题

### 问题 1: Framework 路径错误

如果看到 `framework not found` 错误，检查 Framework Search Paths：

在 Xcode 中：
- Target → Build Settings → Framework Search Paths
- 应该包含：`$(SRCROOT)/../apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework`

### 问题 2: 模块名称不匹配

确保 ContentView.swift 中的 import 语句正确：

**iosApp-lite:**
```swift
import JuejinLite
```

**iosApp:**
```swift
import ComposeApp
```

## 完整的 Gradle 任务路径

| 应用 | 模块路径 | Gradle 任务 |
|------|---------|------------|
| juejin-main | `apps/juejin-main` | `:apps:juejin-main:embedAndSignAppleFrameworkForXcode` |
| juejin-lite | `apps/juejin-lite` | `:apps:juejin-lite:embedAndSignAppleFrameworkForXcode` |

## 测试步骤

1. **清理构建**
```bash
# 清理 Xcode
rm -rf ~/Library/Developer/Xcode/DerivedData/iosApp-*

# 清理 Gradle
./gradlew clean
```

2. **重新编译 Framework**
```bash
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

3. **在 Xcode 中构建**
```bash
cd iosApp-lite
open iosApp.xcodeproj
# Cmd+B
```

4. **运行应用**
```bash
./run-ios.sh
# 选择 2
```

## 预防措施

创建新的 iOS 项目时，确保：

1. Build Phase 脚本使用正确的模块路径
2. Framework 名称与模块配置一致
3. ContentView.swift 中的 import 语句正确

## 相关文档

- [iOS_APPS_QUICK_START.md](iOS_APPS_QUICK_START.md) - iOS 快速启动
- [iOS_LITE_SETUP_GUIDE.md](iOS_LITE_SETUP_GUIDE.md) - juejin-lite 配置
- [KMP_MONOREPO_GUIDE.md](KMP_MONOREPO_GUIDE.md) - Monorepo 架构

---

**修复时间**: 2026-04-11  
**状态**: ✅ 已修复
