# 🍎 iOS 应用运行指南

## 🎯 重要说明

KMP 项目的 iOS 部分分为两个步骤：
1. **编译 Framework**（Kotlin 代码 → Framework）
2. **运行 iOS 应用**（Framework → iOS 模拟器/真机）

## 🚀 快速开始

### 方法 1: 使用脚本（推荐）

```bash
./run-ios.sh
```

选择选项：
- `1` - 运行 juejin-main（完整版）
- `2` - 查看 juejin-lite 创建说明
- `3` - 只编译 Framework
- `4` - 清理并重新编译

### 方法 2: 使用 Xcode（推荐用于调试）

```bash
# 1. 编译 Framework
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64

# 2. 打开 Xcode 项目
cd iosApp
open iosApp.xcodeproj

# 3. 在 Xcode 中
#    - 选择模拟器（顶部工具栏）
#    - 点击运行按钮 (▶️) 或按 Cmd + R
```

### 方法 3: 使用命令行

```bash
# 1. 编译 Framework
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64

# 2. 构建并运行
cd iosApp
xcodebuild -project iosApp.xcodeproj \
           -scheme iosApp \
           -configuration Debug \
           -destination 'platform=iOS Simulator,name=iPhone 15 Pro' \
           build

# 3. 安装到模拟器
# （需要手动操作或使用脚本）
```

## 📱 当前 iOS 项目状态

### juejin-main（完整版）✅

**状态**: 已配置，可以运行

**项目位置**: `iosApp/`

**Framework**: `ComposeApp.framework`

**运行方式**:
```bash
# 方式 1: 使用脚本
./run-ios.sh
# 选择 1

# 方式 2: 使用 Xcode
cd iosApp && open iosApp.xcodeproj
```

### juejin-lite（轻量版）⚠️

**状态**: Framework 可编译，但 iOS 项目未创建

**Framework 位置**: 
```
apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/JuejinLite.framework
```

**需要创建 iOS 项目**（见下文）

## 🔧 完整运行流程

### 步骤 1: 编译 Framework

```bash
# juejin-main
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64

# juejin-lite
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

**编译时间**: 约 15 秒

**输出位置**:
```
apps/juejin-main/build/bin/iosSimulatorArm64/debugFramework/ComposeApp.framework
apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/JuejinLite.framework
```

### 步骤 2: 打开 Xcode 项目

```bash
cd iosApp
open iosApp.xcodeproj
```

### 步骤 3: 选择模拟器

在 Xcode 顶部工具栏：
```
iosApp > iPhone 15 Pro (或其他模拟器)
```

### 步骤 4: 运行应用

点击运行按钮 (▶️) 或按 `Cmd + R`

## 🎨 创建 juejin-lite 的 iOS 项目

### 方法 1: 复制并修改现有项目

```bash
# 1. 复制 iosApp 目录
cp -r iosApp iosApp-lite

# 2. 重命名项目文件
cd iosApp-lite
mv iosApp.xcodeproj iosApp-lite.xcodeproj

# 3. 在 Xcode 中打开并修改
open iosApp-lite.xcodeproj

# 4. 修改以下内容:
#    - Project Name: iosApp-lite
#    - Bundle Identifier: com.example.juejin.lite
#    - Framework: 替换为 JuejinLite.framework
```

### 方法 2: 创建新项目

#### 步骤 1: 在 Xcode 中创建新项目

1. 打开 Xcode
2. `File` → `New` → `Project`
3. 选择 `iOS` → `App`
4. 填写信息：
   - Product Name: `iosApp-lite`
   - Organization Identifier: `com.example.juejin`
   - Interface: `SwiftUI`
   - Language: `Swift`
5. 保存到项目根目录

#### 步骤 2: 编译 Framework

```bash
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

#### 步骤 3: 添加 Framework 到项目

1. 在 Xcode 中，选择项目 → Target → General
2. 找到 `Frameworks, Libraries, and Embedded Content`
3. 点击 `+` 按钮
4. 点击 `Add Other...` → `Add Files...`
5. 导航到：
   ```
   apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/JuejinLite.framework
   ```
6. 选择 `Embed & Sign`

#### 步骤 4: 配置 Build Phase

1. 选择 Target → Build Phases
2. 点击 `+` → `New Run Script Phase`
3. 添加脚本：
   ```bash
   cd "$SRCROOT/.."
   ./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
   ```

#### 步骤 5: 创建 SwiftUI 视图

创建 `ContentView.swift`:

```swift
import SwiftUI
import JuejinLite

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}
```

#### 步骤 6: 运行应用

点击运行按钮 (▶️) 或按 `Cmd + R`

## 🔍 调试技巧

### 查看日志

在 Xcode 中：
```
View → Debug Area → Activate Console (Cmd + Shift + Y)
```

### 设置断点

1. 在 Swift 代码中点击行号左侧设置断点
2. 运行应用（Debug 模式）
3. 当执行到断点时会暂停

### 查看 Kotlin 日志

Kotlin 代码中的 `Logger` 输出会显示在 Xcode 控制台：

```kotlin
// Kotlin 代码
Logger.d { "iOS Debug message" }
```

在 Xcode 控制台中会看到：
```
[Kermit] iOS Debug message
```

## 🐛 常见问题

### Q1: Framework 找不到
**错误**: `Module 'ComposeApp' not found`

**解决方案**:
```bash
# 1. 重新编译 Framework
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64

# 2. 在 Xcode 中清理
Product → Clean Build Folder (Cmd + Shift + K)

# 3. 重新构建
Product → Build (Cmd + B)
```

### Q2: 模拟器启动失败
**解决方案**:
```bash
# 清理模拟器
xcrun simctl delete unavailable
xcrun simctl shutdown all

# 重启 Simulator
killall Simulator
open -a Simulator
```

### Q3: 应用崩溃
**解决方案**:
1. 查看 Xcode 控制台的错误信息
2. 检查 Framework 是否正确嵌入
3. 确认 Framework 架构匹配（模拟器 vs 真机）

### Q4: Framework 版本不匹配
**解决方案**:
```bash
# 清理所有缓存
./gradlew clean
rm -rf ~/.konan
rm -rf ~/Library/Developer/Xcode/DerivedData

# 重新编译
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64
```

### Q5: 真机运行失败
**解决方案**:
```bash
# 编译真机 Framework
./gradlew :apps:juejin-main:linkDebugFrameworkIosArm64

# 在 Xcode 中:
# 1. 连接 iPhone
# 2. 选择你的设备
# 3. 配置签名（Signing & Capabilities）
# 4. 运行
```

## 📊 性能对比

| 操作 | 时间 | 说明 |
|------|------|------|
| 编译 Framework | 15秒 | 首次编译或清理后 |
| 增量编译 | 5秒 | 只修改了部分代码 |
| Xcode 构建 | 10秒 | 首次构建 |
| 增量构建 | 3秒 | 只修改了 Swift 代码 |
| 启动模拟器 | 5秒 | 如果未运行 |
| 安装应用 | 2秒 | - |
| **总计（首次）** | **32秒** | Framework + Xcode + 启动 |
| **总计（增量）** | **10秒** | 只修改代码 |

## 🎯 推荐工作流

### 日常开发

```
1. 在 Android Studio 中开发共享代码
   ├─ Desktop 快速测试（2秒）
   └─ Android 验证（8秒）

2. 定期在 iOS 上测试
   ├─ 编译 Framework（15秒）
   └─ Xcode 运行（10秒）

3. 只在 Xcode 中开发 iOS 特定代码
```

### 时间分配

```
Android Studio: 85%
├─ Desktop 开发: 50%
├─ Android 测试: 30%
└─ 编译 iOS Framework: 5%

Xcode: 15%
├─ iOS 特定功能: 8%
├─ UI 调整: 4%
└─ 测试验证: 3%
```

## 🔗 相关命令

### Framework 编译
```bash
# 模拟器
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

# 真机
./gradlew :apps:juejin-main:linkDebugFrameworkIosArm64
./gradlew :apps:juejin-lite:linkDebugFrameworkIosArm64

# Release
./gradlew :apps:juejin-main:linkReleaseFrameworkIosArm64
```

### 模拟器管理
```bash
# 列出模拟器
xcrun simctl list devices

# 启动模拟器
open -a Simulator

# 清理模拟器
xcrun simctl delete unavailable
```

### Xcode 命令
```bash
# 打开项目
open iosApp/iosApp.xcodeproj

# 命令行构建
xcodebuild -project iosApp/iosApp.xcodeproj \
           -scheme iosApp \
           -configuration Debug \
           -destination 'platform=iOS Simulator,name=iPhone 15 Pro' \
           build
```

## 📚 相关文档

- [iOS_DEVELOPMENT_GUIDE.md](iOS_DEVELOPMENT_GUIDE.md) - iOS 开发完整指南
- [iOS_QUICK_REFERENCE.md](iOS_QUICK_REFERENCE.md) - 快速命令参考
- [README.md](README.md) - 项目主文档

## 🎉 总结

iOS 应用运行需要两个步骤：
1. ✅ 编译 Framework（Gradle）
2. ✅ 运行应用（Xcode）

**最简单的方式**:
```bash
./run-ios.sh
```

**最灵活的方式**:
```bash
cd iosApp && open iosApp.xcodeproj
```

---

**更新时间**: 2026-04-11  
**状态**: ✅ iOS 运行指南完成
