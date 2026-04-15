# 🌍 多平台快速参考

## 📱 支持的平台

| 应用 | Android | iOS | Desktop |
|------|---------|-----|---------|
| **juejin-main** | ✅ | ✅ | ✅ |
| **juejin-lite** | ✅ | ✅ | ✅ |

## 🚀 快速命令

### Android

```bash
# 编译并安装 juejin-main
./gradlew :apps:juejin-main:installDebug

# 编译并安装 juejin-lite
./gradlew :apps:juejin-lite:installDebug

# 同时安装两个应用
./gradlew installDebug
```

### iOS

```bash
# 编译 juejin-main Framework（模拟器）
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64

# 编译 juejin-lite Framework（模拟器）
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

# 编译真机版本
./gradlew :apps:juejin-main:linkDebugFrameworkIosArm64
./gradlew :apps:juejin-lite:linkDebugFrameworkIosArm64
```

### Desktop

```bash
# 运行 juejin-main
./gradlew :apps:juejin-main:runDesktop

# 运行 juejin-lite
./gradlew :apps:juejin-lite:runDesktop

# 使用启动脚本（推荐）
./run-desktop.sh
```

## 📦 打包发布

### Android APK

```bash
# Debug 版本
./gradlew :apps:juejin-lite:assembleDebug

# Release 版本
./gradlew :apps:juejin-lite:assembleRelease

# APK 位置
apps/juejin-lite/build/outputs/apk/debug/juejin-lite-debug.apk
apps/juejin-lite/build/outputs/apk/release/juejin-lite-release.apk
```

### iOS Framework

```bash
# 生成 Framework
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

# Framework 位置
apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/JuejinLite.framework
```

### Desktop 安装包

```bash
# 打包当前平台
./gradlew :apps:juejin-lite:packageDistributionForCurrentOS

# 生成的文件位置：
# macOS: apps/juejin-lite/build/compose/binaries/main/dmg/
# Windows: apps/juejin-lite/build/compose/binaries/main/msi/
# Linux: apps/juejin-lite/build/compose/binaries/main/deb/
```

## ⚡ 编译时间

| 平台 | juejin-main | juejin-lite |
|------|-------------|-------------|
| Android | ~38秒 | ~7秒 ⚡ |
| iOS | ~10分钟 | ~8分钟 |
| Desktop | ~15秒 | ~11秒 |

## 🎯 推荐工作流

### 日常开发
```bash
# 使用 Android 平台（最快）
./gradlew :apps:juejin-lite:assembleDebug --no-daemon
```

### 跨平台测试
```bash
# 1. Android
./gradlew :apps:juejin-lite:installDebug

# 2. Desktop
./gradlew :apps:juejin-lite:runDesktop

# 3. iOS（可选）
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

### 发布准备
```bash
# Android
./gradlew :apps:juejin-lite:assembleRelease

# Desktop
./gradlew :apps:juejin-lite:packageDistributionForCurrentOS

# iOS
# 在 Xcode 中构建
```

## 📁 输出文件位置

### Android
```
apps/juejin-lite/build/outputs/apk/
├── debug/
│   └── juejin-lite-debug.apk
└── release/
    └── juejin-lite-release.apk
```

### iOS
```
apps/juejin-lite/build/bin/
├── iosArm64/
│   └── debugFramework/
│       └── JuejinLite.framework
└── iosSimulatorArm64/
    └── debugFramework/
        └── JuejinLite.framework
```

### Desktop
```
apps/juejin-lite/build/compose/binaries/main/
├── app/
│   └── Juejin Lite/
├── dmg/
│   └── Juejin Lite-1.0.0.dmg
├── msi/
│   └── Juejin Lite-1.0.0.msi
└── deb/
    └── juejin-lite_1.0.0_amd64.deb
```

## 🔧 常用任务

### 清理构建
```bash
./gradlew clean
```

### 查看所有任务
```bash
./gradlew :apps:juejin-lite:tasks
```

### 查看依赖
```bash
./gradlew :apps:juejin-lite:dependencies
```

### 查看项目结构
```bash
./gradlew projects
```

## 🐛 故障排查

### Android 编译失败
```bash
./gradlew clean
./gradlew :apps:juejin-lite:assembleDebug --stacktrace
```

### iOS 编译失败
```bash
./gradlew clean
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64 --stacktrace
```

### Desktop 无法启动
```bash
# 检查 main 函数
cat apps/juejin-lite/src/desktopMain/kotlin/com/example/juejin/lite/main.kt

# 重新编译
./gradlew :apps:juejin-lite:desktopJar
./gradlew :apps:juejin-lite:runDesktop
```

## 📚 相关文档

- [MULTIPLATFORM_EXPANSION_SUMMARY.md](MULTIPLATFORM_EXPANSION_SUMMARY.md) - 详细总结
- [ALL_PLATFORMS_GUIDE.md](ALL_PLATFORMS_GUIDE.md) - 跨平台指南
- [PROJECT_STATUS.md](PROJECT_STATUS.md) - 项目状态

---

💡 **提示**：开发时使用 Android 平台最快，发布前测试所有平台！
