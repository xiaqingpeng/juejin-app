

# Kotlin Multiplatform 打包指南

## Android

| 类型 | 命令 | 产物 |
|------|------|------|
| 开发测试 | `./gradlew :composeApp:assembleDebug` | APK |
| 正式发布 | `./gradlew :composeApp:assembleRelease` | APK |
| 上架专用 | `./gradlew :composeApp:bundleRelease` | AAB |

**产物路径**：`composeApp/build/outputs/apk/release/`

---

## iOS

| 场景 | 方式 | 说明 |
|------|------|------|
| 模拟器运行 | Android Studio 直接运行 | 或使用 `iosDeployAppleSim` 命令 |
| 生成 Framework | `./gradlew :composeApp:embedAndSignAppleFrameworkForXcode` | 供 Xcode 调用 |
| 正式分发 | Xcode → Product → Archive → Distribute App | 导出 `.ipa` 文件 |

**环境要求**：macOS + 开发者证书

---

## Web

| 类型 | 命令 | 产物 |
|------|------|------|
| 生产打包 | `./gradlew :composeApp:wasmJsBrowserDistribution` | HTML/JS/Wasm |
| 开发运行 | `./gradlew :composeApp:wasmJsBrowserRun` | 热重载支持 |

**产物路径**：`composeApp/build/dist/wasmJs/productionExecutable/`

---

## Desktop

| 平台 | 命令 | 产物 |
|------|------|------|
| Windows | `./gradlew packageExe` | EXE 安装包 |
| macOS | `./gradlew packageDmg` | DMG 安装包 |
| Linux | `./gradlew packageDeb` | DEB 安装包 |

---

## 日志查看

```bash
adb logcat -s "CoursesScreen:D"
```

---

## 平台速查

| 平台 | 核心命令 | 产物 | 前置条件 |
|------|----------|------|----------|
| Android | `assembleRelease` | APK/AAB | 签名配置 |
| iOS | Xcode Archive | IPA | macOS + 证书 |
| Web | `wasmJsBrowserDistribution` | HTML/JS/Wasm | 无 |
| Desktop | `packageExe` / `packageDmg` | EXE/DMG | 本地 OS |