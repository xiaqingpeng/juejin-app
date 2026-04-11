# 🍎 juejin-lite iOS 项目配置指南

## 📋 概述

本指南将帮助你配置 `juejin-lite` 的 iOS 项目，使其可以在 iOS 模拟器和真机上运行。

## 🚀 快速开始

### 方法 1: 使用自动配置脚本（推荐）

```bash
./setup-ios-lite.sh
```

这个脚本会自动：
1. ✅ 编译 JuejinLite Framework
2. ✅ 创建 iosApp-lite 目录
3. ✅ 更新 ContentView.swift
4. ✅ 显示下一步操作

### 方法 2: 手动配置

按照下面的详细步骤进行配置。

## 📝 详细配置步骤

### 步骤 1: 编译 Framework

```bash
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

**输出位置**:
```
apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/JuejinLite.framework
```

### 步骤 2: 创建 iOS 项目

#### 选项 A: 复制现有项目（推荐）

```bash
cp -r iosApp iosApp-lite
```

#### 选项 B: 在 Xcode 中创建新项目

1. 打开 Xcode
2. `File` → `New` → `Project`
3. 选择 `iOS` → `App`
4. 填写信息：
   - Product Name: `iosApp-lite`
   - Organization Identifier: `com.example.juejin`
   - Interface: `SwiftUI`
   - Language: `Swift`
5. 保存到项目根目录

### 步骤 3: 更新 ContentView.swift

打开 `iosApp-lite/iosApp/ContentView.swift`，修改以下内容：

```swift
// 修改 import
import JuejinLite  // 原来是 import ComposeApp

// 修改应用名称
Text("掘金轻量版")  // 原来是 "掘金 APP"
```

### 步骤 4: 在 Xcode 中配置 Framework

#### 4.1 打开项目

```bash
cd iosApp-lite
open iosApp.xcodeproj
```

#### 4.2 删除旧的 Framework（如果有）

1. 选择项目 → Target → General
2. 找到 `Frameworks, Libraries, and Embedded Content`
3. 删除 `ComposeApp.framework`（如果存在）

#### 4.3 添加 JuejinLite Framework

1. 在 `Frameworks, Libraries, and Embedded Content` 部分
2. 点击 `+` 按钮
3. 点击 `Add Other...` → `Add Files...`
4. 导航到：
   ```
   apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/JuejinLite.framework
   ```
5. 选择 Framework
6. 确保选择 `Embed & Sign`

#### 4.4 配置 Framework 搜索路径（如果需要）

1. 选择 Target → Build Settings
2. 搜索 `Framework Search Paths`
3. 添加：
   ```
   $(PROJECT_DIR)/../apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework
   ```

### 步骤 5: 配置自动编译 Framework

#### 5.1 添加 Build Phase

1. 选择 Target → Build Phases
2. 点击 `+` → `New Run Script Phase`
3. 将脚本拖到 `Compile Sources` 之前
4. 添加脚本内容：

```bash
cd "$SRCROOT/.."
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

#### 5.2 配置脚本选项

- Name: `Compile Kotlin Framework`
- Shell: `/bin/sh`
- Show environment variables in build log: ✅
- Based on dependency analysis: ❌

### 步骤 6: 配置 Bundle Identifier

1. 选择 Target → Signing & Capabilities
2. Bundle Identifier: `com.example.juejin.lite`
3. Team: 选择你的开发团队

### 步骤 7: 运行应用

#### 方法 1: 在 Xcode 中运行

1. 选择模拟器（顶部工具栏）
2. 点击运行按钮 (▶️) 或按 `Cmd + R`

#### 方法 2: 使用脚本

```bash
cd ..  # 回到项目根目录
./run-ios.sh
# 选择 2 - juejin-lite（轻量版）
```

## 🎨 项目结构

```
iosApp-lite/
├── iosApp.xcodeproj/          # Xcode 项目文件
├── iosApp/
│   ├── iOSApp.swift           # 应用入口
│   ├── ContentView.swift      # 主视图（已修改）
│   ├── Info.plist             # 应用配置
│   └── Assets.xcassets/       # 资源文件
└── Configuration/
    └── Config.xcconfig        # 构建配置
```

## 🔧 配置文件对比

### iosApp (juejin-main)

| 配置项 | 值 |
|--------|-----|
| Framework | ComposeApp.framework |
| Bundle ID | com.example.juejin |
| 应用名称 | 掘金 APP |
| 编译任务 | :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64 |

### iosApp-lite (juejin-lite)

| 配置项 | 值 |
|--------|-----|
| Framework | JuejinLite.framework |
| Bundle ID | com.example.juejin.lite |
| 应用名称 | 掘金轻量版 |
| 编译任务 | :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64 |

## 🐛 常见问题

### Q1: Framework 找不到

**错误**: `Module 'JuejinLite' not found`

**解决方案**:
```bash
# 1. 重新编译 Framework
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

# 2. 在 Xcode 中清理
Product → Clean Build Folder (Cmd + Shift + K)

# 3. 检查 Framework 路径
ls -la apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/
```

### Q2: 应用崩溃

**解决方案**:
1. 查看 Xcode 控制台的错误信息
2. 确认 Framework 是 `Embed & Sign`
3. 检查 Bundle Identifier 是否正确

### Q3: 编译失败

**解决方案**:
```bash
# 清理所有缓存
./gradlew clean
rm -rf ~/.konan
rm -rf ~/Library/Developer/Xcode/DerivedData

# 重新编译
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

### Q4: 模拟器启动失败

**解决方案**:
```bash
# 清理模拟器
xcrun simctl delete unavailable
xcrun simctl shutdown all

# 重启 Simulator
killall Simulator
open -a Simulator
```

### Q5: 真机运行失败

**解决方案**:
```bash
# 1. 编译真机 Framework
./gradlew :apps:juejin-lite:linkDebugFrameworkIosArm64

# 2. 在 Xcode 中配置签名
# Signing & Capabilities → Team → 选择你的团队

# 3. 连接设备并运行
```

## 📊 性能对比

| 操作 | juejin-main | juejin-lite |
|------|-------------|-------------|
| Framework 编译 | 17秒 | 6秒 |
| Xcode 构建 | 10秒 | 8秒 |
| 应用大小 | ~20MB | ~15MB |
| 启动速度 | ~1秒 | ~0.5秒 |

## 🎯 验证清单

配置完成后，检查以下项目：

- [ ] Framework 编译成功
- [ ] iosApp-lite 项目已创建
- [ ] ContentView.swift 已更新
- [ ] Framework 已添加到 Xcode
- [ ] Build Phase 脚本已配置
- [ ] Bundle Identifier 已设置
- [ ] 应用可以在模拟器中运行
- [ ] 应用显示"掘金轻量版"

## 🚀 快速命令

```bash
# 配置项目
./setup-ios-lite.sh

# 编译 Framework
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

# 打开 Xcode
cd iosApp-lite && open iosApp.xcodeproj

# 运行应用
./run-ios.sh  # 选择 2
```

## 📚 相关文档

- [iOS_RUN_GUIDE.md](iOS_RUN_GUIDE.md) - iOS 运行指南
- [iOS_DEVELOPMENT_GUIDE.md](iOS_DEVELOPMENT_GUIDE.md) - iOS 开发指南
- [iOS_COMPILE_FIX.md](iOS_COMPILE_FIX.md) - 编译问题修复
- [README.md](README.md) - 项目主文档

## 🎉 完成

配置完成后，你可以：

- ✅ 在 iOS 模拟器上运行 juejin-lite
- ✅ 在 iOS 真机上运行 juejin-lite
- ✅ 使用 Xcode 调试应用
- ✅ 修改 Kotlin 代码并自动重新编译

---

**更新时间**: 2026-04-11  
**状态**: ✅ 配置指南完成
