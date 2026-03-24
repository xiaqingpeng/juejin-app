# 多平台构建指南

本项目支持以下平台的构建和发布：

## 支持的平台

### 1. Android
- **脚本**: `android-build.sh`
- **产物**: APK (Debug/Release), AAB
- **要求**: Android SDK
- **使用**:
  ```bash
  ./scripts/release/android-build.sh --version v1.0.0 --type all
  ```

### 2. iOS
- **脚本**: `ios-build.sh`
- **产物**: IPA, Framework
- **要求**: macOS, Xcode, 开发者证书
- **使用**:
  ```bash
  ./scripts/release/ios-build.sh --version v1.0.0 --type release
  ```
- **注意**: 需要配置签名和 Team ID

### 3. Desktop (macOS)
- **脚本**: `desktop-build.sh`
- **产物**: DMG
- **要求**: macOS
- **使用**:
  ```bash
  ./scripts/release/desktop-build.sh --version v1.0.0 --platform current
  ```

### 4. Linux
- **脚本**: `linux-build.sh`
- **产物**: DEB
- **要求**: Linux 或 macOS (交叉编译)
- **使用**:
  ```bash
  ./scripts/release/linux-build.sh --version v1.0.0
  ```

### 5. Windows
- **脚本**: `windows-build.sh`
- **产物**: MSI
- **要求**: Windows 或 macOS (交叉编译)
- **使用**:
  ```bash
  ./scripts/release/windows-build.sh --version v1.0.0
  ```

### 6. Web (Wasm)
- **脚本**: `web-build.sh`
- **产物**: ZIP (包含 HTML, JS, WASM)
- **要求**: 需要在 `build.gradle.kts` 中配置 `wasmJs` 目标
- **使用**:
  ```bash
  ./scripts/release/web-build.sh --version v1.0.0
  ```

## 全平台构建

### 方式 1: 使用全平台构建脚本
```bash
# 构建所有平台
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms all

# 构建指定平台
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms android,ios,desktop

# 构建并上传
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms all --upload
```

### 方式 2: 使用菜单式发布
```bash
./scripts/release/complete-release.sh
# 选择 7a - 全平台构建
```

## 配置 Web 平台

如果需要支持 Web 平台，需要在 `composeApp/build.gradle.kts` 中添加：

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

## 配置 iOS 签名

在 `scripts/release/ios-build.sh` 中修改 `ExportOptions.plist`：

```xml
<key>teamID</key>
<string>YOUR_TEAM_ID</string>
```

将 `YOUR_TEAM_ID` 替换为你的 Apple Developer Team ID。

## 构建产物位置

所有构建产物保存在：
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

## 自动上传

所有脚本都支持 `--upload` 参数，自动上传到 GitHub Release：

```bash
./scripts/release/android-build.sh --version v1.0.0 --type all --upload
./scripts/release/ios-build.sh --version v1.0.0 --upload
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms all --upload
```

## 常见问题

### Q: iOS 构建失败
A: 检查以下几点：
1. 是否在 macOS 上运行
2. 是否安装了 Xcode
3. 是否配置了开发者证书
4. 是否设置了正确的 Team ID

### Q: Web 构建失败
A: 需要在 `build.gradle.kts` 中配置 `wasmJs` 目标，参考上面的配置示例。

### Q: Linux/Windows 构建失败
A: 在 macOS 上构建 Linux/Windows 需要交叉编译支持，可能需要额外配置。

### Q: 如何只构建某些平台？
A: 使用 `all-platforms-build.sh` 的 `--platforms` 参数：
```bash
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms android,desktop
```

## 推荐工作流

### 开发测试
```bash
# 快速构建 Android Debug
./scripts/release/android-build.sh --version v1.0.0-dev --type debug
```

### 正式发布
```bash
# 1. 构建所有平台
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms all

# 2. 测试构建产物

# 3. 上传到 GitHub Release
./scripts/release/all-platforms-build.sh --version v1.0.0 --platforms all --upload
```

### 热修复发布
```bash
# 只构建需要的平台
./scripts/release/all-platforms-build.sh --version v1.0.1 --platforms android,ios --upload
```

## 脚本列表

| 脚本 | 功能 | 平台 |
|------|------|------|
| `android-build.sh` | Android 构建 | Android |
| `ios-build.sh` | iOS 构建 | iOS |
| `desktop-build.sh` | Desktop 构建 | macOS |
| `linux-build.sh` | Linux 构建 | Linux |
| `windows-build.sh` | Windows 构建 | Windows |
| `web-build.sh` | Web 构建 | Web/Wasm |
| `all-platforms-build.sh` | 全平台构建 | 所有平台 |
| `cross-platform-build.sh` | 跨平台构建 | Android + Desktop |
| `complete-release.sh` | 菜单式发布 | 所有平台 |

## 下一步

1. 根据需要配置各平台的构建环境
2. 测试各平台的构建脚本
3. 配置 iOS 签名（如需要）
4. 配置 Web 支持（如需要）
5. 开始使用自动化发布流程
