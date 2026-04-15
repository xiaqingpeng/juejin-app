# Navigation 导航迁移指南

## 概述
本项目已将基于状态管理的导航方式迁移到 Jetpack Compose Navigation 导航框架。

## 已完成的工作

### 1. 添加依赖
在 `gradle/libs.versions.toml` 中添加了 Navigation 依赖：
```toml
androidx-navigation = "2.8.0-alpha10"
androidx-navigation-compose = { module = "org.jetbrains.androidx.navigation:navigation-compose", version.ref = "androidx-navigation" }
```

在 `composeApp/build.gradle.kts` 中引入：
```kotlin
implementation(libs.androidx.navigation.compose)
```

### 2. 创建导航路由定义
创建了 `NavRoutes.kt` 文件，使用 type-safe navigation 方式定义所有路由：
- `Main`: 主页面（包含底部导航的 Tab 页面）
- `Settings`: 设置页面
- `SettingDetail`: 设置详情页面
- `EditProfile`: 编辑资料页面
- `QrScanner`: 二维码扫描页面
- `TestList`: 测试列表页面
- `TestDetail`: 测试详情页面
- `CourseList`: 课程列表页面
- `CourseDetail`: 课程详情页面

### 3. 创建导航图
创建了 `NavGraph.kt` 文件，配置了所有页面的导航关系：
- 使用 `NavHost` 管理所有页面
- 使用 `composable` 定义每个路由对应的页面
- 使用 `navController.navigate()` 进行页面跳转
- 使用 `navController.navigateUp()` 返回上一页

### 4. 重构 App.kt
- 移除了所有基于状态的导航变量（`showSettings`, `showTestList`, `selectedTestCase` 等）
- 使用 `rememberNavController()` 创建导航控制器
- 使用 `currentBackStackEntryAsState()` 监听当前路由
- 根据当前路由判断是否显示底部导航栏和浮动按钮
- 将主内容区域替换为 `AppNavGraph`

### 5. 更新 SettingsScreen
- 移除了内部的状态导航逻辑
- 添加了 `onNavigateToDetail` 和 `onNavigateToEditProfile` 回调
- 通过回调触发导航，而不是内部状态管理

### 6. 更新 TestRegistry
- 添加了课程数据管理功能
- 提供 `getTestCase()` 和 `getCourse()` 方法供导航使用

### 7. 更新 CourseListScreen
- 在渲染课程列表时自动注册课程到 TestRegistry
- 确保导航到课程详情时能获取到课程数据

## 导航使用示例

### 基本导航
```kotlin
// 跳转到设置页面
navController.navigate(NavRoutes.Settings)

// 跳转到二维码扫描页面
navController.navigate(NavRoutes.QrScanner)

// 返回上一页
navController.navigateUp()
```

### 带参数的导航
```kotlin
// 跳转到测试详情页面
navController.navigate(NavRoutes.TestDetail(testId = "test_001"))

// 跳转到课程详情页面
navController.navigate(NavRoutes.CourseDetail(courseId = "course_123"))

// 跳转到设置详情页面
navController.navigate(NavRoutes.SettingDetail(settingTitle = "隐私设置"))
```

## 注意事项

### 编译问题
当前项目存在 Kotlin 版本不兼容问题：
- 项目使用 Kotlin 2.3.0
- 但编译器版本是 2.1.0

需要执行以下操作之一：
1. 清理并重新构建项目：
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

2. 或者同步 Gradle 配置，确保使用正确的 Kotlin 版本

### 数据传递
对于复杂对象的传递，当前使用了两种方式：
1. 通过路由参数传递简单数据（如 ID、标题等）
2. 通过全局注册表（TestRegistry）存储和获取复杂对象

未来可以考虑：
- 使用 ViewModel 共享数据
- 使用 SavedStateHandle 传递序列化数据
- 使用依赖注入框架管理数据

## 迁移前后对比

### 迁移前（状态管理）
```kotlin
var showSettings by remember { mutableStateOf(false) }
var showQrScanner by remember { mutableStateOf(false) }

// 跳转
showSettings = true

// 返回
showSettings = false
```

### 迁移后（Navigation）
```kotlin
val navController = rememberNavController()

// 跳转
navController.navigate(NavRoutes.Settings)

// 返回
navController.navigateUp()
```

## 优势

1. **类型安全**：使用 Kotlin 序列化定义路由，编译时检查参数类型
2. **统一管理**：所有导航逻辑集中在 NavGraph 中
3. **返回栈管理**：自动处理返回栈，支持系统返回键
4. **深度链接**：易于支持深度链接和外部跳转
5. **测试友好**：导航逻辑独立，易于单元测试
6. **可维护性**：代码结构清晰，易于维护和扩展

## 后续优化建议

1. 添加导航动画和转场效果
2. 实现深度链接支持
3. 添加导航拦截器（如登录检查）
4. 优化数据传递机制
5. 添加导航单元测试
