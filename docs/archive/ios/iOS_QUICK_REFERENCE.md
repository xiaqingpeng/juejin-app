# 🍎 iOS 快速参考

## ⚡ 常用命令

### Framework 编译

```bash
# 模拟器 (推荐用于开发)
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

# 真机
./gradlew :apps:juejin-lite:linkDebugFrameworkIosArm64

# Release 版本
./gradlew :apps:juejin-lite:linkReleaseFrameworkIosArm64
```

### 模拟器管理

```bash
# 查看所有模拟器
xcrun simctl list devices

# 删除不可用的模拟器（推荐定期运行）
xcrun simctl delete unavailable

# 重置所有模拟器（清除数据）
xcrun simctl erase all

# 关闭所有模拟器
xcrun simctl shutdown all
```

### 清理和维护

```bash
# 清理 Gradle 构建
./gradlew clean

# 删除 Kotlin Native 缓存
rm -rf ~/.konan

# 查看模拟器占用空间
du -sh ~/Library/Developer/CoreSimulator/Devices

# 完整清理流程
./gradlew clean && \
rm -rf ~/.konan && \
xcrun simctl delete unavailable
```

## 📁 Framework 位置

```
apps/juejin-lite/build/bin/
├── iosSimulatorArm64/debugFramework/JuejinLite.framework
└── iosArm64/debugFramework/JuejinLite.framework

apps/juejin-main/build/bin/
├── iosSimulatorArm64/debugFramework/ComposeApp.framework
└── iosArm64/debugFramework/ComposeApp.framework
```

## 🚀 快速工作流

### 开发流程
```bash
# 1. 在 Android Studio 中开发
# 2. 编译 iOS Framework
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

# 3. 在 Xcode 中运行
open -a Xcode

# 4. 定期清理（每周）
xcrun simctl delete unavailable
```

### 问题排查
```bash
# 如果编译失败
./gradlew clean
rm -rf ~/.konan
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

# 如果模拟器有问题
xcrun simctl delete unavailable
xcrun simctl shutdown all
killall Simulator
```

## 🎯 推荐设置

### 只保留需要的模拟器
```
✅ iPhone 15 Pro (iOS 17.0) - 主要开发
✅ iPhone SE (iOS 17.0) - 小屏测试
❌ 删除其他不常用的模拟器
```

### 定期维护
```
每周: xcrun simctl delete unavailable
每月: xcrun simctl erase all (如果空间不足)
```

## 📚 相关文档

- [iOS_DEVELOPMENT_GUIDE.md](iOS_DEVELOPMENT_GUIDE.md) - 完整指南
- [README.md](README.md) - 项目文档
- [QUICK_COMMANDS.md](QUICK_COMMANDS.md) - 所有平台命令

---

**提示**: 将此文件加入书签，方便快速查找命令！
