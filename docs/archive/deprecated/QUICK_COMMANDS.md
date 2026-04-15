# ⚡ 快速命令参考

## 🚀 一键脚本

```bash
./test-all-platforms.sh    # 测试所有平台编译
./run-desktop.sh           # 运行 Desktop 应用
./install-all.sh           # 安装 Android 应用
```

## 📱 Android

### juejin-main
```bash
# 编译
./gradlew :apps:juejin-main:assembleDebug
./gradlew :apps:juejin-main:assembleRelease

# 安装运行
./gradlew :apps:juejin-main:installDebug
adb shell am start -n com.example.juejin/.MainActivity
```

### juejin-lite
```bash
# 编译
./gradlew :apps:juejin-lite:assembleDebug
./gradlew :apps:juejin-lite:assembleRelease

# 安装运行
./gradlew :apps:juejin-lite:installDebug
adb shell am start -n com.example.juejin.lite/.MainActivity
```

## 🍎 iOS

### juejin-main
```bash
# 模拟器
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64

# 真机
./gradlew :apps:juejin-main:linkDebugFrameworkIosArm64
```

### juejin-lite
```bash
# 模拟器
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

# 真机
./gradlew :apps:juejin-lite:linkDebugFrameworkIosArm64
```

## 🖥️ Desktop

### juejin-main
```bash
# 运行
./gradlew :apps:juejin-main:run

# 编译 JAR
./gradlew :apps:juejin-main:jvmJar

# 打包发布
./gradlew :apps:juejin-main:packageDistributionForCurrentOS
```

### juejin-lite
```bash
# 运行
./gradlew :apps:juejin-lite:run

# 编译 JAR
./gradlew :apps:juejin-lite:jvmJar

# 打包发布
./gradlew :apps:juejin-lite:packageDistributionForCurrentOS
```

## 🔧 开发常用

```bash
# 最快编译（开发时使用）
./gradlew :apps:juejin-lite:assembleDebug --no-daemon

# 清理构建
./gradlew clean

# 查看所有任务
./gradlew :apps:juejin-lite:tasks

# 查看依赖
./gradlew :apps:juejin-lite:dependencies
```

## 📊 编译时间

| 命令 | 时间 |
|------|------|
| `juejin-lite:assembleDebug` | ~7秒 |
| `juejin-main:assembleDebug` | ~38秒 |
| `juejin-lite:jvmJar` | ~20秒 |
| `juejin-main:jvmJar` | ~15秒 |
| `juejin-lite:linkDebugFrameworkIosSimulatorArm64` | ~15秒 |

## 🎯 推荐工作流

### 日常开发
```bash
# 1. 使用 Android Debug（最快）
./gradlew :apps:juejin-lite:assembleDebug --no-daemon

# 2. 安装到设备
./gradlew :apps:juejin-lite:installDebug

# 3. 启动应用
adb shell am start -n com.example.juejin.lite/.MainActivity
```

### 跨平台测试
```bash
# 测试所有平台
./test-all-platforms.sh
```

### 发布准备
```bash
# Android
./gradlew :apps:juejin-lite:assembleRelease

# Desktop
./gradlew :apps:juejin-lite:packageDistributionForCurrentOS

# iOS
./gradlew :apps:juejin-lite:linkDebugFrameworkIosArm64
```

---

**提示**: 添加 `--no-daemon` 可以节省内存，但会稍微增加启动时间
