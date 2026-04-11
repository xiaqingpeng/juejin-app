# 版本号更新指南

本文档说明如何更新应用的版本号。

## 📋 版本号位置

### Android
**文件**: `composeApp/build.gradle.kts`

```kotlin
android {
    defaultConfig {
        versionCode = 1        // 版本代码（整数，每次发布递增）
        versionName = "1.0.0"  // 版本名称（显示给用户）
    }
}
```

### iOS
**文件**: `iosApp/iosApp/Info.plist`

```xml
<key>CFBundleShortVersionString</key>
<string>1.0.0</string>  <!-- 版本名称 -->
<key>CFBundleVersion</key>
<string>1</string>       <!-- 构建号 -->
```

### Desktop (macOS/Linux/Windows)
**文件**: `composeApp/build.gradle.kts`

```kotlin
compose.desktop {
    application {
        nativeDistributions {
            packageVersion = "1.0.0"  // 版本号
        }
    }
}
```

**文件**: `composeApp/src/jvmMain/kotlin/com/example/juejin/platform/AppVersion.jvm.kt`

```kotlin
actual fun getAppVersionInfo(): AppVersionInfo {
    val versionName = "1.0.0"  // 更新这里
    val buildNumber = "1"       // 更新这里
    // ...
}
```

## 🔄 更新步骤

### 方式 1: 手动更新（推荐）

1. **更新 Android 版本**
   ```kotlin
   // composeApp/build.gradle.kts
   versionCode = 2
   versionName = "1.0.1"
   ```

2. **更新 iOS 版本**
   ```xml
   <!-- iosApp/iosApp/Info.plist -->
   <key>CFBundleShortVersionString</key>
   <string>1.0.1</string>
   <key>CFBundleVersion</key>
   <string>2</string>
   ```

3. **更新 Desktop 版本**
   ```kotlin
   // composeApp/build.gradle.kts
   packageVersion = "1.0.1"
   
   // composeApp/src/jvmMain/kotlin/com/example/juejin/platform/AppVersion.jvm.kt
   val versionName = "1.0.1"
   val buildNumber = "2"
   ```

### 方式 2: 使用脚本更新（待实现）

可以创建一个脚本来自动更新所有平台的版本号：

```bash
./scripts/update-version.sh 1.0.1 2
```

## 📊 版本号规范

### 语义化版本（Semantic Versioning）

格式: `MAJOR.MINOR.PATCH`

- **MAJOR**: 主版本号，不兼容的 API 修改
- **MINOR**: 次版本号，向下兼容的功能性新增
- **PATCH**: 修订号，向下兼容的问题修正

示例:
- `1.0.0` - 首次发布
- `1.0.1` - 修复 bug
- `1.1.0` - 新增功能
- `2.0.0` - 重大更新

### 构建号（Build Number）

- Android: `versionCode` - 整数，每次发布递增
- iOS: `CFBundleVersion` - 字符串，通常是整数
- Desktop: `buildNumber` - 字符串

## 🎯 发布流程中的版本更新

### 1. 开发版本
```
版本: 1.0.0-dev
构建号: 1
```

### 2. 测试版本
```
版本: 1.0.0-beta.1
构建号: 2
```

### 3. 正式版本
```
版本: 1.0.0
构建号: 3
```

### 4. 热修复版本
```
版本: 1.0.1
构建号: 4
```

## 📝 版本显示

应用会在设置页面自动显示版本信息：

```
当前版本: v1.0.0 (Build-1)
```

版本信息来源：
- **Android**: 从 `build.gradle.kts` 的 `versionName` 和 `versionCode`
- **iOS**: 从 `Info.plist` 的 `CFBundleShortVersionString` 和 `CFBundleVersion`
- **Desktop**: 从 `AppVersion.jvm.kt` 的硬编码值

## 🔧 自动化版本管理（高级）

### 使用 Git 标签

```bash
# 创建版本标签
git tag -a v1.0.0 -m "Release version 1.0.0"

# 推送标签
git push origin v1.0.0
```

### 从 Git 标签读取版本

可以在构建脚本中自动从 Git 标签读取版本号：

```kotlin
// build.gradle.kts
val gitVersion = providers.exec {
    commandLine("git", "describe", "--tags", "--always")
}.standardOutput.asText.get().trim()
```

## ⚠️ 注意事项

1. **版本号一致性**: 确保所有平台使用相同的版本号
2. **构建号递增**: 每次发布都要递增构建号
3. **App Store 要求**: iOS 上传到 App Store 时，构建号必须唯一且递增
4. **Google Play 要求**: Android 上传到 Google Play 时，versionCode 必须递增

## 📚 相关文档

- [语义化版本规范](https://semver.org/lang/zh-CN/)
- [Android 版本管理](https://developer.android.com/studio/publish/versioning)
- [iOS 版本管理](https://developer.apple.com/documentation/bundleresources/information_property_list/cfbundleshortversionstring)

## 🚀 快速参考

### 发布新版本检查清单

- [ ] 更新 `composeApp/build.gradle.kts` 中的 Android 版本
- [ ] 更新 `iosApp/iosApp/Info.plist` 中的 iOS 版本
- [ ] 更新 `composeApp/build.gradle.kts` 中的 Desktop 版本
- [ ] 更新 `AppVersion.jvm.kt` 中的硬编码版本
- [ ] 测试所有平台的版本显示
- [ ] 提交版本更新
- [ ] 创建 Git 标签
- [ ] 构建和发布

---

最后更新: 2024-03-24
