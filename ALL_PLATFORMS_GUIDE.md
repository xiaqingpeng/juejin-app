# 全平台构建指南

## 🎯 支持的平台

本项目现在支持 **6 个平台** 的构建和发布：

| 平台 | 产物格式 | 构建脚本 | 要求 |
|------|---------|---------|------|
| Android | APK, AAB | `android-build.sh` | Android SDK |
| iOS | IPA, Framework | `ios-build.sh` | macOS, Xcode |
| Desktop (macOS) | DMG | `desktop-build.sh` | macOS |
| Linux | DEB | `linux-build.sh` | Linux/macOS |
| Windows | MSI | `windows-build.sh` | Windows/macOS |
| Web | ZIP (Wasm) | `web-build.sh` | wasmJs 配置 |

## 🚀 快速开始

### 方式 1: 菜单式发布（推荐）

```bash
./scripts/release/complete-release.sh
```

选择对应的选项：
- **选项 9**: Android 专用构建
- **选项 10**: Desktop 专用构建
- **选项 11**: iOS 专用构建
- **选项 12**: Linux 专用构建
- **选项 13**: Windows 专用构建
- **选项 14**: Web 专用构建
- **选项 7a**: 全平台构建

### 方式 2: 全平台一键构建

```bash
# 构建所有平台
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms all

# 构建指定平台
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms android,ios,desktop

# 构建并自动上传
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms all --upload
```

### 方式 3: 单平台构建

#### Android
```bash
./scripts/release/android-build.sh --version v1.0.0 --type all
# 产物: APK (Debug/Release), AAB
```

#### iOS
```bash
./scripts/release/ios-build.sh --version v1.0.0 --type release
# 产物: IPA, Framework
# 注意: 需要配置签名
```

#### Desktop (macOS)
```bash
./scripts/release/desktop-build.sh --version v1.0.0 --platform current
# 产物: DMG
```

#### Linux
```bash
./scripts/release/linux-build.sh --version v1.0.0
# 产物: DEB
```

#### Windows
```bash
./scripts/release/windows-build.sh --version v1.0.0
# 产物: MSI
```

#### Web
```bash
./scripts/release/web-build.sh --version v1.0.0
# 产物: ZIP (包含 HTML, JS, WASM)
```

## 📦 构建产物位置

```
build/releases/
├── android/
│   ├── juejin-app-v1.0.0-android-debug.apk
│   ├── juejin-app-v1.0.0-android-release.apk
│   └── juejin-app-v1.0.0-android-release.aab
├── ios/
│   ├── juejin-app-v1.0.0-ios-release.ipa
│   └── juejin-app-v1.0.0-ios-framework.zip
├── desktop/
│   └── juejin-app-v1.0.0-macos.dmg
├── linux/
│   └── juejin-app-v1.0.0-linux.deb
├── windows/
│   └── juejin-app-v1.0.0-windows.msi
└── web/
    └── juejin-app-v1.0.0-web.zip
```

## ⚙️ 平台配置

### iOS 配置

1. **安装 Xcode**
   ```bash
   xcode-select --install
   ```

2. **配置签名**
   
   编辑 `scripts/release/ios-build.sh`，修改 Team ID：
   ```xml
   <key>teamID</key>
   <string>YOUR_TEAM_ID</string>
   ```

3. **查找 Team ID**
   - 打开 Xcode
   - Preferences → Accounts
   - 选择你的 Apple ID
   - 查看 Team ID

### Web 配置

在 `composeApp/build.gradle.kts` 中添加：

```kotlin
kotlin {
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        wasmJsMain.dependencies {
            implementation(compose.html.core)
        }
    }
}
```

## 🔄 自动上传

所有脚本都支持 `--upload` 参数：

```bash
# 单平台上传
./scripts/release/android-build.sh --version v1.0.0 --type all --upload

# 全平台上传
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms all --upload
```

## 📊 使用场景

### 场景 1: 开发测试
```bash
# 快速构建 Android Debug
./scripts/release/android-build.sh --version v1.0.0-dev --type debug
```

### 场景 2: 正式发布（所有平台）
```bash
# 1. 构建所有平台
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms all

# 2. 测试构建产物

# 3. 上传到 GitHub Release
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms all --upload
```

### 场景 3: 热修复（部分平台）
```bash
# 只构建和上传 Android 和 iOS
./scripts/release/all-platforms-build.sh --version v1.0.1 --platforms android,ios --upload
```

### 场景 4: Web 部署
```bash
# 1. 构建 Web
./scripts/release/web-build.sh --version v1.0.0

# 2. 解压到服务器
unzip build/releases/web/juejin-app-v1.0.0-web.zip -d /var/www/html/

# 3. 配置 Web 服务器支持 WASM MIME 类型
```

## ❓ 常见问题

### Q1: iOS 构建失败
**A**: 检查以下几点：
1. 是否在 macOS 上运行
2. 是否安装了 Xcode
3. 是否配置了开发者证书
4. 是否设置了正确的 Team ID

### Q2: Web 构建失败
**A**: 需要在 `build.gradle.kts` 中配置 `wasmJs` 目标，参考上面的配置示例。

### Q3: Linux/Windows 构建失败
**A**: 在 macOS 上构建 Linux/Windows 需要交叉编译支持，可能需要额外配置。

### Q4: 如何只构建某些平台？
**A**: 使用 `all-platforms-build.sh` 的 `--platforms` 参数：
```bash
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms android,desktop
```

### Q5: 构建产物在哪里？
**A**: 所有构建产物保存在 `build/releases/` 目录下，按平台分类。

## 📝 脚本参数说明

### android-build.sh
```bash
--version VERSION    # 版本号
--type TYPE          # debug|release|all
--upload             # 自动上传
```

### ios-build.sh
```bash
--version VERSION    # 版本号
--type TYPE          # debug|release
--upload             # 自动上传
```

### desktop-build.sh
```bash
--version VERSION    # 版本号
--platform PLATFORM  # current|all
--upload             # 自动上传
```

### linux-build.sh
```bash
--version VERSION    # 版本号
--upload             # 自动上传
```

### windows-build.sh
```bash
--version VERSION    # 版本号
--upload             # 自动上传
```

### web-build.sh
```bash
--version VERSION    # 版本号
--upload             # 自动上传
```

### all-platforms-build.sh
```bash
--version VERSION    # 版本号
--platforms LIST     # android,ios,desktop,linux,windows,web,all
--upload             # 自动上传
```

## 🎉 完整发布流程

### 步骤 1: 准备
```bash
# 验证环境
./scripts/release/verify-scripts.sh

# 确保 GitHub CLI 已认证
gh auth login
```

### 步骤 2: 构建
```bash
# 构建所有平台
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms all
```

### 步骤 3: 测试
```bash
# 测试各平台的构建产物
# Android: adb install build/releases/android/*.apk
# iOS: 使用 Xcode 安装
# Desktop: 打开 DMG 安装
# Linux: sudo dpkg -i build/releases/linux/*.deb
# Windows: 运行 MSI 安装
# Web: 解压并在浏览器中打开
```

### 步骤 4: 发布
```bash
# 上传到 GitHub Release
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms all --upload
```

## 📚 相关文档

- [RELEASE_GUIDE.md](RELEASE_GUIDE.md) - 详细发布指南
- [QUICK_START.md](QUICK_START.md) - 快速开始指南
- [PLATFORMS.md](scripts/release/PLATFORMS.md) - 平台配置详解
- [COMPLETED_WORK.md](COMPLETED_WORK.md) - 已完成工作总结

## 🔗 有用的链接

- [Kotlin Multiplatform 官方文档](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform 官方文档](https://www.jetbrains.com/lp/compose-multiplatform/)
- [GitHub CLI 文档](https://cli.github.com/manual/)

---

祝你发布顺利！🚀
