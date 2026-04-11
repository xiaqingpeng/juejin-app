# ⚡ 快速命令参考

## 🚀 快速运行

### Android
```bash
# juejin-lite（推荐，最快 7秒）
./gradlew :apps:juejin-lite:assembleDebug
./gradlew :apps:juejin-lite:installDebug

# juejin-main
./gradlew :apps:juejin-main:assembleDebug
./gradlew :apps:juejin-main:installDebug
```

### iOS
```bash
# 使用脚本（推荐）
./run-ios.sh

# 手动编译
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
cd iosApp-lite && open iosApp.xcodeproj
```

### Desktop
```bash
# 使用脚本（推荐）
./run-desktop.sh

# 手动运行
./gradlew :apps:juejin-lite:run
./gradlew :apps:juejin-main:run
```

## 🔧 配置命令

### Android Studio
```bash
# 配置运行配置
./setup-android-studio.sh
```

### iOS Lite
```bash
# 首次配置 juejin-lite iOS 项目
./setup-ios-lite.sh
```

## 🧪 测试命令

```bash
# 测试所有平台编译
./test-all-platforms.sh

# 安装所有 Android 应用
./install-all.sh
```

## 🧹 清理命令

```bash
# 清理 Gradle 构建
./gradlew clean

# 清理 iOS
rm -rf ~/Library/Developer/Xcode/DerivedData
rm -rf ~/.konan

# 清理模拟器
xcrun simctl delete unavailable
```

## 📊 常用 Gradle 任务

### 查看项目结构
```bash
./gradlew projects
```

### 查看任务列表
```bash
./gradlew tasks
./gradlew :apps:juejin-lite:tasks
```

### 编译特定平台
```bash
# Android
./gradlew :apps:juejin-lite:assembleDebug
./gradlew :apps:juejin-lite:assembleRelease

# iOS
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
./gradlew :apps:juejin-lite:linkReleaseFrameworkIosArm64

# Desktop
./gradlew :apps:juejin-lite:packageDistributionForCurrentOS
```

## 📱 模拟器管理

### iOS 模拟器
```bash
# 查看可用模拟器
xcrun simctl list devices available

# 启动模拟器
xcrun simctl boot <UUID>
open -a Simulator

# 清理模拟器
xcrun simctl erase all
```

### Android 模拟器
```bash
# 查看设备
adb devices

# 安装应用
adb install -r apps/juejin-lite/build/outputs/apk/debug/juejin-lite-debug.apk

# 卸载应用
adb uninstall com.example.juejin.lite
```

## 🔍 调试命令

### 查看日志
```bash
# Android
adb logcat | grep "Juejin"

# iOS (在 Xcode 中查看)
```

### 检查编译问题
```bash
# 详细输出
./gradlew :apps:juejin-lite:assembleDebug --info

# 调试输出
./gradlew :apps:juejin-lite:assembleDebug --debug

# 堆栈跟踪
./gradlew :apps:juejin-lite:assembleDebug --stacktrace
```

## 📚 更多信息

- [README.md](README.md) - 项目主文档
- [KMP_MONOREPO_GUIDE.md](KMP_MONOREPO_GUIDE.md) - Monorepo 架构指南
- [iOS_APPS_QUICK_START.md](iOS_APPS_QUICK_START.md) - iOS 快速启动
- [ANDROID_STUDIO_MULTIPLATFORM.md](ANDROID_STUDIO_MULTIPLATFORM.md) - Android Studio 配置

---

**更新时间**: 2026-04-11
