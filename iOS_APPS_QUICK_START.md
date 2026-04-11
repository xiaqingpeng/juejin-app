# iOS 应用快速启动指南

## 🎯 两个 iOS 应用

| 应用 | iOS 项目 | Framework | Bundle ID | 应用名称 |
|------|---------|-----------|-----------|---------|
| juejin-main | `iosApp/` | ComposeApp.framework | orgIdentifier.iosApp | 掘金 |
| juejin-lite | `iosApp-lite/` | JuejinLite.framework | orgIdentifier.iosApp-lite | 掘金轻量版 |

## ⚡ 快速启动

### 方法 1: 使用脚本（推荐）

```bash
./run-ios.sh
```

选择：
- `1` - juejin-main（完整版）
- `2` - juejin-lite（轻量版）

脚本会自动：
1. ✅ 编译 Framework
2. ✅ 查找并启动模拟器
3. ✅ 构建并运行应用

### 方法 2: 手动运行

#### juejin-main
```bash
# 1. 编译 Framework
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64

# 2. 在 Xcode 中运行
cd iosApp && open iosApp.xcodeproj
# 按 Cmd+R 运行
```

#### juejin-lite
```bash
# 1. 首次配置（只需一次）
./setup-ios-lite.sh

# 2. 在 Xcode 中配置 Framework（只需一次）
cd iosApp-lite && open iosApp.xcodeproj
# 按照提示配置 JuejinLite.framework

# 3. 后续运行
./run-ios.sh  # 选择 2
```

## 🔧 首次配置 juejin-lite

### 自动配置（推荐）

```bash
./setup-ios-lite.sh
```

### 手动配置

1. **编译 Framework**
```bash
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

2. **在 Xcode 中配置**
```bash
cd iosApp-lite && open iosApp.xcodeproj
```

在 Xcode 中：
- 选择项目 → Target → General
- 找到 "Frameworks, Libraries, and Embedded Content"
- 删除 `ComposeApp.framework`
- 点击 '+' → 'Add Other...' → 'Add Files...'
- 选择：`apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/JuejinLite.framework`
- 设置为 "Embed & Sign"

3. **配置自动重新编译（可选）**

在 Xcode 中：
- Target → Build Phases
- 点击 '+' → 'New Run Script Phase'
- 添加脚本：
```bash
cd "$SRCROOT/.."
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

## 📱 模拟器管理

### 查看可用模拟器
```bash
xcrun simctl list devices available
```

### 启动特定模拟器
```bash
# 获取 UUID
SIMULATOR_ID=$(xcrun simctl list devices available | grep "iPhone 17 Pro" | grep -oE '[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}' | head -1)

# 启动模拟器
xcrun simctl boot "$SIMULATOR_ID"
open -a Simulator
```

### 清理模拟器
```bash
# 删除不可用的模拟器
xcrun simctl delete unavailable

# 重置所有模拟器
xcrun simctl erase all

# 清理 DerivedData
rm -rf ~/Library/Developer/Xcode/DerivedData
```

## 🐛 常见问题

### Q: 模拟器 UUID 错误
```
xcodebuild: error: Unable to find a device matching { id:Shutdown }
```

**A**: ✅ 已修复！`run-ios.sh` 现在正确提取 UUID。

### Q: Gradle 任务找不到
```
Cannot locate tasks that match ':composeApp:embedAndSignAppleFrameworkForXcode'
```

**A**: ✅ 已修复！Xcode Build Phase 脚本已更新为正确的模块路径：
- juejin-main: `:apps:juejin-main:embedAndSignAppleFrameworkForXcode`
- juejin-lite: `:apps:juejin-lite:embedAndSignAppleFrameworkForXcode`

### Q: MainViewController 找不到
```swift
error: cannot find 'MainViewControllerKt' in scope
```

**A**: ✅ 已修复！已为 juejin-lite 创建 iOS 入口函数：
- 文件: `apps/juejin-lite/src/iosMain/kotlin/com/example/juejin/lite/MainViewController.kt`

### Q: Framework 找不到
```
ld: framework not found ComposeApp
```

**A**: 
1. 确保已编译 Framework
2. 在 Xcode 中检查 Framework 路径
3. 重新添加 Framework（删除后重新添加）

### Q: 编译失败（符号冲突）
```
IrPropertySymbolImpl is already bound
```

**A**: 清理并重新编译
```bash
./gradlew clean
rm -rf ~/.konan
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

### Q: 应用无法启动
**A**: 
1. 检查 Bundle ID 是否正确
2. 清理 DerivedData
3. 在 Xcode 中 Clean Build Folder (Cmd+Shift+K)
4. 重新运行

## 📊 编译时间对比

| 操作 | juejin-main | juejin-lite |
|------|-------------|-------------|
| Framework 编译 | ~15秒 | ~15秒 |
| Xcode 构建 | ~10秒 | ~8秒 |
| 总计 | ~25秒 | ~23秒 |

## 🎯 开发建议

### 日常开发
- 优先使用 Android（最快，7秒）
- 需要测试 iOS 特性时再用 iOS

### 跨平台测试
```bash
# 测试所有平台
./test-all-platforms.sh
```

### 同时安装两个应用
两个应用有不同的 Bundle ID，可以同时安装在模拟器上：
- 掘金（juejin-main）
- 掘金轻量版（juejin-lite）

## 📚 相关文档

- [iOS_RUN_GUIDE.md](iOS_RUN_GUIDE.md) - 详细运行指南
- [iOS_LITE_SETUP_GUIDE.md](iOS_LITE_SETUP_GUIDE.md) - juejin-lite 配置指南
- [iOS_DEVELOPMENT_GUIDE.md](iOS_DEVELOPMENT_GUIDE.md) - 完整开发指南
- [iOS_COMPILE_FIX.md](iOS_COMPILE_FIX.md) - 编译问题修复
- [SESSION_12_iOS_LITE_COMPLETE.md](SESSION_12_iOS_LITE_COMPLETE.md) - 本次会话总结

## 🚀 下一步

现在你可以：
1. ✅ 使用 `./run-ios.sh` 快速启动任一应用
2. ✅ 在 Xcode 中调试和开发
3. ✅ 同时安装两个应用进行对比测试
4. ✅ 根据需要切换开发平台

---

**更新时间**: 2026-04-11  
**状态**: ✅ 完成
