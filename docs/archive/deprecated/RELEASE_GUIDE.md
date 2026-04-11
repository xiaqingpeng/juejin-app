# 发布指南

## 快速开始

### 1. Android 快速构建和发布

```bash
# 构建 Android Debug APK
./scripts/release/android-build.sh --version v1.0.0 --type debug

# 构建所有 Android 版本（Debug APK + Release APK + AAB）
./scripts/release/android-build.sh --version v1.0.0 --type all

# 构建并上传
./scripts/release/android-build.sh --version v1.0.0 --type all --upload
```

### 2. Desktop 快速构建

```bash
# 构建当前平台
./scripts/release/desktop-build.sh --version v1.0.0 --platforms current

# 构建所有平台
./scripts/release/desktop-build.sh --version v1.0.0 --platforms all

# 构建并上传
./scripts/release/desktop-build.sh --version v1.0.0 --platforms current --upload
```

### 3. 一键构建和上传

```bash
# Android 平台
./scripts/release/quick-build-upload.sh --version v1.0.0 --platform android

# Desktop 平台
./scripts/release/quick-build-upload.sh --version v1.0.0 --platform desktop

# 所有平台
./scripts/release/quick-build-upload.sh --version v1.0.0 --platform all
```

### 4. 使用菜单式界面

```bash
./scripts/release/complete-release.sh
```

然后选择：
- 选项 9: Android 专用构建
- 选项 10: Desktop 专用构建
- 选项 8: 快速构建上传

## 构建产物位置

所有构建产物都会保存在：
- Android: `build/releases/android/`
- Desktop: `build/releases/desktop/`
- 通用: `build/releases/`

## 文件命名规则

- Android Debug: `juejin-app-v1.0.0-android-debug.apk`
- Android Release: `juejin-app-v1.0.0-android-release.apk`
- Android AAB: `juejin-app-v1.0.0-android-release.aab`
- macOS: `juejin-app-v1.0.0-macos.dmg`
- Linux: `juejin-app-v1.0.0-linux.deb`
- Windows: `juejin-app-v1.0.0-windows.exe`

## 上传到 GitHub Release

### 前提条件

1. 安装 GitHub CLI
```bash
# macOS
brew install gh

# 认证
gh auth login
```

2. 确保有正确的权限
- 需要对仓库有写入权限
- Token 需要 `repo` 权限

### 手动上传单个文件

```bash
./scripts/release/upload-release.sh <文件路径> --tag v1.0.0
```

示例：
```bash
./scripts/release/upload-release.sh build/releases/android/juejin-app-v1.0.0-android-debug.apk --tag v1.0.0
```

### 批量上传

```bash
# 上传 Android 构建目录下的所有文件
for file in build/releases/android/*; do
    ./scripts/release/upload-release.sh "$file" --tag v1.0.0
done
```

## 常见问题

### 1. 构建失败

**问题**: Android 构建失败
```bash
# 清理并重试
./gradlew clean
./scripts/release/android-build.sh --version v1.0.0 --type debug
```

**问题**: 找不到 APK 文件
- 检查 `composeApp/build/outputs/apk/` 目录
- 确保构建成功完成

### 2. 上传失败

**问题**: GitHub CLI 未认证
```bash
gh auth login
```

**问题**: Release 不存在
```bash
# 使用 --force 参数自动创建
./scripts/release/upload-release.sh <文件> --tag v1.0.0 --force
```

**问题**: 文件太大（>100MB）
- GitHub Release 限制单个文件 100MB
- 考虑使用其他存储服务（如 AWS S3）

### 3. 权限问题

**问题**: 脚本没有执行权限
```bash
chmod +x scripts/release/*.sh
```

## 测试构建

在正式发布前，建议先测试构建：

```bash
# 测试编译
./scripts/release/test-build.sh

# 测试 Android 构建
./scripts/release/android-build.sh --version v1.0.0-test --type debug

# 安装到设备测试
adb install -r build/releases/android/juejin-app-v1.0.0-test-android-debug.apk
```

## 版本号规范

建议使用语义化版本号：
- `v1.0.0` - 正式版本
- `v1.0.0-beta.1` - Beta 版本
- `v1.0.0-alpha.1` - Alpha 版本
- `v1.0.0-rc.1` - Release Candidate

## 发布检查清单

- [ ] 更新版本号
- [ ] 更新 CHANGELOG
- [ ] 测试所有平台构建
- [ ] 在真机上测试
- [ ] 检查应用签名（Release 版本）
- [ ] 准备 Release Notes
- [ ] 上传到 GitHub Release
- [ ] 通知用户更新

## 自动化发布（GitHub Actions）

如果配置了 GitHub Actions，可以通过推送标签触发自动构建：

```bash
# 创建并推送标签
git tag v1.0.0
git push origin v1.0.0

# 或使用脚本
./scripts/release/recreate-tag.sh --tag v1.0.0
```

然后监控构建状态：
```bash
./scripts/release/check-pipeline.sh --tag v1.0.0
```
