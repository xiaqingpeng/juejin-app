# 🔧 iOS 编译问题修复

## 问题描述

编译 `juejin-main` 的 iOS Framework 时出现错误：

```
IrPropertySymbolImpl is already bound
com_example_juejin_ui_components_TabItem$stableprop
```

## 根本原因

`TabItem` 数据类在两个地方重复定义：

1. `apps/juejin-main/src/commonMain/kotlin/com/example/juejin/ui/components/TabPager.kt`
2. `shared/ui/components/src/commonMain/kotlin/com/example/juejin/ui/components/TabPager.kt`

这导致 Kotlin/Native 编译器在链接时发现同一个符号被绑定了两次。

## 解决方案

删除 `apps/juejin-main` 中重复的 `TabPager.kt` 文件，使用 `shared/ui/components` 模块中的版本。

### 执行的操作

```bash
# 删除重复文件
rm apps/juejin-main/src/commonMain/kotlin/com/example/juejin/ui/components/TabPager.kt
```

### 验证

```bash
# 重新编译 iOS Framework
./gradlew :apps:juejin-main:linkDebugFrameworkIosSimulatorArm64

# 结果：BUILD SUCCESSFUL ✅
```

## 编译结果

### juejin-main
- ✅ iOS Framework 编译成功
- ⏱️ 编译时间：17秒
- 📦 Framework 位置：`apps/juejin-main/build/bin/iosSimulatorArm64/debugFramework/ComposeApp.framework`

### juejin-lite
- ✅ iOS Framework 编译成功
- ⏱️ 编译时间：6秒
- 📦 Framework 位置：`apps/juejin-lite/build/bin/iosSimulatorArm64/debugFramework/JuejinLite.framework`

## 经验教训

### 1. 避免重复定义

在 Monorepo 架构中，共享组件应该只在 `shared` 模块中定义一次，应用模块通过依赖引用。

**正确做法**:
```kotlin
// shared/ui/components/src/commonMain/kotlin/.../TabPager.kt
data class TabItem(val key: String?, val label: String)

// apps/juejin-main/build.gradle.kts
implementation(project(":shared:ui:components"))
```

**错误做法**:
```kotlin
// ❌ 在应用模块中重复定义
// apps/juejin-main/src/.../TabPager.kt
data class TabItem(val key: String?, val label: String)
```

### 2. 检查重复文件

定期检查是否有重复的文件：

```bash
# 查找同名文件
find . -name "TabPager.kt"

# 查找重复的类定义
grep -r "data class TabItem" --include="*.kt"
```

### 3. 使用 IDE 重构

当需要移动代码到共享模块时，使用 IDE 的重构功能：
- Android Studio → Refactor → Move
- 自动更新所有引用

## 相关问题

### Q1: 如何判断是否有重复定义？

**A**: 编译错误信息会提示：
```
IrPropertySymbolImpl is already bound
```

### Q2: 为什么 Android 和 Desktop 没问题？

**A**: 
- Android 和 Desktop 使用 JVM 编译器，有更好的符号去重机制
- iOS 使用 Kotlin/Native 编译器，对符号冲突更敏感

### Q3: 如何预防这类问题？

**A**: 
1. 遵循 Monorepo 架构原则
2. 共享代码放在 `shared` 模块
3. 应用模块只包含应用特定代码
4. 定期代码审查

## 最佳实践

### Monorepo 代码组织

```
juejin-app/
├── shared/                    # 共享模块
│   ├── core/
│   │   ├── common/           # 通用工具
│   │   ├── network/          # 网络请求
│   │   └── storage/          # 数据存储
│   ├── domain/               # 业务逻辑
│   └── ui/
│       ├── theme/            # 主题
│       └── components/       # UI 组件 ← TabItem 应该在这里
│
├── apps/                      # 应用模块
│   ├── juejin-main/          # 完整版
│   │   └── src/
│   │       ├── commonMain/   # 应用特定代码
│   │       ├── androidMain/
│   │       ├── iosMain/
│   │       └── jvmMain/
│   └── juejin-lite/          # 轻量版
```

### 依赖关系

```
apps/juejin-main
├─ depends on → shared/ui/components
│                └─ contains → TabItem
└─ should NOT define → TabItem (重复)
```

## 验证清单

- [x] 删除重复的 TabPager.kt
- [x] iOS Framework 编译成功
- [x] Android 编译正常
- [x] Desktop 编译正常
- [x] 所有平台使用相同的 TabItem 定义

## 总结

通过删除重复定义，成功修复了 iOS 编译问题。这个问题提醒我们：

1. ✅ 共享代码应该只定义一次
2. ✅ 遵循 Monorepo 架构原则
3. ✅ 定期检查重复文件
4. ✅ 使用 IDE 重构工具

---

**修复时间**: 2026-04-11  
**状态**: ✅ 已修复  
**影响**: juejin-main iOS Framework 现在可以正常编译
