

# Kotlin Multiplatform 打包指南

## 🚀 快速开始

### 本地构建
```bash
# 克隆项目
git clone https://github.com/your-username/juejin-app.git
cd juejin-app

# 赋予脚本执行权限
chmod +x scripts/release/*.sh

# 运行完整发布流程
./scripts/release/complete-release.sh
```

### 自动化构建
- 推送标签到 GitHub 自动触发 CI/CD
- 手动触发构建：GitHub Actions → Run workflow

---

## 📱 Android

| 类型 | 命令 | 产物 |
|------|------|------|
| 开发测试 | `./gradlew :composeApp:assembleDebug` | APK |
| 正式发布 | `./gradlew :composeApp:assembleRelease` | APK |
| 上架专用 | `./gradlew :composeApp:bundleRelease` | AAB |

**脚本构建**：
```bash
# Android 专用构建
./scripts/release/android-build.sh --type debug --version v1.0.0

# 所有 Android 构建类型
./scripts/release/android-build.sh --type all --version v1.0.0 --upload
```

**产物路径**：`composeApp/build/outputs/apk/release/`

---

## 🍎 iOS

| 场景 | 方式 | 说明 |
|------|------|------|
| 模拟器运行 | Android Studio 直接运行 | 或使用 `iosDeployAppleSim` 命令 |
| 生成 Framework | `./gradlew :composeApp:embedAndSignAppleFrameworkForXcode` | 供 Xcode 调用 |
| 正式分发 | Xcode → Product → Archive → Distribute App | 导出 `.ipa` 文件 |

**环境要求**：macOS + 开发者证书

---

## 🌐 Web

| 类型 | 命令 | 产物 |
|------|------|------|
| 生产打包 | `./gradlew :composeApp:wasmJsBrowserDistribution` | HTML/JS/Wasm |
| 开发运行 | `./gradlew :composeApp:wasmJsBrowserRun` | 热重载支持 |

**产物路径**：`composeApp/build/dist/wasmJs/productionExecutable/`

---

## 💻 Desktop

| 平台 | 命令 | 产物 |
|------|------|------|
| Windows | `./gradlew packageExe` | EXE 安装包 |
| macOS | `./gradlew packageDmg` | DMG 安装包 |
| Linux | `./gradlew packageDeb` | DEB 安装包 |

**脚本构建**：
```bash
# 跨平台构建
./scripts/release/cross-platform-build.sh --version v1.0.0 --upload

# Desktop 专用构建
./scripts/release/desktop-build.sh --platforms current --version v1.0.0

# 所有桌面平台
./scripts/release/desktop-build.sh --platforms all --version v1.0.0
```

---

## 🔄 CI/CD 自动化

### GitHub Actions 工作流

项目包含完整的 GitHub Actions 工作流，支持：

#### 自动触发
- **标签推送**：推送 `v*` 标签自动触发全平台构建
- **手动触发**：在 Actions 页面手动选择平台构建

#### 支持的平台
- ✅ **Android**：Debug APK、Release APK、AAB
- ✅ **iOS**：Framework（需要 Xcode 打包）
- ✅ **Desktop**：Windows EXE、macOS DMG、Linux DEB
- ✅ **Web**：WASM 静态文件

#### 工作流文件
`.github/workflows/release.yml` - 完整的多平台构建和发布

### 本地脚本系统

#### 主要脚本

| 脚本 | 功能 | 使用场景 |
|------|------|----------|
| `complete-release.sh` | 完整发布流程菜单 | 正式发布 |
| `cross-platform-build.sh` | 跨平台构建 | 本地全平台构建 |
| `android-build.sh` | Android专用构建 | Android快速发布 |
| `desktop-build.sh` | Desktop专用构建 | Desktop快速发布 |
| `upload-release.sh` | 上传到GitHub Release | 手动上传 |
| `recreate-tag.sh` | 标签重建 | 触发CI/CD |

#### 使用示例

```bash
# 1. 完整发布流程（推荐）
./scripts/release/complete-release.sh

# 2. 快速Android发布
./scripts/release/android-build.sh --type release --version v1.0.0 --upload

# 3. 跨平台构建
./scripts/release/cross-platform-build.sh --version v1.0.0

# 4. 触发GitHub Actions
./scripts/release/recreate-tag.sh --tag v1.0.0

# 5. 上传文件到Release
./scripts/release/upload-release.sh juejin-app-v1.0.0-android-release.apk
```

---

## 📋 平台速查

| 平台 | 核心命令 | 产物 | 前置条件 |
|------|----------|------|----------|
| Android | `assembleRelease` | APK/AAB | 签名配置 |
| iOS | Xcode Archive | IPA | macOS + 证书 |
| Web | `wasmJsBrowserDistribution` | HTML/JS/Wasm | 无 |
| Desktop | `packageExe` / `packageDmg` | EXE/DMG | 本地 OS |

---

## 🔧 开发环境

### 系统要求
- **Java**: JDK 17+
- **Kotlin**: 1.9.0+
- **Gradle**: 8.0+
- **Android SDK**: API 21+ (Android 构建)
- **Xcode**: 14.0+ (iOS 构建)

### 环境配置

```bash
# Java 版本检查
java -version

# Gradle 版本检查
./gradlew --version

# Android SDK 配置
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

---

## 📝 日志查看

### Android
```bash
# ADB 日志
adb logcat -s "juejin-app"

# 构建日志
./gradlew assembleDebug --info
```

### Desktop
```bash
# JVM 运行日志
./gradlew :composeApp:jvmRun --stacktrace

# 构建日志
./gradlew packageDmg --info
```

### iOS
```bash
# Xcode 日志
# 在 Xcode 中查看 Console 输出
```

---

## 🚀 发布流程

### 自动发布（推荐）
1. **代码提交**：确保所有更改已提交
2. **创建标签**：`git tag v1.0.0`
3. **推送标签**：`git push origin v1.0.0`
4. **等待构建**：GitHub Actions 自动构建
5. **检查产物**：在 Release 页面下载测试

### 手动发布
1. **本地构建**：使用脚本构建所需平台
2. **创建Release**：在 GitHub 创建 Release
3. **上传产物**：使用上传脚本或手动上传

### 脚本发布
```bash
# 一键发布
./scripts/release/complete-release.sh

# 选择选项 7：跨平台构建和打包
# 选择选项 2：重建标签并推送（触发 CI/CD）
# 选择选项 3：检测流水线状态
```

---

## 🐛 故障排除

### 常见问题

#### Android 构建失败
```bash
# 清理构建
./gradlew clean

# 检查 Android SDK
echo $ANDROID_HOME

# 重新构建
./gradlew assembleDebug
```

#### Desktop 构建失败
```bash
# 检查 Java 版本
java -version

# 清理构建
./gradlew clean

# 重新构建
./gradlew packageDmg
```

#### GitHub Actions 失败
1. 检查工作流配置
2. 查看错误日志
3. 检查 secrets 配置
4. 重新触发工作流

### 获取帮助
- **项目 Issues**：https://github.com/your-username/juejin-app/issues
- **GitHub Actions**：https://github.com/your-username/juejin-app/actions
- **构建日志**：查看 Actions 中的详细日志

---

## 📄 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。


