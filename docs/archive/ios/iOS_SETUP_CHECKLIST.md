# ✅ iOS 配置检查清单

## 快速验证

运行以下命令验证 iOS 配置是否完整：

```bash
# 1. 检查脚本是否可执行
ls -l *.sh | grep -E "(run-ios|setup-ios-lite)"

# 2. 检查 iOS 项目是否存在
ls -ld iosApp iosApp-lite

# 3. 测试 UUID 提取
echo "iPhone 17 Pro (AD4269FE-0288-4055-BF40-B8C27962D616) (Shutdown)" | grep -oE '[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}'
# 应该输出: AD4269FE-0288-4055-BF40-B8C27962D616

# 4. 编译 Framework
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

# 5. 运行应用
./run-ios.sh
```

## 详细检查清单

### ✅ 基础环境

- [ ] macOS 系统
- [ ] Xcode 已安装
- [ ] 命令行工具已安装：`xcode-select --install`
- [ ] 至少有一个 iPhone 模拟器

验证：
```bash
# 检查 Xcode
xcodebuild -version

# 检查模拟器
xcrun simctl list devices available | grep iPhone
```

### ✅ 项目文件

- [ ] `iosApp/` 目录存在（juejin-main）
- [ ] `iosApp-lite/` 目录存在（juejin-lite）
- [ ] `run-ios.sh` 脚本存在且可执行
- [ ] `setup-ios-lite.sh` 脚本存在且可执行

验证：
```bash
ls -ld iosApp iosApp-lite
ls -l run-ios.sh setup-ios-lite.sh
```

### ✅ Framework 编译

- [ ] juejin-main Framework 可以编译
- [ ] juejin-lite Framework 可以编译

验证：
```bash
# juejin-main
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64

# juejin-lite
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

### ✅ Xcode 配置

#### juejin-main (iosApp)
- [ ] 项目可以在 Xcode 中打开
- [ ] ComposeApp.framework 已添加
- [ ] Framework 设置为 "Embed & Sign"
- [ ] 可以编译运行

验证：
```bash
cd iosApp && open iosApp.xcodeproj
# 在 Xcode 中按 Cmd+B 编译
```

#### juejin-lite (iosApp-lite)
- [ ] 项目可以在 Xcode 中打开
- [ ] JuejinLite.framework 已添加（首次需要手动配置）
- [ ] Framework 设置为 "Embed & Sign"
- [ ] 可以编译运行

验证：
```bash
cd iosApp-lite && open iosApp.xcodeproj
# 在 Xcode 中按 Cmd+B 编译
```

### ✅ 脚本功能

- [ ] `run-ios.sh` 可以正确提取模拟器 UUID
- [ ] `run-ios.sh` 可以启动 juejin-main
- [ ] `run-ios.sh` 可以启动 juejin-lite
- [ ] `setup-ios-lite.sh` 可以配置 juejin-lite

验证：
```bash
# 测试 UUID 提取
./run-ios.sh
# 选择 3 只编译 Framework，检查是否有错误

# 测试完整流程
./run-ios.sh
# 选择 1 或 2，检查是否能成功运行
```

## 常见问题检查

### 问题 1: 模拟器 UUID 错误
```bash
# 症状
xcodebuild: error: Unable to find a device matching { id:Shutdown }

# 检查
grep "grep -oE" run-ios.sh
# 应该看到: grep -oE '[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}'

# 修复
# 如果没有看到上述内容，重新下载 run-ios.sh
```

### 问题 2: Framework 找不到
```bash
# 症状
ld: framework not found ComposeApp

# 检查
ls apps/juejin-main/build/bin/iosSimulatorArm64/debugFramework/
ls apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/

# 修复
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64
```

### 问题 3: 编译失败（符号冲突）
```bash
# 症状
IrPropertySymbolImpl is already bound

# 检查
grep -r "data class TabItem" --include="*.kt" apps/juejin-main/src/

# 修复
./gradlew clean
rm -rf ~/.konan
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64
```

### 问题 4: 脚本不可执行
```bash
# 症状
-bash: ./run-ios.sh: Permission denied

# 修复
chmod +x run-ios.sh setup-ios-lite.sh
```

## 完整测试流程

### 测试 juejin-main
```bash
# 1. 编译 Framework
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64

# 2. 运行应用
./run-ios.sh
# 选择 1

# 3. 验证
# 应该看到模拟器启动并运行应用
```

### 测试 juejin-lite
```bash
# 1. 首次配置（只需一次）
./setup-ios-lite.sh

# 2. 在 Xcode 中配置 Framework（只需一次）
cd iosApp-lite && open iosApp.xcodeproj
# 按照提示配置 JuejinLite.framework

# 3. 运行应用
./run-ios.sh
# 选择 2

# 4. 验证
# 应该看到模拟器启动并运行应用
```

## 性能基准

### 编译时间（参考）
- Framework 编译：~15秒
- Xcode 构建：~8-10秒
- 总计：~23-25秒

### 应用大小（估算）
- juejin-main：~20MB
- juejin-lite：~15MB

## 文档参考

如果遇到问题，查看以下文档：

1. [iOS_APPS_QUICK_START.md](iOS_APPS_QUICK_START.md) - 快速启动指南
2. [iOS_LITE_SETUP_GUIDE.md](iOS_LITE_SETUP_GUIDE.md) - juejin-lite 配置
3. [iOS_RUN_GUIDE.md](iOS_RUN_GUIDE.md) - 运行指南
4. [iOS_COMPILE_FIX.md](iOS_COMPILE_FIX.md) - 编译问题修复
5. [SESSION_12_iOS_LITE_COMPLETE.md](SESSION_12_iOS_LITE_COMPLETE.md) - 详细说明

## 下一步

全部检查通过后，你可以：

1. ✅ 开始 iOS 平台的功能开发
2. ✅ 在真机上测试（需要配置证书）
3. ✅ 配置 CI/CD 自动构建
4. ✅ 准备 App Store 发布

---

**更新时间**: 2026-04-11  
**状态**: ✅ 完成
