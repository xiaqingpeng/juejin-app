# Session 12: juejin-lite iOS 项目完成

## 问题诊断

### 原始错误
```bash
xcodebuild: error: Unable to find a device matching the provided destination specifier:
{ id:Shutdown }
```

### 根本原因
`run-ios.sh` 中的模拟器 UUID 提取逻辑错误：

```bash
# ❌ 错误的提取方式
SIMULATOR=$(xcrun simctl list devices available | grep "iPhone" | head -1 | sed 's/.*(\(.*\)).*/\1/')
```

这个 sed 命令会提取最后一对括号中的内容，对于这样的输出：
```
iPhone 17 Pro (AD4269FE-0288-4055-BF40-B8C27962D616) (Shutdown)
```

会错误地提取到 `Shutdown` 而不是 UUID `AD4269FE-0288-4055-BF40-B8C27962D616`。

## 解决方案

### 修复 UUID 提取逻辑

使用正则表达式直接匹配 UUID 格式：

```bash
# ✅ 正确的提取方式
SIMULATOR=$(xcrun simctl list devices available | grep "iPhone" | head -1 | grep -oE '[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}')
```

### 增强用户体验

同时显示模拟器名称和 UUID：

```bash
SIMULATOR_NAME=$(xcrun simctl list devices available | grep "$SIMULATOR" | sed 's/^ *//;s/ (.*//')
echo -e "${BLUE}使用模拟器: $SIMULATOR_NAME${NC}"
echo -e "${BLUE}UUID: $SIMULATOR${NC}"
```

## 修改的文件

### `run-ios.sh`

修复了两处 UUID 提取逻辑：
1. **Option 1 (juejin-main)**: 第 48-60 行
2. **Option 2 (juejin-lite)**: 第 120-132 行

## 当前状态

### ✅ 已完成

1. **juejin-lite iOS 项目创建**
   - ✅ 复制并配置 `iosApp-lite` 项目
   - ✅ 更新 `ContentView.swift`（import JuejinLite）
   - ✅ 创建 `setup-ios-lite.sh` 配置脚本
   - ✅ JuejinLite Framework 编译成功

2. **运行脚本修复**
   - ✅ 修复模拟器 UUID 提取逻辑
   - ✅ 支持 juejin-main 和 juejin-lite 两个应用
   - ✅ 增强错误提示和用户体验

### ⚠️ 需要用户手动操作

在 Xcode 中配置 Framework（只需一次）：

```bash
cd iosApp-lite && open iosApp.xcodeproj
```

在 Xcode 中：
1. 选择项目 → Target → General
2. 找到 "Frameworks, Libraries, and Embedded Content"
3. 删除旧的 `ComposeApp.framework`
4. 点击 '+' → 'Add Other...' → 'Add Files...'
5. 导航到：`apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/JuejinLite.framework`
6. 选择 "Embed & Sign"

## 使用方法

### 方法 1: 使用脚本（推荐）

```bash
./run-ios.sh
# 选择 2) juejin-lite（轻量版）
```

脚本会自动：
1. 编译 JuejinLite Framework
2. 查找并启动 iOS 模拟器
3. 构建并运行应用

### 方法 2: 手动运行

```bash
# 1. 编译 Framework
./gradlew :apps:juejin-lite:linkDebugFrameworkIosSimulatorArm64

# 2. 在 Xcode 中运行
cd iosApp-lite && open iosApp.xcodeproj
# 然后按 Cmd+R
```

## 项目对比

| 特性 | juejin-main | juejin-lite |
|------|-------------|-------------|
| **功能** | 完整版 | 轻量版（核心功能） |
| **编译时间** | ~38秒 | ~7秒 |
| **iOS 项目** | `iosApp/` | `iosApp-lite/` |
| **Framework** | ComposeApp.framework | JuejinLite.framework |
| **Bundle ID** | orgIdentifier.iosApp | orgIdentifier.iosApp-lite |
| **应用名称** | 掘金 | 掘金轻量版 |

## 相关文档

- `iOS_LITE_SETUP_GUIDE.md` - 详细的 iOS Lite 配置指南
- `iOS_RUN_GUIDE.md` - iOS 应用运行指南
- `run-ios.sh` - iOS 应用启动脚本
- `setup-ios-lite.sh` - iOS Lite 项目配置脚本

## 技术细节

### UUID 格式
iOS 模拟器 UUID 格式：`XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX`
- 8位-4位-4位-4位-12位十六进制数字（大写）
- 示例：`AD4269FE-0288-4055-BF40-B8C27962D616`

### 正则表达式
```bash
grep -oE '[A-F0-9]{8}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{4}-[A-F0-9]{12}'
```
- `-o`: 只输出匹配的部分
- `-E`: 使用扩展正则表达式
- `[A-F0-9]{n}`: 匹配 n 个十六进制数字

## 下一步

现在可以：
1. 使用 `./run-ios.sh` 运行 iOS 应用
2. 在 Xcode 中进行调试和开发
3. 同时安装两个应用（不同的 Bundle ID）
4. 根据需要切换开发平台（Android/iOS/Desktop）

---

**状态**: ✅ 完成
**日期**: 2026-04-11
