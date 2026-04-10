# KMP Monorepo 改造指南

## 目标架构

将当前单应用项目改造成支持多应用开发的 Monorepo 架构：

```
juejin-monorepo/
├── apps/                          # 应用层
│   ├── juejin-main/              # 主应用（当前的 composeApp）
│   │   ├── androidApp/
│   │   ├── iosApp/
│   │   ├── desktopApp/
│   │   └── build.gradle.kts
│   ├── juejin-lite/              # 轻量版应用
│   │   ├── androidApp/
│   │   ├── iosApp/
│   │   └── build.gradle.kts
│   └── admin-dashboard/          # 管理后台
│       ├── desktopApp/
│       └── build.gradle.kts
├── shared/                        # 共享模块层
│   ├── core/                     # 核心功能
│   │   ├── common/               # 通用工具
│   │   ├── network/              # 网络层
│   │   ├── database/             # 数据库
│   │   └── storage/              # 存储
│   ├── features/                 # 功能模块
│   │   ├── auth/                 # 认证
│   │   ├── profile/              # 个人资料
│   │   ├── article/              # 文章
│   │   └── course/               # 课程
│   ├── ui/                       # UI 组件库
│   │   ├── components/           # 通用组件
│   │   ├── theme/                # 主题系统
│   │   └── resources/            # 资源文件
│   └── domain/                   # 领域模型
│       ├── models/               # 数据模型
│       └── repositories/         # 仓库接口
├── build-logic/                  # 构建逻辑
│   ├── convention/               # 约定插件
│   └── build.gradle.kts
├── gradle/
├── settings.gradle.kts
└── build.gradle.kts
```

## 改造步骤

### 第一阶段：准备工作

#### 1. 备份当前项目

```bash
# 创建备份分支
git checkout -b backup/before-monorepo
git push origin backup/before-monorepo

# 创建新的开发分支
git checkout -b feature/monorepo-migration
```

#### 2. 创建新的目录结构

```bash
# 创建主要目录
mkdir -p apps/juejin-main
mkdir -p shared/core/{common,network,database,storage}
mkdir -p shared/features/{auth,profile,article,course}
mkdir -p shared/ui/{components,theme,resources}
mkdir -p shared/domain/{models,repositories}
mkdir -p build-logic/convention
```

### 第二阶段：迁移共享代码

#### 1. 创建 shared/core/common 模块

这个模块包含通用工具类、扩展函数等。

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
        
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
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

#### 2. 创建 shared/core/network 模块

网络层模块，包含 API 定义和网络请求。

**shared/core/network/build.gradle.kts:**

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidTarget()
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "CoreNetwork"
            isStatic = true
        }
    }
    jvm("desktop")
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core:common"))
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
        }
        
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        
        val desktopMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
    }
}

android {
    namespace = "com.example.juejin.core.network"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
}
```

#### 3. 创建 shared/core/database 模块

数据库模块，使用 SQLDelight。

**shared/core/database/build.gradle.kts:**

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget()
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "CoreDatabase"
            isStatic = true
        }
    }
    jvm("desktop")
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core:common"))
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
        }
        
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
        }
        
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
        
        val desktopMain by getting {
            dependencies {
                implementation(libs.sqldelight.sqlite.driver)
            }
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.example.juejin.core.database")
        }
    }
}

android {
    namespace = "com.example.juejin.core.database"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
}
```

#### 4. 创建 shared/ui/components 模块

UI 组件库模块。

**shared/ui/components/build.gradle.kts:**

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget()
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "UIComponents"
            isStatic = true
        }
    }
    jvm("desktop")
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core:common"))
            implementation(project(":shared:ui:theme"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }
    }
}

android {
    namespace = "com.example.juejin.ui.components"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
}
```

#### 5. 创建 shared/features/auth 模块

功能模块示例：认证模块。

**shared/features/auth/build.gradle.kts:**

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget()
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "FeatureAuth"
            isStatic = true
        }
    }
    jvm("desktop")
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core:common"))
            implementation(project(":shared:core:network"))
            implementation(project(":shared:core:storage"))
            implementation(project(":shared:ui:components"))
            implementation(project(":shared:domain:models"))
            
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(libs.lifecycle.viewmodel.compose)
        }
    }
}

android {
    namespace = "com.example.juejin.feature.auth"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
}
```

### 第三阶段：迁移应用代码

#### 1. 重构主应用

将当前的 `composeApp` 移动到 `apps/juejin-main`：

```bash
# 移动应用代码
mv composeApp apps/juejin-main
mv iosApp apps/juejin-main/iosApp
```

**apps/juejin-main/build.gradle.kts:**

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "JuejinMain"
            isStatic = true
            
            // 导出所有共享模块
            export(project(":shared:core:common"))
            export(project(":shared:core:network"))
            export(project(":shared:core:database"))
            export(project(":shared:ui:components"))
            export(project(":shared:features:auth"))
            export(project(":shared:features:profile"))
            export(project(":shared:features:article"))
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        commonMain.dependencies {
            // 共享模块依赖
            implementation(project(":shared:core:common"))
            implementation(project(":shared:core:network"))
            implementation(project(":shared:core:database"))
            implementation(project(":shared:core:storage"))
            implementation(project(":shared:ui:components"))
            implementation(project(":shared:ui:theme"))
            implementation(project(":shared:features:auth"))
            implementation(project(":shared:features:profile"))
            implementation(project(":shared:features:article"))
            implementation(project(":shared:features:course"))
            implementation(project(":shared:domain:models"))
            
            // Compose 依赖
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            
            // 其他依赖
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.navigation.compose)
        }
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    namespace = "com.example.juejin.main"
    compileSdk = 35
    
    defaultConfig {
        applicationId = "com.example.juejin"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

#### 2. 创建轻量版应用

**apps/juejin-lite/build.gradle.kts:**

```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget()
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "JuejinLite"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            // 只依赖核心功能
            implementation(project(":shared:core:common"))
            implementation(project(":shared:core:network"))
            implementation(project(":shared:ui:components"))
            implementation(project(":shared:features:article"))
            
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
        }
    }
}

android {
    namespace = "com.example.juejin.lite"
    compileSdk = 35
    
    defaultConfig {
        applicationId = "com.example.juejin.lite"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0-lite"
    }
}
```

### 第四阶段：更新配置文件

#### 1. 更新 settings.gradle.kts

```kotlin
rootProject.name = "JuejinMonorepo"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
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

// 应用层
include(":apps:juejin-main")
include(":apps:juejin-lite")
include(":apps:admin-dashboard")

// 共享模块 - 核心
include(":shared:core:common")
include(":shared:core:network")
include(":shared:core:database")
include(":shared:core:storage")

// 共享模块 - 功能
include(":shared:features:auth")
include(":shared:features:profile")
include(":shared:features:article")
include(":shared:features:course")

// 共享模块 - UI
include(":shared:ui:components")
include(":shared:ui:theme")
include(":shared:ui:resources")

// 共享模块 - 领域
include(":shared:domain:models")
include(":shared:domain:repositories")
```

#### 2. 创建构建约定插件

**build-logic/convention/build.gradle.kts:**

```kotlin
plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "juejin.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("kotlinMultiplatform") {
            id = "juejin.kotlin.multiplatform"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
    }
}
```

### 第五阶段：代码迁移

#### 迁移清单

1. **通用工具类** → `shared/core/common`
   - Logger
   - DateTimeUtil
   - StringUtil
   - 扩展函数

2. **网络层** → `shared/core/network`
   - API 接口定义
   - HTTP 客户端配置
   - 请求/响应拦截器

3. **数据库** → `shared/core/database`
   - SQLDelight 定义
   - DatabaseDriverFactory
   - Repository 实现

4. **存储** → `shared/core/storage`
   - PrivacyStorage
   - PreferencesManager

5. **UI 组件** → `shared/ui/components`
   - TopNavigationBar
   - BottomNavigationBar
   - 通用对话框
   - 加载指示器

6. **主题系统** → `shared/ui/theme`
   - ThemeManager
   - ThemeColors
   - Typography

7. **功能模块** → `shared/features/*`
   - 认证相关 → auth
   - 个人资料 → profile
   - 文章相关 → article
   - 课程相关 → course

8. **数据模型** → `shared/domain/models`
   - User
   - Article
   - Course
   - 等等

## 优势

### 1. 代码复用
- 多个应用共享核心代码
- 减少重复开发
- 统一的业务逻辑

### 2. 独立开发
- 每个应用可以独立开发和部署
- 不同的应用可以有不同的功能集
- 灵活的版本管理

### 3. 模块化
- 清晰的模块边界
- 更好的代码组织
- 易于维护和测试

### 4. 团队协作
- 不同团队可以负责不同的模块
- 减少代码冲突
- 并行开发

### 5. 构建优化
- 只构建需要的模块
- 增量编译
- 更快的构建速度

## 最佳实践

### 1. 模块设计原则

- **单一职责**：每个模块只负责一个功能领域
- **依赖倒置**：高层模块不依赖低层模块，都依赖抽象
- **接口隔离**：模块间通过接口通信
- **最小依赖**：只依赖必要的模块

### 2. 命名规范

- **模块命名**：`shared:core:network`、`shared:features:auth`
- **包名**：`com.example.juejin.core.network`、`com.example.juejin.feature.auth`
- **类名**：清晰表达职责

### 3. 版本管理

使用 Version Catalog 统一管理依赖版本：

```toml
[versions]
kotlin = "2.1.0"
compose = "1.7.1"
ktor = "3.0.2"

[libraries]
kotlinx-coroutines-core = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "coroutines" }
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }

[plugins]
kotlinMultiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
```

### 4. 文档

为每个模块创建 README.md：

```markdown
# shared/core/network

网络层模块，提供 HTTP 客户端和 API 接口。

## 功能

- HTTP 客户端配置
- API 接口定义
- 请求/响应拦截
- 错误处理

## 使用

\`\`\`kotlin
val client = HttpClientFactory.create()
val api = ApiClient(client)
val result = api.getArticles()
\`\`\`

## 依赖

- shared:core:common
- ktor-client-core
```

## 迁移检查清单

- [ ] 创建目录结构
- [ ] 创建所有共享模块的 build.gradle.kts
- [ ] 迁移通用工具类到 core/common
- [ ] 迁移网络层到 core/network
- [ ] 迁移数据库到 core/database
- [ ] 迁移存储到 core/storage
- [ ] 迁移 UI 组件到 ui/components
- [ ] 迁移主题系统到 ui/theme
- [ ] 迁移功能模块到 features/*
- [ ] 迁移数据模型到 domain/models
- [ ] 更新主应用依赖
- [ ] 创建轻量版应用
- [ ] 更新 settings.gradle.kts
- [ ] 测试所有应用编译通过
- [ ] 测试所有应用运行正常
- [ ] 更新文档

## 注意事项

1. **渐进式迁移**：不要一次性迁移所有代码，逐步进行
2. **保持可编译**：每次迁移后确保项目可以编译
3. **测试覆盖**：迁移后运行测试确保功能正常
4. **版本控制**：频繁提交，便于回滚
5. **团队沟通**：确保团队成员了解新的架构

## 下一步

1. 开始第一阶段：创建目录结构
2. 创建第一个共享模块：core/common
3. 迁移一个简单的工具类进行验证
4. 逐步迁移其他模块
5. 创建第二个应用验证架构

## 参考资源

- [Kotlin Multiplatform 官方文档](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform 文档](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Gradle 多项目构建](https://docs.gradle.org/current/userguide/multi_project_builds.html)
