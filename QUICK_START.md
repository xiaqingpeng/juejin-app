# 快速开始指南

## 🚀 5分钟快速发布

### 1. 首次设置（只需一次）

```bash
# 认证 GitHub CLI
gh auth login

# 验证环境
./scripts/release/verify-scripts.sh
```

### 2. 快速构建和发布

```bash
# 方式 1: 使用菜单（推荐新手）
./scripts/release/complete-release.sh

# 方式 2: 一键构建上传（推荐）
./scripts/release/quick-build-upload.sh --version v1.0.0 --platform android

# 方式 3: 轻量级发布（避免 Actions 超时）
./scripts/release/create-lite-tag.sh --tag v1.0.0
```

## 📱 平台特定构建

### Android

```bash
# Debug 版本（快速测试）
./scripts/release/android-build.sh --version v1.0.0 --type debug

# Release 版本（正式发布）
./scripts/release/android-build.sh --version v1.0.0 --type release

# 所有版本
./scripts/release/android-build.sh --version v1.0.0 --type all
```

构建产物位置：`build/releases/android/`

### Desktop

```bash
# 当前平台
./scripts/release/desktop-build.sh --version v1.0.0 --platforms current

# 所有平台
./scripts/release/desktop-build.sh --version v1.0.0 --platforms all
```

构建产物位置：`build/releases/desktop/`

## 📤 上传到 GitHub Release

### 自动上传（构建时）

```bash
# Android
./scripts/release/android-build.sh --version v1.0.0 --type all --upload

# Desktop
./scripts/release/desktop-build.sh --version v1.0.0 --platforms current --upload
```

### 手动上传（已有构建文件）

```bash
# 单个文件
./scripts/release/upload-release.sh build/releases/android/juejin-app-v1.0.0-android-debug.apk --tag v1.0.0

# 批量上传
for file in build/releases/android/*; do
    ./scripts/release/upload-release.sh "$file" --tag v1.0.0
done
```

## 🔍 常用命令

### 测试构建

```bash
# 快速测试编译
./scripts/release/test-build.sh

# 测试 Android 构建
./scripts/release/android-build.sh --version v1.0.0-test --type debug
```

### 安装到设备

```bash
# Android
adb install -r build/releases/android/juejin-app-v1.0.0-android-debug.apk

# 查看日志
adb logcat -s "juejin-app"
```

### 检查构建产物

```bash
# 列出所有构建文件
ls -lh build/releases/android/
ls -lh build/releases/desktop/

# 查看文件大小
du -sh build/releases/android/*
```

## 🛠️ 故障排除

### 问题 1: 构建失败

```bash
# 清理并重试
./gradlew clean
./scripts/release/android-build.sh --version v1.0.0 --type debug
```

### 问题 2: 找不到 APK

检查构建输出目录：
```bash
find composeApp/build/outputs/apk -name "*.apk"
```

### 问题 3: 上传失败

```bash
# 检查 GitHub CLI 认证
gh auth status

# 重新认证
gh auth login

# 检查 Release 是否存在
gh release view v1.0.0
```

### 问题 4: 权限错误

```bash
# 给所有脚本添加执行权限
chmod +x scripts/release/*.sh
```

## 📋 发布检查清单

构建前：
- [ ] 更新版本号
- [ ] 提交所有代码更改
- [ ] 运行 `./scripts/release/verify-scripts.sh`

构建后：
- [ ] 检查构建产物大小
- [ ] 在真机上测试安装
- [ ] 验证应用功能正常

发布后：
- [ ] 验证 GitHub Release 页面
- [ ] 测试下载链接
- [ ] 更新文档和 CHANGELOG

## 🎯 推荐工作流

### 日常开发测试

```bash
# 快速构建 Debug 版本
./scripts/release/android-build.sh --version v1.0.0-dev --type debug

# 安装到设备
adb install -r build/releases/android/juejin-app-v1.0.0-dev-android-debug.apk
```

### 正式发布

```bash
# 1. 验证环境
./scripts/release/verify-scripts.sh

# 2. 构建所有版本
./scripts/release/quick-build-upload.sh --version v1.0.0 --platform all

# 3. 测试构建产物
# ... 在真机上测试 ...

# 4. 上传到 GitHub（如果步骤2没有自动上传）
# 脚本会提示是否上传
```

### 热修复发布

```bash
# 快速构建和发布
./scripts/release/create-lite-tag.sh --tag v1.0.1
```

## 📚 更多信息

- 详细发布指南：[RELEASE_GUIDE.md](RELEASE_GUIDE.md)
- iOS 状态栏测试：[iOS_STATUS_BAR_TEST.md](iOS_STATUS_BAR_TEST.md)
- 状态栏配置：[STATUS_BAR_SETUP.md](STATUS_BAR_SETUP.md)

## 💡 提示

1. **版本号规范**：使用语义化版本 `v主版本.次版本.修订号`
2. **构建类型**：
   - Debug: 用于开发测试，包含调试信息
   - Release: 用于正式发布，经过优化
3. **文件大小**：Release 版本通常比 Debug 版本小 30-50%
4. **测试建议**：在发布前至少在 2-3 台不同设备上测试

## 🆘 获取帮助

```bash
# 查看脚本帮助
./scripts/release/android-build.sh --help
./scripts/release/desktop-build.sh --help
./scripts/release/quick-build-upload.sh --help
./scripts/release/create-lite-tag.sh --help
```

## 🔗 相关链接

- GitHub Actions: https://github.com/xiaqingpeng/juejin-app/actions
- Releases: https://github.com/xiaqingpeng/juejin-app/releases
- Issues: https://github.com/xiaqingpeng/juejin-app/issues
