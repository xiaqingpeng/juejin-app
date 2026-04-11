# KMP Monorepo 快速参考

## 🚀 快速开始

### 编译命令
```bash
# Android（推荐，15秒）
./gradlew :composeApp:assembleDebug --no-daemon

# iOS 模拟器（慢，2-3分钟）
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64

# 清理
./gradlew clean --no-daemon
```

## 📦 模块结构

```
shared/
├── core/
│   ├── common/      # Logger, DateTimeUtil
│   ├── storage/     # PrivacyStorage
│   └── network/     # ApiConfig, HttpClientManager
├── domain/          # 所有业务模型
└── ui/
    ├── theme/       # ThemeManager, Colors
    └── components/  # TopNavigationBar, TabPager
```

## 🔧 添加新模块

### 1. 创建模块目录
```bash
mkdir -p shared/features/myfeature/src/commonMain/kotlin
```

### 2. 创建 build.gradle.kts
```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    jvm("desktop")
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core:common"))
            implementation(project(":shared:domain"))
        }
    }
}

android {
    namespace = "com.example.juejin.features.myfeature"
    compileSdk = 35
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
```

### 3. 更新 settings.gradle.kts
```kotlin
include(":shared:features:myfeature")
```

### 4. 添加依赖到主应用
```kotlin
// composeApp/build.gradle.kts
commonMain.dependencies {
    implementation(project(":shared:features:myfeature"))
}
```

## 📝 使用共享模块

### Logger
```kotlin
import com.example.juejin.core.common.Logger

Logger.d("Tag", "Debug message")
Logger.e("Tag", "Error message")
```

### Network
```kotlin
import com.example.juejin.core.network.ApiConfig
import com.example.juejin.core.network.HttpClientManager

val url = ApiConfig.buildUrl("/api/users")
val response = HttpClientManager.get(url)
val data = HttpClientManager.parseResponse(response, User.serializer())
```

### Theme
```kotlin
import com.example.juejin.ui.theme.AppTheme
import com.example.juejin.ui.theme.ThemeManager

// 初始化（在 MainActivity.onCreate）
ThemeManager.initialize()

// 使用主题
AppTheme {
    // Your UI
}

// 切换主题
ThemeManager.setThemeMode(ThemeMode.DARK)
```

### Storage
```kotlin
import com.example.juejin.core.storage.PrivacyStorage

// 初始化（在 MainActivity.onCreate）
PrivacyStorage.init(context)

// 使用
PrivacyStorage.setPrivacyAccepted(true)
val accepted = PrivacyStorage.isPrivacyAccepted()
```

## ⚠️ 常见问题

### 1. JVM Target 不匹配
**错误**：`Cannot inline bytecode built with JVM target 17...`

**解决**：确保所有模块使用 JVM 11
```kotlin
compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
```

### 2. iOS 编译太慢
**解决**：开发时只编译 Android
```bash
./gradlew :composeApp:assembleDebug --no-daemon
```

### 3. 模块找不到
**错误**：`Project ':shared:xxx' not found`

**解决**：检查 settings.gradle.kts 是否包含该模块
```kotlin
include(":shared:xxx")
```

### 4. 初始化顺序错误
**错误**：运行时崩溃

**解决**：在 MainActivity.onCreate() 中按顺序初始化
```kotlin
PrivacyStorage.init(applicationContext)
ThemeManager.initialize()
```

## 🎯 最佳实践

### Domain 模块
- ✅ 使用 `@Serializable` 注解
- ✅ 纯 Kotlin 代码
- ❌ 不要依赖 UI 框架
- ❌ 不要使用平台特定类型

### Core 模块
- ✅ 使用 expect/actual 模式
- ✅ 提供平台抽象
- ❌ 不要包含业务逻辑

### UI 模块
- ✅ 纯 UI 组件
- ✅ 可复用
- ❌ 不要包含业务逻辑
- ❌ 避免依赖平台特定代码

## 📊 性能指标

| 操作 | 时间 |
|------|------|
| Android 编译 | 15秒 |
| iOS 编译 | 2-3分钟 |
| 清理构建 | 5秒 |
| 单模块编译 | 5-10秒 |

## 🔗 相关文档

- [完整迁移进度](MONOREPO_MIGRATION_PROGRESS.md)
- [最终状态报告](MONOREPO_FINAL_STATUS.md)
- [Session 3 总结](MONOREPO_SESSION_3_SUMMARY.md)

---

**提示**：开发时使用 `--no-daemon` 避免 Gradle Daemon 占用内存
