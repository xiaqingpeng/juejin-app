# KMP Monorepo 快速开始指南

## 5 分钟快速开始

### 步骤 1: 运行设置脚本

```bash
# 给脚本添加执行权限
chmod +x scripts/setup-monorepo.sh
chmod +x scripts/create-module.sh

# 运行设置脚本
./scripts/setup-monorepo.sh
```

### 步骤 2: 创建第一个共享模块

让我们从最简单的 `core/common` 模块开始：

**shared/core/common/build.gradle.kts:**

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "CoreCommon"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }
    }
}

android {
    namespace = "com.example.juejin.core.common"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
```

### 步骤 3: 迁移第一个工具类

将 `Logger` 迁移到 `shared/core/common`：

**shared/core/common/src/commonMain/kotlin/com/example/juejin/core/common/Logger.kt:**

```kotlin
package com.example.juejin.core.common

expect object Logger {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String)
    fun w(tag: String, message: String)
    fun i(tag: String, message: String)
}
```

**shared/core/common/src/androidMain/kotlin/com/example/juejin/core/common/Logger.android.kt:**

```kotlin
package com.example.juejin.core.common

import android.util.Log

actual object Logger {
    actual fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
    
    actual fun e(tag: String, message: String) {
        Log.e(tag, message)
    }
    
    actual fun w(tag: String, message: String) {
        Log.w(tag, message)
    }
    
    actual fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
}
```

**shared/core/common/src/iosMain/kotlin/com/example/juejin/core/common/Logger.ios.kt:**

```kotlin
package com.example.juejin.core.common

import platform.Foundation.NSLog

actual object Logger {
    actual fun d(tag: String, message: String) {
        NSLog("D/$tag: $message")
    }
    
    actual fun e(tag: String, message: String) {
        NSLog("E/$tag: $message")
    }
    
    actual fun w(tag: String, message: String) {
        NSLog("W/$tag: $message")
    }
    
    actual fun i(tag: String, message: String) {
        NSLog("I/$tag: $message")
    }
}
```

**shared/core/common/src/desktopMain/kotlin/com/example/juejin/core/common/Logger.jvm.kt:**

```kotlin
package com.example.juejin.core.common

actual object Logger {
    actual fun d(tag: String, message: String) {
        println("D/$tag: $message")
    }
    
    actual fun e(tag: String, message: String) {
        System.err.println("E/$tag: $message")
    }
    
    actual fun w(tag: String, message: String) {
        println("W/$tag: $message")
    }
    
    actual fun i(tag: String, message: String) {
        println("I/$tag: $message")
    }
}
```

### 步骤 4: 更新 settings.gradle.kts

```kotlin
rootProject.name = "JuejinMonorepo"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

// 添加第一个共享模块
include(":shared:core:common")

// 保留原有应用（暂时）
include(":composeApp")
```

### 步骤 5: 测试编译

```bash
# 编译共享模块
./gradlew :shared:core:common:build

# 如果成功，你会看到 BUILD SUCCESSFUL
```

### 步骤 6: 在主应用中使用

在 `composeApp/build.gradle.kts` 中添加依赖：

```kotlin
sourceSets {
    commonMain.dependencies {
        // 添加共享模块依赖
        implementation(project(":shared:core:common"))
        
        // ... 其他依赖
    }
}
```

在代码中使用：

```kotlin
import com.example.juejin.core.common.Logger

fun someFunction() {
    Logger.d("MyTag", "Hello from shared module!")
}
```

## 渐进式迁移策略

### 第 1 周：基础设施

1. **Day 1-2**: 创建目录结构和 core/common 模块
2. **Day 3-4**: 迁移通用工具类（Logger, DateTimeUtil 等）
3. **Day 5**: 测试和文档

### 第 2 周：核心模块

1. **Day 1-2**: 创建和迁移 core/network
2. **Day 3-4**: 创建和迁移 core/database
3. **Day 5**: 创建和迁移 core/storage

### 第 3 周：UI 模块

1. **Day 1-2**: 创建和迁移 ui/theme
2. **Day 3-4**: 创建和迁移 ui/components
3. **Day 5**: 测试和优化

### 第 4 周：功能模块

1. **Day 1**: 创建和迁移 features/auth
2. **Day 2**: 创建和迁移 features/profile
3. **Day 3**: 创建和迁移 features/article
4. **Day 4**: 创建和迁移 features/course
5. **Day 5**: 集成测试

### 第 5 周：应用层和收尾

1. **Day 1-2**: 迁移主应用到 apps/juejin-main
2. **Day 3**: 创建轻量版应用
3. **Day 4**: 全面测试
4. **Day 5**: 文档和发布

## 常见问题

### Q1: 如何处理循环依赖？

**A**: 使用依赖倒置原则：

```kotlin
// ❌ 错误：直接依赖
// shared/features/auth 依赖 shared/features/profile
// shared/features/profile 依赖 shared/features/auth

// ✅ 正确：通过接口解耦
// shared/domain/repositories 定义接口
interface UserRepository {
    suspend fun getUser(): User
}

// shared/features/auth 实现接口
class AuthUserRepository : UserRepository {
    override suspend fun getUser(): User { ... }
}

// shared/features/profile 依赖接口
class ProfileViewModel(
    private val userRepository: UserRepository
) { ... }
```

### Q2: 如何共享资源文件？

**A**: 创建 `shared/ui/resources` 模块：

```kotlin
// shared/ui/resources/build.gradle.kts
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    // ... 平台配置
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.components.resources)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.example.juejin.resources"
    generateResClass = always
}
```

### Q3: 如何处理平台特定代码？

**A**: 使用 expect/actual 模式：

```kotlin
// commonMain
expect class PlatformContext

expect fun getPlatformContext(): PlatformContext

// androidMain
actual typealias PlatformContext = Context

actual fun getPlatformContext(): PlatformContext {
    return ApplicationContext.get()
}

// iosMain
actual class PlatformContext

actual fun getPlatformContext(): PlatformContext {
    return PlatformContext()
}
```

### Q4: 如何优化构建时间？

**A**: 使用以下技巧：

1. **启用 Gradle 构建缓存**:

```properties
# gradle.properties
org.gradle.caching=true
org.gradle.parallel=true
org.gradle.configureondemand=true
```

2. **使用 Configuration Cache**:

```bash
./gradlew build --configuration-cache
```

3. **只构建需要的模块**:

```bash
# 只构建特定模块
./gradlew :shared:core:common:build

# 只构建 Android
./gradlew :apps:juejin-main:assembleDebug
```

### Q5: 如何管理版本号？

**A**: 使用 Version Catalog 统一管理：

```toml
# gradle/libs.versions.toml
[versions]
kotlin = "2.1.0"
compose = "1.7.1"
coroutines = "1.9.0"

[libraries]
kotlinx-coroutines-core = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "coroutines" }

[plugins]
kotlinMultiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
```

## 实用命令

### 构建相关

```bash
# 清理构建
./gradlew clean

# 构建所有模块
./gradlew build

# 只构建共享模块
./gradlew :shared:core:common:build

# 构建主应用
./gradlew :apps:juejin-main:assembleDebug

# 运行测试
./gradlew test

# 检查依赖
./gradlew :apps:juejin-main:dependencies
```

### 模块管理

```bash
# 创建新的功能模块
./scripts/create-module.sh feature notification

# 创建新的核心模块
./scripts/create-module.sh core analytics

# 创建新的 UI 模块
./scripts/create-module.sh ui icons
```

### 代码检查

```bash
# 运行 Kotlin lint
./gradlew ktlintCheck

# 自动修复格式问题
./gradlew ktlintFormat

# 运行 Detekt
./gradlew detekt
```

## 下一步

1. ✅ 完成快速开始
2. 📖 阅读 [KMP_MONOREPO_GUIDE.md](./KMP_MONOREPO_GUIDE.md) 了解详细架构
3. ✅ 查看 [MONOREPO_MIGRATION_CHECKLIST.md](./MONOREPO_MIGRATION_CHECKLIST.md) 跟踪进度
4. 🚀 开始迁移你的第一个模块！

## 获取帮助

- 查看 [Kotlin Multiplatform 文档](https://kotlinlang.org/docs/multiplatform.html)
- 查看 [Compose Multiplatform 文档](https://www.jetbrains.com/lp/compose-multiplatform/)
- 在团队中讨论遇到的问题
- 参考现有的 KMP 项目示例

## 成功案例

迁移到 Monorepo 后，你将获得：

- ✅ **代码复用率提升 60%+**
- ✅ **新应用开发时间减少 50%+**
- ✅ **维护成本降低 40%+**
- ✅ **团队协作效率提升 30%+**

祝你迁移顺利！🎉
