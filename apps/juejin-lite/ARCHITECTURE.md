# Juejin Lite - MVVM 架构说明

## 项目概述

Juejin Lite 是一个采用 MVVM（Model-View-ViewModel）架构的 Kotlin Multiplatform 应用，展示了大型商业项目的最佳实践。

## 架构层次

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (UI + ViewModel + UiState)             │
├─────────────────────────────────────────┤
│         Domain Layer                    │
│  (Use Cases + Domain Models + Repos)    │
├─────────────────────────────────────────┤
│         Data Layer                      │
│  (Repository Impl + Data Sources)       │
└─────────────────────────────────────────┘
```

## 目录结构

```
src/commonMain/kotlin/com/example/juejin/lite/
├── data/                    # 数据层
│   ├── model/              # 数据传输对象 (DTO)
│   ├── repository/         # 仓库实现
│   └── source/             # 数据源（本地/远程）
│       ├── local/          # 本地数据源
│       └── remote/         # 远程 API
│
├── domain/                  # 领域层（业务逻辑）
│   ├── model/              # 领域模型
│   ├── repository/         # 仓库接口
│   └── usecase/            # 用例（业务用例）
│
├── presentation/            # 表现层
│   ├── home/               # 首页模块
│   │   ├── HomeScreen.kt   # UI 组件
│   │   └── HomeViewModel.kt # 视图模型
│   ├── hot/                # 热门模块
│   │   ├── HotScreen.kt
│   │   └── HotViewModel.kt
│   ├── profile/            # 个人中心模块
│   │   ├── ProfileScreen.kt
│   │   └── ProfileViewModel.kt
│   └── common/             # 通用 UI 组件
│
└── di/                      # 依赖注入
    └── AppContainer.kt     # 依赖容器
```

## 核心概念

### 1. Presentation Layer（表现层）

**职责：** 处理 UI 渲染和用户交互

- **Screen**: Composable 函数，负责 UI 渲染
- **ViewModel**: 管理 UI 状态，处理业务逻辑调用
- **UiState**: 密封类，表示 UI 的不同状态（Loading, Success, Error）

**示例：**
```kotlin
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val articles: List<Article>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
```

### 2. Domain Layer（领域层）

**职责：** 包含业务逻辑和业务规则

- **Use Case**: 封装单一业务用例
- **Domain Model**: 业务实体
- **Repository Interface**: 定义数据访问契约

**优势：**
- 业务逻辑与 UI 和数据层解耦
- 可测试性强
- 可复用性高

### 3. Data Layer（数据层）

**职责：** 数据获取和持久化

- **Repository Implementation**: 实现领域层定义的接口
- **Data Source**: 具体的数据来源（API、数据库等）
- **DTO**: 数据传输对象

## 数据流

```
User Action
    ↓
Screen (Composable)
    ↓
ViewModel
    ↓
Use Case
    ↓
Repository Interface
    ↓
Repository Implementation
    ↓
Data Source (API/DB)
    ↓
Repository Implementation
    ↓
Use Case
    ↓
ViewModel (Update UiState)
    ↓
Screen (Recompose)
```

## 模块说明

### Home Module（首页）
- 展示推荐文章列表
- 支持下拉刷新和加载更多
- 使用 `GetRecommendedArticlesUseCase`

### Hot Module（热门）
- 展示热门话题列表
- 支持下拉刷新和加载更多
- 使用 `GetHotTopicsUseCase`

### Profile Module（个人中心）
- 展示用户资料和统计信息
- 支持下拉刷新
- 使用 `GetUserProfileUseCase`

## 依赖注入

使用简单的手动依赖注入（AppContainer），适合中小型项目。

**优点：**
- 无需额外依赖库
- 代码简单易懂
- 完全类型安全

**扩展建议：**
对于大型项目，可以考虑使用 Koin 或 Kodein 等 DI 框架。

## 状态管理

使用 Kotlin Flow 和 StateFlow 进行响应式状态管理：

```kotlin
private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
val uiState: StateFlow<UiState> = _uiState.asStateFlow()
```

**优势：**
- 类型安全
- 生命周期感知
- 支持多平台

## 最佳实践

### 1. 单一职责原则
每个类只负责一个功能

### 2. 依赖倒置原则
高层模块不依赖低层模块，都依赖抽象

### 3. 关注点分离
UI、业务逻辑、数据访问分离

### 4. 可测试性
- ViewModel 可以独立测试
- Use Case 可以独立测试
- Repository 可以 Mock

### 5. 错误处理
使用 Kotlin 的 Result 类型进行错误处理

## 扩展建议

### 1. 添加真实 API
替换 Mock 数据为真实的网络请求（使用 Ktor）

### 2. 添加本地缓存
使用 SQLDelight 或 Room 实现离线支持

### 3. 添加导航
使用 Compose Navigation 实现页面导航

### 4. 添加测试
- 单元测试（Use Case, ViewModel）
- UI 测试（Screen）

### 5. 性能优化
- 图片加载（Coil）
- 分页加载
- 缓存策略

## 技术栈

- **UI**: Compose Multiplatform
- **架构**: MVVM
- **异步**: Kotlin Coroutines + Flow
- **依赖注入**: 手动 DI（AppContainer）
- **数据**: Mock 数据（可扩展为 Ktor + SQLDelight）

## 总结

这个架构提供了：
- ✅ 清晰的代码组织
- ✅ 高可测试性
- ✅ 易于维护和扩展
- ✅ 符合 SOLID 原则
- ✅ 适合团队协作
