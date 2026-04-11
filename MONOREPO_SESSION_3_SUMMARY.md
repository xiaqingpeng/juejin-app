# KMP Monorepo 迁移 - Session 3 总结

## 本次完成的工作

### 1. 创建 shared/ui/components 模块 ✅
- 迁移了 `TopNavigationBar` - 通用顶部导航栏组件
- 迁移了 `TabPager` - Tab 切换和页面滑动组件
- 策略调整：依赖平台特定代码和资源的组件保留在主应用中

### 2. 创建 shared/core/network 模块 ✅
- 迁移了 `ApiConfig` - API 配置管理
- 迁移了 `HttpClientManager` - Ktor HTTP 客户端封装
- 支持 GET、POST、PUT、DELETE、PATCH 请求
- 修复了 JVM target 版本不匹配问题（移除 inline 函数）

### 3. 解决的技术问题

#### 问题 1：iOS 编译时间过长
- **现象**：编译 iOS framework 需要 10-20 分钟，经常卡住
- **原因**：需要为 3 个 iOS 架构（iosX64, iosArm64, iosSimulatorArm64）分别编译和链接
- **解决方案**：开发时只编译 Android 版本 `./gradlew :composeApp:assembleDebug`

#### 问题 2：JVM target 版本不匹配
- **现象**：`Cannot inline bytecode built with JVM target 17 into bytecode that is being built with JVM target 11`
- **原因**：Ktor 库使用 JVM 17 编译，而主应用使用 JVM 11
- **解决方案**：将 `HttpClientManager.parseResponse` 从 `inline reified` 改为普通泛型函数

## 当前架构状态

```
juejin-app/
├── shared/
│   ├── core/
│   │   ├── common/              ✅ 已完成
│   │   ├── storage/             ✅ 已完成
│   │   └── network/             ✅ 已完成
│   └── ui/
│       ├── theme/               ✅ 已完成
│       └── components/          ✅ 已完成（部分）
├── composeApp/                  ✅ 已集成
└── settings.gradle.kts          ✅ 已更新
```

## 配置文件更新

### settings.gradle.kts
```kotlin
include(":composeApp")

// 共享模块 - 核心
include(":shared:core:common")
include(":shared:core:storage")
include(":shared:core:network")

// 共享模块 - UI
include(":shared:ui:theme")
include(":shared:ui:components")
```

### composeApp/build.gradle.kts
```kotlin
commonMain.dependencies {
    // 共享模块依赖
    implementation(project(":shared:core:common"))
    implementation(project(":shared:core:storage"))
    implementation(project(":shared:core:network"))
    implementation(project(":shared:ui:theme"))
    implementation(project(":shared:ui:components"))
    // ...
}
```

## 性能优化建议

### 编译时间优化
1. **开发时只编译 Android**：`./gradlew :composeApp:assembleDebug`（15秒）
2. **增加 Gradle 内存**：在 `gradle.properties` 中设置 `org.gradle.jvmargs=-Xmx8g -XX:MaxMetaspaceSize=2g`
3. **使用 Gradle 配置缓存**：已启用
4. **按需编译**：只在需要时编译 iOS 版本

### 模块划分策略
1. **纯 Kotlin 代码**：迁移到共享模块
2. **平台特定代码**：保留在主应用或创建 expect/actual
3. **依赖资源文件**：保留在主应用
4. **UI 组件**：优先迁移纯 Compose 组件

## 进度统计

- **已完成模块**: 5/13 (common, storage, network, theme, components)
- **编译状态**: ✅ 全部通过
- **总体进度**: 约 45%

## 下一步计划

### 优先级 1：shared/core/database
- 创建 `build.gradle.kts` 配置
- 配置 SQLDelight
- 迁移数据库表定义
- 迁移 `DatabaseDriverFactory`

### 优先级 2：shared/domain
- 创建领域模型
- 迁移业务逻辑
- 创建 Repository 接口

### 优先级 3：shared/features
- 按功能模块拆分
- 迁移 ViewModel
- 迁移 Screen

## 技术要点

### 向后兼容性
使用 typealias 保持向后兼容：
```kotlin
@Deprecated("Use com.example.juejin.core.network.ApiConfig instead")
typealias ApiConfig = com.example.juejin.core.network.ApiConfig
```

### JVM Target 一致性
所有模块必须使用相同的 JVM target（当前为 11）：
```kotlin
compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
```

### 避免 inline reified 函数
在共享模块中避免使用 inline reified 函数，因为可能导致 JVM target 不匹配问题。

## 编译命令参考

```bash
# 编译 Android 版本（快速）
./gradlew :composeApp:assembleDebug --no-daemon

# 编译特定共享模块
./gradlew :shared:core:network:build --no-daemon

# 清理构建
./gradlew clean --no-daemon

# 编译 iOS 版本（慢）
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
```

---

**最后更新**: 2026-04-11
**总耗时**: 约 2 小时
**下次继续**: 创建 database 模块
