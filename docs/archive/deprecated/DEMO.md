# 使用演示

## 🎬 完整演示流程

### 演示 1: 首次使用（新手推荐）

```bash
# 步骤 1: 验证环境
$ ./scripts/release/verify-scripts.sh

========================================
    发布脚本验证
========================================

=== 检查必需工具 ===
检查 Git... ✓
检查 Java... ✓
检查 Gradle Wrapper... ✓
检查 GitHub CLI... ✓

# 步骤 2: 认证 GitHub CLI（如果需要）
$ gh auth login
# 按照提示完成认证

# 步骤 3: 使用菜单式发布
$ ./scripts/release/complete-release.sh

========================================
    Kotlin Multiplatform 完整发布流程
========================================

请选择要执行的步骤:
  1. 本地构建测试
  2. 重建标签并推送
  3. 检测流水线状态
  4. 手动上传Release资产
  5. 完整流程 (1→2→3)
  6. 创建轻量级标签
  7. 跨平台构建和打包
  8. 快速构建上传
  9. Android 专用构建
  10. Desktop 专用构建
  0. 退出

请输入选择 [0-10]: 9

# 选择 Android 构建，按照提示操作
```

---

### 演示 2: Android 快速构建（熟练用户）

```bash
# 构建 Debug 版本
$ ./scripts/release/android-build.sh --version v1.0.0 --type debug

========================================
    Android 专用构建
========================================

构建配置:
  版本: v1.0.0
  类型: debug
  项目: juejin-app

检查 Android 环境...
✓ Android 环境检查通过

清理之前的构建...

=== 构建 Debug APK ===
正在构建 Debug APK...
✓ Debug APK 构建成功
✓ APK 已复制到: build/releases/android/juejin-app-v1.0.0-android-debug.apk
APK 大小: 25 MB

=== 构建结果 ===
生成的文件:
  ✓ juejin-app-v1.0.0-android-debug.apk (25 MB)

=== 设备安装 ===
发现 1 个设备
是否安装到设备? [y/N]: y
正在安装到设备...
✓ 安装成功

========================================
✓ Android 构建完成！
========================================
```

---

### 演示 3: 快速构建和上传

```bash
$ ./scripts/release/quick-build-upload.sh --version v1.0.0 --platform android

========================================
    快速构建和上传
========================================

构建配置:
  版本: v1.0.0
  平台: android

=== 构建 Android ===
# ... 构建过程 ...

=== 检查构建产物 ===
  ✓ juejin-app-v1.0.0-android-debug.apk (25 MB)
  ✓ juejin-app-v1.0.0-android-release.apk (18 MB)
  ✓ juejin-app-v1.0.0-android-release.aab (17 MB)

是否上传到 GitHub Release? [y/N]: y

=== 上传到 GitHub Release ===
上传: juejin-app-v1.0.0-android-debug.apk
✓ 上传成功

上传: juejin-app-v1.0.0-android-release.apk
✓ 上传成功

上传: juejin-app-v1.0.0-android-release.aab
✓ 上传成功

✓ 所有文件上传完成

========================================
✓ 操作完成！
========================================
```

---

### 演示 4: 轻量级发布（避免 Actions 超时）

```bash
$ ./scripts/release/create-lite-tag.sh --tag v1.0.0

========================================
    轻量级标签创建（本地构建方案）
========================================

配置信息:
  标签名称: v1.0.0
  项目名称: juejin-app

GitHub仓库: xiaqingpeng/juejin-app
✓ GitHub CLI 已就绪

=== 步骤 1/4: 本地构建 ===
是否执行本地构建? [Y/n]: y

选择构建平台:
  1. Android
  2. Desktop
  3. 两者都构建
请选择 [1-3]: 1

# ... Android 构建过程 ...

=== 步骤 2/4: 创建标签 ===
✓ 标签创建成功

=== 步骤 3/4: 创建 GitHub Release ===
✓ Release 创建成功

=== 步骤 4/4: 上传构建文件 ===
找到 3 个构建文件:
  - juejin-app-v1.0.0-android-debug.apk
  - juejin-app-v1.0.0-android-release.apk
  - juejin-app-v1.0.0-android-release.aab

是否上传这些文件? [Y/n]: y

上传: juejin-app-v1.0.0-android-debug.apk
✓ 上传成功

# ... 其他文件上传 ...

========================================
✓ 轻量级发布完成！
========================================

Release 信息:
  标签: v1.0.0
  仓库: xiaqingpeng/juejin-app
  链接: https://github.com/xiaqingpeng/juejin-app/releases/tag/v1.0.0
```

---

### 演示 5: Desktop 构建

```bash
$ ./scripts/release/desktop-build.sh --version v1.0.0 --platforms current

========================================
    Desktop 专用构建
========================================

构建配置:
  版本: v1.0.0
  平台: current
  项目: juejin-app

检测操作系统: Darwin
✓ 环境检查通过

清理之前的构建...

=== 构建当前平台 ===
构建 macOS DMG...
✓ macOS DMG 构建成功

=== 构建结果 ===
生成的文件:
  ✓ juejin-app-v1.0.0-macos.dmg (45 MB)

=== 测试运行 ===
是否运行测试? [y/N]: y
启动应用测试...
挂载 DMG: build/releases/desktop/juejin-app-v1.0.0-macos.dmg

========================================
✓ Desktop 构建完成！
========================================
```

---

### 演示 6: 验证脚本

```bash
$ ./scripts/release/verify-scripts.sh

========================================
    发布脚本验证
========================================

=== 检查必需工具 ===
检查 Git... ✓
检查 Java... ✓
检查 Gradle Wrapper... ✓
检查 GitHub CLI... ✓
检查 ADB (可选)... ✓

=== 检查脚本文件 ===
检查 android-build.sh... ✓
检查 desktop-build.sh... ✓
检查 cross-platform-build.sh... ✓
检查 quick-build-upload.sh... ✓
检查 create-lite-tag.sh... ✓
检查 recreate-tag.sh... ✓
检查 upload-release.sh... ✓
检查 check-pipeline.sh... ✓
检查 test-build.sh... ✓
检查 complete-release.sh... ✓

=== 检查 Git 仓库 ===
检查 Git 仓库... ✓
检查 远程仓库... ✓
  ✓ GitHub 仓库: git@github.com:xiaqingpeng/juejin-app.git

=== 检查构建环境 ===
检查 Android SDK... ✓
检查 Gradle 配置... ✓
检查 Compose App 模块... ✓

=== 检查 GitHub CLI ===
  ✓ GitHub CLI 已认证

=== 检查构建目录 ===
  ✓ build/releases 存在
  ✓ build/releases/android 存在
  ✓ build/releases/desktop 存在

=== 测试脚本语法 ===
  ✓ android-build.sh 语法正确
  ✓ desktop-build.sh 语法正确
  # ... 其他脚本 ...

========================================
验证总结:
  总检查项: 21
  通过: 21
  失败: 0
✓ 所有检查通过！
========================================
```

---

## 📱 实际使用案例

### 案例 1: 日常开发测试

```bash
# 早上开始工作
$ git pull origin main

# 快速构建测试版本
$ ./scripts/release/android-build.sh --version v1.0.0-dev --type debug

# 安装到测试设备
$ adb install -r build/releases/android/juejin-app-v1.0.0-dev-android-debug.apk

# 查看日志
$ adb logcat -s "juejin-app"
```

### 案例 2: 周五发布新版本

```bash
# 1. 更新版本号和文档
$ vim build.gradle.kts  # 更新版本号
$ vim CHANGELOG.md      # 更新变更日志

# 2. 提交代码
$ git add .
$ git commit -m "Release v1.0.0"
$ git push

# 3. 验证环境
$ ./scripts/release/verify-scripts.sh

# 4. 构建和发布
$ ./scripts/release/quick-build-upload.sh --version v1.0.0 --platform all

# 5. 验证 Release
# 访问 https://github.com/xiaqingpeng/juejin-app/releases/tag/v1.0.0
```

### 案例 3: 紧急热修复

```bash
# 1. 修复 bug
$ git checkout -b hotfix/v1.0.1
# ... 修复代码 ...
$ git commit -m "Fix critical bug"
$ git push

# 2. 合并到主分支
$ git checkout main
$ git merge hotfix/v1.0.1
$ git push

# 3. 快速发布
$ ./scripts/release/create-lite-tag.sh --tag v1.0.1

# 4. 通知用户
# 发送更新通知
```

---

## 🎯 最佳实践

### 1. 发布前检查

```bash
# 使用检查清单
$ cat CHECKLIST.md

# 运行测试
$ ./scripts/release/test-build.sh

# 验证环境
$ ./scripts/release/verify-scripts.sh
```

### 2. 版本号管理

```bash
# 主版本更新
v1.0.0 -> v2.0.0

# 次版本更新
v1.0.0 -> v1.1.0

# 修订版本更新
v1.0.0 -> v1.0.1

# 预发布版本
v1.0.0-beta.1
v1.0.0-rc.1
```

### 3. 构建类型选择

```bash
# 开发测试 - 使用 Debug
$ ./scripts/release/android-build.sh --type debug

# 内部测试 - 使用 Release
$ ./scripts/release/android-build.sh --type release

# 正式发布 - 使用 All
$ ./scripts/release/android-build.sh --type all
```

---

## 💡 提示和技巧

### 1. 快速命令

```bash
# 创建别名
alias build-android='./scripts/release/android-build.sh'
alias build-desktop='./scripts/release/desktop-build.sh'
alias verify='./scripts/release/verify-scripts.sh'

# 使用
$ build-android --version v1.0.0 --type debug
```

### 2. 批量操作

```bash
# 批量上传所有 Android 文件
$ for file in build/releases/android/*; do
    ./scripts/release/upload-release.sh "$file" --tag v1.0.0
done
```

### 3. 查看帮助

```bash
# 所有脚本都支持 --help
$ ./scripts/release/android-build.sh --help
$ ./scripts/release/desktop-build.sh --help
$ ./scripts/release/quick-build-upload.sh --help
```

---

## 🎉 总结

现在你已经看到了完整的使用演示！

**关键要点：**
1. 使用 `verify-scripts.sh` 验证环境
2. 新手使用 `complete-release.sh` 菜单
3. 熟练用户使用具体的构建脚本
4. 紧急情况使用 `create-lite-tag.sh`

**开始使用：**
```bash
./scripts/release/verify-scripts.sh
./scripts/release/complete-release.sh
```

祝你发布顺利！🚀
