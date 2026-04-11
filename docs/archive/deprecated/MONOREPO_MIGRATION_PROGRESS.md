# KMP Monorepo 迁移进度

## 当前状态：阶段 2 进行中 ⏳

### 已完成的工作

#### 1. 目录结构创建 ✅
- 运行了 `setup-monorepo.sh` 脚本
- 创建了所有必要的目录结构
- 应用层：`apps/juejin-main`, `apps/juejin-lite`, `apps/admin-dashboard`
- 共享模块：`shared/core/*`, `shared/ui/*`, `shared/features/*`, `shared/domain/*`

#### 2. 第一个共享模块：shared/core/common ✅
- ✅ 创建了 `build.gradle.kts` 配置
- ✅ 迁移了 `Logger` 工具类（Android, iOS, Desktop 平台实现）
- ✅ 迁移了 `DateTimeUtil` 工具类
- ✅ 修复了 iOS 平台的 `DateTimeUtil` 编译错误
- ✅ 模块编译成功：`./gradlew :shared:core:common:build`
- ✅ 主应用集成成功：`./gradlew :composeApp:assembleDebug`

#### 3. 配置文件更新 ✅
- ✅ `settings.gradle.kts` 已包含 `shared:core:common` 模块
- ✅ `composeApp/build.gradle.kts` 已添加依赖：`implementation(project(":shared:core:common"))`

#### 4. 代码修复 ✅
- ✅ 修复了 `ThemeManager.kt` 的 JVM 签名冲突
  - 将 `themeMode` 改为私有的 `_themeMode`
  - 提供公开的只读属性 `themeMode`

#### 5. 第二个共享模块：shared/core/storage ✅
- ✅ 创建了 `build.gradle.kts` 配置
- ✅ 迁移了 `PrivacyStorage` 工具类（Android, iOS, Desktop 平台实现）
- ✅ 模块编译成功：`./gradlew :shared:core:storage:build`
- ✅ 主应用集成成功：`./gradlew :composeApp:assembleDebug`
- ✅ 更新了 `settings.gradle.kts` 包含 storage 模块
- ✅ 更新了主应用的导入语句使用新的包名

#### 6. 第三个共享模块：shared/ui/theme ✅
- ✅ 创建了 `build.gradle.kts` 配置
- ✅ 迁移了 `ThemeManager` - 主题管理器
- ✅ 迁移了 `AppTheme` - 应用主题组件
- ✅ 迁移了 `ThemeColors` - 主题颜色对象
- ✅ 迁移了 `LightColors` 和 `DarkColors` - 浅色和深色主题颜色定义
- ✅ 迁移了 `SystemTheme` - 系统主题检测（Android, iOS, Desktop 平台实现）
- ✅ 迁移了 `ThemeNotification` - 主题变化通知（Android, iOS, Desktop 平台实现）
- ✅ 更新了 `settings.gradle.kts` 包含 theme 模块
- ✅ 更新了主应用依赖和导入语句

### 当前架构

```
juejin-app/
├── shared/
│   └── core/
│       ├── common/              ✅ 已完成
│       │   ├── src/
│       │   │   ├── commonMain/
│       │   │   │   └── kotlin/
│       │   │   │       └── com/example/juejin/core/common/
│       │   │   │           ├── Logger.kt
│       │   │   │           └── DateTimeUtil.kt
│       │   │   ├── androidMain/
│       │   │   │   └── kotlin/
│       │   │   │       └── com/example/juejin/core/common/
│       │   │   │           ├── Logger.android.kt
│       │   │   │           └── DateTimeUtil.android.kt
│       │   │   ├── iosMain/
│       │   │   │   └── kotlin/
│       │   │   │       └── com/example/juejin/core/common/
│       │   │   │           ├── Logger.ios.kt
│       │   │   │           └── DateTimeUtil.ios.kt
│       │   │   └── desktopMain/
│       │   │       └── kotlin/
│       │   │           └── com/example/juejin/core/common/
│       │   │               └── Logger.jvm.kt
│       │   └── build.gradle.kts
│       └── storage/             ✅ 已完成
│           ├── src/
│           │   ├── commonMain/
│           │   │   └── kotlin/
│           │   │       └── com/example/juejin/core/storage/
│           │   │           └── PrivacyStorage.kt
│           │   ├── androidMain/
│           │   │   └── kotlin/
│           │   │       └── com/example/juejin/core/storage/
│           │   │           └── PrivacyStorage.android.kt
│           │   ├── iosMain/
│           │   │   └── kotlin/
│           │   │       └── com/example/juejin/core/storage/
│           │   │           └── PrivacyStorage.ios.kt
│           │   └── desktopMain/
│           │       └── kotlin/
│           │           └── com/example/juejin/core/storage/
│           │               └── PrivacyStorage.jvm.kt
│           └── build.gradle.kts
├── composeApp/                  ✅ 已集成共享模块
└── settings.gradle.kts          ✅ 已更新
```

#### 优先级 4：shared/ui/components ✅
- ✅ 创建 `build.gradle.kts` 配置
- ✅ 迁移 `TopNavigationBar` 组件
- ✅ 迁移 `TabPager` 组件
- ✅ 测试编译
- ⚠️ 注意：依赖平台特定代码的组件（StatusBarEffect、ImageSourceBottomSheet、WebViewScreen）和依赖资源的组件（PrivacyPolicyDialog、NotificationPermissionDialog）保留在主应用中

## 下一步计划

### 阶段 2：继续创建核心模块

#### 优先级 1：shared/core/network ✅
- ✅ 创建了 `build.gradle.kts` 配置
- ✅ 迁移了 `ApiConfig` - API 配置管理
- ✅ 迁移了 `HttpClientManager` - Ktor HTTP 客户端封装
- ✅ 修复了 JVM target 版本不匹配问题
- ✅ 测试编译通过

#### 优先级 2：shared/domain ✅
- ✅ 创建了 `build.gradle.kts` 配置
- ✅ 迁移了所有领域模型（User, Hot, Article, Circle, Setting, LogStats）
- ✅ 添加了 Serialization 支持
- ✅ 测试编译通过

#### 优先级 3：shared/core/database
- ⏭️ 跳过（项目暂未使用数据库）

### 阶段 3：UI 模块
- [x] shared/ui/theme ✅
- [x] shared/ui/components ✅ (部分完成)
- [ ] shared/ui/resources

### 阶段 4：功能模块
- [ ] shared/features/auth
- [ ] shared/features/profile
- [ ] shared/features/article
- [ ] shared/features/course

### 阶段 5：应用层迁移
- [ ] 迁移主应用到 `apps/juejin-main`
- [ ] 创建轻量版应用 `apps/juejin-lite`

## 技术要点

### 已解决的问题

1. **DateTimeUtil iOS 编译错误**
   - 问题：使用了 `kotlin.time.Instant` 而不是 `kotlinx.datetime.Instant`
   - 解决：更新导入和使用正确的 API

2. **ThemeManager JVM 签名冲突**
   - 问题：`var themeMode` 的 setter 与 `fun setThemeMode()` 冲突
   - 解决：使用私有 `_themeMode` 和公开只读属性

### 最佳实践

1. **渐进式迁移**：一次迁移一个模块，确保每次都能编译通过
2. **测试驱动**：每个模块迁移后立即测试编译
3. **保持兼容**：在迁移过程中保持主应用可用
4. **文档同步**：及时更新文档和进度

## 命令参考

### 编译共享模块
```bash
./gradlew :shared:core:common:build
```

### 编译主应用
```bash
# Android
./gradlew :composeApp:assembleDebug

# iOS
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64

# Desktop
./gradlew :composeApp:packageDistributionForCurrentOS
```

### 清理构建
```bash
./gradlew clean
```

## 时间估算

- ✅ 阶段 1（准备和第一个模块）：已完成
- ⏳ 阶段 2（核心模块）：预计 2-3 天（进行中）
- ⏳ 阶段 3（UI 模块）：预计 1-2 天（部分完成）
- ⏳ 阶段 4（功能模块）：预计 2-3 天
- ⏳ 阶段 5（应用层）：预计 1-2 天

## 性能优化建议

### 编译时间优化
KMP 项目编译 iOS framework 非常耗时（每个模块需要为 3 个 iOS 架构分别编译和链接）。优化建议：

1. **开发时只编译 Android**：`./gradlew :composeApp:assembleDebug`
2. **增加 Gradle 内存**：在 `gradle.properties` 中设置 `org.gradle.jvmargs=-Xmx8g -XX:MaxMetaspaceSize=2g`
3. **使用 Gradle 配置缓存**：已启用
4. **按需编译**：只在需要时编译 iOS 版本

**总进度：约 50% 完成**

## 下一个任务

可以选择以下任一方向继续：
1. 创建 `shared/features` 模块，按功能拆分（推荐）
2. 迁移 Repository 层到 shared/domain
3. 优化现有模块，清理旧代码

推荐创建 features 模块，开始按功能模块化。

---

最后更新：2026-04-10
