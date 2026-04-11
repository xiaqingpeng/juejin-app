# 🚀 Juejin KMP Monorepo

一个基于 Kotlin Multiplatform (KMP) 的掘金客户端项目，支持 Android、iOS 和 Desktop 平台。

## ✨ 特性

- 🎯 **多平台支持**: Android、iOS、Desktop (macOS/Windows/Linux)
- 📦 **Monorepo 架构**: 统一管理多个应用和共享模块
- 🔄 **90% 代码共享**: 业务逻辑、UI 组件完全共享
- ⚡ **快速编译**: juejin-lite 仅需 7 秒（Android Debug）
- 🎨 **Material Design 3**: 现代化的 UI 设计
- 🌙 **主题切换**: 支持亮色/暗色主题

## 📱 应用

### juejin-main（完整版）
完整功能的掘金客户端，包含所有功能模块。

- ✅ 首页、热门、沸点、课程、我的
- ✅ 文章详情、评论、点赞
- ✅ 用户资料、关注、粉丝
- ✅ 搜索、分类、标签
- ✅ 主题切换、隐私设置

### juejin-lite（轻量版）
精简版掘金客户端，专注核心功能。

- ✅ 首页、热门、我的
- ✅ 文章详情、评论
- ✅ 主题切换
- ⚡ 编译速度快 5 倍
- 📦 体积更小

## 🚀 快速开始

### 前置要求

- JDK 11+
- Android Studio Ladybug+
- Xcode 15+ (iOS 开发)
- Gradle 8.14+

### 克隆项目

```bash
git clone <repository-url>
cd juejin-app
```

### 配置 Android Studio

```bash
# 运行配置脚本
./setup-android-studio.sh

# 重启 Android Studio
# 在顶部工具栏选择运行配置并运行
```

### 编译运行

#### Android
```bash
# juejin-lite（推荐，最快）
./gradlew :apps:juejin-lite:assembleDebug
./gradlew :apps:juejin-lite:installDebug

# juejin-main
./gradlew :apps:juejin-main:assembleDebug
./gradlew :apps:juejin-main:installDebug
```

#### Desktop
```bash
# 使用脚本（推荐）
./run-desktop.sh

# 或直接运行
./gradlew :apps:juejin-lite:run
./gradlew :apps:juejin-main:run
```

#### iOS
```bash
# 方法 1: 使用脚本（推荐）
./run-ios.sh
# 选择 1) juejin-main 或 2) juejin-lite

# 方法 2: 使用 Xcode
# juejin-main
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64
cd iosApp && open iosApp.xcodeproj

# juejin-lite（首次需要配置 Framework）
./setup-ios-lite.sh  # 首次运行
cd iosApp-lite && open iosApp.xcodeproj

# 3. 在 Xcode 中点击运行 (▶️) 或按 Cmd + R
```

**注意**: iOS 需要两个步骤：
1. 编译 Framework（Gradle）
2. 运行应用（Xcode）

详见 [iOS_RUN_GUIDE.md](iOS_RUN_GUIDE.md) 和 [iOS_LITE_SETUP_GUIDE.md](iOS_LITE_SETUP_GUIDE.md)

### 一键脚本

```bash
./test-all-platforms.sh    # 测试所有平台编译
./run-desktop.sh           # 运行 Desktop 应用
./run-ios.sh               # 运行 iOS 应用
./install-all.sh           # 安装 Android 应用
```

## 📊 项目结构

```
juejin-app/
├── apps/                          # 应用层
│   ├── juejin-main/              # 完整版应用
│   └── juejin-lite/              # 轻量版应用
│
├── shared/                        # 共享模块
│   ├── core/
│   │   ├── common/               # 通用工具
│   │   ├── storage/              # 数据存储
│   │   └── network/              # 网络请求
│   ├── domain/                   # 业务逻辑
│   └── ui/
│       ├── theme/                # 主题样式
│       └── components/           # UI 组件
│
├── gradle/                        # Gradle 配置
├── *.sh                          # 工具脚本
└── *.md                          # 文档
```

## 📚 文档

### 快速参考
- [QUICK_COMMANDS.md](QUICK_COMMANDS.md) - 常用命令速查
- [PLATFORM_STATUS.md](PLATFORM_STATUS.md) - 平台支持状态

### 完整指南
- [ALL_PLATFORMS_COMPLETE.md](ALL_PLATFORMS_COMPLETE.md) - 多平台完整指南
- [KMP_MONOREPO_GUIDE.md](KMP_MONOREPO_GUIDE.md) - Monorepo 架构指南
- [DEVICE_INSTALLATION_SUMMARY.md](DEVICE_INSTALLATION_SUMMARY.md) - Android 安装指南

### Android Studio 配置
- [ANDROID_STUDIO_MULTIPLATFORM.md](ANDROID_STUDIO_MULTIPLATFORM.md) - 多平台开发指南
- [ANDROID_STUDIO_SETUP_VISUAL.md](ANDROID_STUDIO_SETUP_VISUAL.md) - 可视化配置指南
- [ANDROID_STUDIO_RUN_CONFIG.md](ANDROID_STUDIO_RUN_CONFIG.md) - 运行配置详解
- [MODULE_NOT_FOUND_FIX.md](MODULE_NOT_FOUND_FIX.md) - 常见问题修复

### iOS 开发
- [iOS_APPS_QUICK_START.md](iOS_APPS_QUICK_START.md) - iOS 应用快速启动（推荐）
- [iOS_RUN_GUIDE.md](iOS_RUN_GUIDE.md) - iOS 应用运行指南
- [iOS_LITE_SETUP_GUIDE.md](iOS_LITE_SETUP_GUIDE.md) - juejin-lite iOS 配置指南
- [iOS_DEVELOPMENT_GUIDE.md](iOS_DEVELOPMENT_GUIDE.md) - iOS 开发完整指南
- [iOS_COMPILE_FIX.md](iOS_COMPILE_FIX.md) - 编译问题修复
- [iOS_QUICK_REFERENCE.md](iOS_QUICK_REFERENCE.md) - 快速命令参考

### 总结文档
- [MULTIPLATFORM_FINAL_SUMMARY.md](MULTIPLATFORM_FINAL_SUMMARY.md) - 多平台扩展总结
- [SESSION_6_COMPLETE.md](SESSION_6_COMPLETE.md) - 最新会话总结

## 🎯 开发建议

### 日常开发（最快）
```bash
# 使用 Android Debug（7 秒）
./gradlew :apps:juejin-lite:assembleDebug --no-daemon
```

### 跨平台测试
```bash
# 测试所有平台
./test-all-platforms.sh
```

### 发布准备
```bash
# Android Release
./gradlew :apps:juejin-lite:assembleRelease

# Desktop 打包
./gradlew :apps:juejin-lite:packageDistributionForCurrentOS

# iOS Framework
./gradlew :apps:juejin-lite:linkDebugFrameworkIosArm64
```

## 📊 性能指标

### 编译时间
| 应用 | Android Debug | Android Release | iOS | Desktop |
|------|--------------|----------------|-----|---------|
| juejin-main | 38秒 | 40秒 | 15秒 | 15秒 |
| juejin-lite | 7秒 | 36秒 | 15秒 | 20秒 |

### 应用大小（估算）
| 应用 | Android | iOS | Desktop |
|------|---------|-----|---------|
| juejin-main | ~15MB | ~20MB | ~30MB |
| juejin-lite | ~12MB | ~15MB | ~25MB |

## 🛠️ 技术栈

### 核心框架
- Kotlin Multiplatform
- Compose Multiplatform
- Kotlin Coroutines

### 网络
- Ktor Client
- Kotlinx Serialization

### UI
- Material Design 3
- Compose Material Icons
- Coil (图片加载)

### 存储
- DataStore (Android)
- UserDefaults (iOS)
- Preferences (Desktop)

### 日志
- Kermit

## 🎨 平台特性

| 特性 | Android | iOS | Desktop |
|------|---------|-----|---------|
| Material 3 | ✅ | ✅ | ✅ |
| 系统主题 | ✅ | ✅ | ✅ |
| 网络请求 | ✅ | ✅ | ✅ |
| 本地存储 | ✅ | ✅ | ✅ |
| 图片加载 | ✅ | ✅ | ✅ |
| 启动屏幕 | ✅ | ✅ | ❌ |
| 通知 | ✅ | ✅ | ⚠️ |
| 相机扫码 | ✅ | ⚠️ | ❌ |
| 窗口管理 | ❌ | ❌ | ✅ |

## 🐛 常见问题

### Q: R8 混淆失败
**A**: 检查 `proguard-rules.pro` 是否包含所有必要的规则

### Q: Desktop 任务找不到
**A**: 使用 `./gradlew :apps:juejin-lite:run` 而不是 `runDesktop`

### Q: iOS 编译时间过长
**A**: 开发时优先使用 Android 平台（最快）

### Q: 模块名称错误
**A**: 使用 `Juejin.apps.juejin-lite.main` 而不是 `juejin-app.apps.juejin-lite.main`

### Q: iOS 模拟器问题或需要清理
**A**: 使用以下命令清理模拟器：

```bash
# 方法 1: 删除不可用的模拟器
xcrun simctl delete unavailable

# 方法 2: 重置所有模拟器（会清除所有数据）
xcrun simctl erase all

# 方法 3: 在 Xcode 中清理
# Xcode → Window → Devices and Simulators → 右键模拟器 → Delete
```

### Q: iOS Framework 编译失败（符号冲突）
**A**: 如果看到 `IrPropertySymbolImpl is already bound` 错误：

```bash
# 1. 清理构建
./gradlew clean

# 2. 删除 Kotlin Native 缓存
rm -rf ~/.konan

# 3. 检查是否有重复的类定义
grep -r "data class TabItem" --include="*.kt"

# 4. 重新编译
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64
```

详见 [iOS_COMPILE_FIX.md](iOS_COMPILE_FIX.md)

更多问题请查看 [MODULE_NOT_FOUND_FIX.md](MODULE_NOT_FOUND_FIX.md)

## 📈 项目统计

- **应用数量**: 2 (juejin-main, juejin-lite)
- **支持平台**: 3 (Android, iOS, Desktop)
- **iOS 项目**: 2 (iosApp, iosApp-lite)
- **共享模块**: 6
- **可部署应用**: 6 (每个应用 × 3 平台)
- **代码共享率**: 90%

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

[MIT License](LICENSE)

## 🔗 相关链接

- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [掘金](https://juejin.cn/)

---

**更新时间**: 2026-04-11  
**状态**: ✅ 多平台配置完成  
**版本**: 1.0.0
