# 多应用架构快速参考

## 应用列表

| 应用 | Application ID | 版本 | 功能 | 编译时间 | 状态 |
|------|---------------|------|------|---------|------|
| juejin-main | com.example.juejin | 1.0.0 | 完整功能 | ~38s | ✅ |
| juejin-lite | com.example.juejin.lite | 1.0.0-lite | 核心功能 | ~7s | ✅ |
| admin-dashboard | - | - | 管理后台 | - | 📝 待开发 |

## 快速命令

### 编译单个应用
```bash
# 编译完整版
./gradlew :apps:juejin-main:assembleDebug --no-daemon

# 编译轻量版
./gradlew :apps:juejin-lite:assembleDebug --no-daemon
```

### 编译所有应用
```bash
./gradlew assembleDebug --no-daemon
```

### 安装到设备
```bash
# 安装完整版
./gradlew :apps:juejin-main:installDebug

# 安装轻量版
./gradlew :apps:juejin-lite:installDebug
```

### 运行应用
```bash
# 运行完整版
./gradlew :apps:juejin-main:installDebug
adb shell am start -n com.example.juejin/.MainActivity

# 运行轻量版
./gradlew :apps:juejin-lite:installDebug
adb shell am start -n com.example.juejin.lite/.MainActivity
```

### 清理构建
```bash
# 清理所有
./gradlew clean

# 清理单个应用
./gradlew :apps:juejin-main:clean
./gradlew :apps:juejin-lite:clean
```

## 应用对比

### juejin-main（完整版）
**优势**：
- ✅ 功能完整（首页、热门、发现、课程、我的）
- ✅ 用户体验完整
- ✅ 适合主力用户

**劣势**：
- ⚠️ APK 体积较大
- ⚠️ 编译时间较长（38秒）
- ⚠️ 功能复杂度高

### juejin-lite（轻量版）
**优势**：
- ✅ APK 体积小
- ✅ 编译速度快（7秒）
- ✅ 启动速度快
- ✅ 适合低端设备

**劣势**：
- ⚠️ 功能精简（只有首页、热门、我的）
- ⚠️ 缺少高级功能

## 共享模块

所有应用共享以下模块：

```
shared/
├── core/
│   ├── common          # 通用工具（Logger, DateTimeUtil）
│   ├── storage         # 存储管理（PrivacyStorage）
│   └── network         # 网络请求（HttpClient, ApiConfig）
├── domain              # 领域模型（User, Hot, Article 等）
└── ui/
    ├── theme           # 主题管理（ThemeManager, Colors）
    └── components      # UI 组件（TopNavigationBar, TabPager）
```

## 添加新应用

### 1. 创建应用目录
```bash
mkdir -p apps/new-app/src/{commonMain,androidMain}/kotlin/com/example/juejin/newapp
```

### 2. 创建 build.gradle.kts
```kotlin
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

compose.resources {
    packageOfResClass = "juejin.newapp.generated.resources"
}

android {
    namespace = "com.example.juejin.newapp"
    defaultConfig {
        applicationId = "com.example.juejin.newapp"
        versionName = "1.0.0"
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared:core:common"))
            implementation(project(":shared:core:storage"))
            implementation(project(":shared:core:network"))
            implementation(project(":shared:domain"))
            implementation(project(":shared:ui:theme"))
            implementation(project(":shared:ui:components"))
            // ... 其他依赖
        }
    }
}
```

### 3. 更新 settings.gradle.kts
```kotlin
include(":apps:new-app")
project(":apps:new-app").projectDir = file("apps/new-app")
```

### 4. 创建主应用文件
- `src/commonMain/kotlin/.../App.kt`
- `src/androidMain/kotlin/.../MainActivity.kt`
- `src/androidMain/AndroidManifest.xml`

## 开发建议

### 1. 选择合适的应用进行开发
- **快速迭代**：使用 juejin-lite（编译快）
- **功能开发**：使用 juejin-main（功能全）
- **性能测试**：两个应用都测试

### 2. 代码组织
- **共享代码**：放在 shared 模块
- **应用特定代码**：放在各自的 apps 目录
- **避免重复**：优先使用共享模块

### 3. 资源管理
- 每个应用使用独立的资源包名
- 共享资源放在 shared/ui/components
- 应用特定资源放在各自的 composeResources

### 4. 版本管理
- 使用不同的 versionName 区分版本
- 使用不同的 applicationId 避免冲突
- 可以同时安装在同一设备上

## 故障排查

### 问题：编译失败
```bash
# 清理并重新构建
./gradlew clean
./gradlew :apps:juejin-lite:assembleDebug --no-daemon
```

### 问题：资源冲突
- 检查 compose.resources 配置
- 确保每个应用使用独立的 packageOfResClass

### 问题：依赖冲突
- 检查所有应用使用相同版本的 shared 模块
- 确保 libs.versions.toml 中的版本一致

### 问题：初始化错误
```kotlin
// 确保正确的初始化顺序
PrivacyStorage.init(applicationContext)  // 先初始化存储
ThemeManager.initialize()                 // 再初始化主题
```

## 性能优化

### 编译优化
```bash
# 使用配置缓存
./gradlew --configuration-cache

# 并行编译
./gradlew --parallel

# 只编译 Android（跳过 iOS）
./gradlew :apps:juejin-lite:assembleDebug --no-daemon
```

### APK 优化
```kotlin
// 启用代码混淆
buildTypes {
    release {
        isMinifyEnabled = true
        proguardFiles(...)
    }
}

// 启用资源压缩
buildTypes {
    release {
        isShrinkResources = true
    }
}
```

## 总结

多应用架构让你可以：
- ✅ 快速创建新应用
- ✅ 最大化代码复用
- ✅ 灵活的功能组合
- ✅ 独立的发布节奏
- ✅ 针对不同用户群体优化
