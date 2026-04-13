# Juejin Lite - 完整 MVVM 架构实现

## 项目概述

这是一个完整的大型商业项目 MVVM 架构实现，包含 5 个核心模块：
- 首页（推荐文章）
- 分类（文章分类浏览）
- 消息（系统消息、点赞、评论、关注）
- 购物车（商品管理、结算）
- 个人中心（用户资料）

## 架构层次

```
┌─────────────────────────────────────────┐
│    Presentation Layer (UI + ViewModel)  │
│  - HomeScreen + HomeViewModel           │
│  - CategoryScreen + CategoryViewModel   │
│  - MessageScreen + MessageViewModel     │
│  - CartScreen + CartViewModel           │
│  - ProfileScreen + ProfileViewModel     │
├─────────────────────────────────────────┤
│         Domain Layer (Business Logic)   │
│  - Use Cases                            │
│  - Domain Models                        │
│  - Repository Interfaces                │
├─────────────────────────────────────────┤
│         Data Layer (Data Access)        │
│  - Repository Implementations           │
│  - Mock Data Sources                    │
└─────────────────────────────────────────┘
```

## 已实现的功能

### 1. 首页模块
- ✅ 推荐文章列表
- ✅ 下拉刷新
- ✅ 上拉加载更多
- ✅ 文章详情展示

### 2. 分类模块
- ✅ 分类列表展示
- ✅ 按分类浏览文章
- ✅ 分类文章数量统计
- ✅ 下拉刷新和加载更多

### 3. 消息模块
- ✅ 多类型消息（系统、点赞、评论、关注）
- ✅ 未读消息标记
- ✅ 标记已读功能
- ✅ 删除消息功能
- ✅ 消息分页加载

### 4. 购物车模块
- ✅ 商品列表展示
- ✅ 商品选择/取消选择
- ✅ 全选/取消全选
- ✅ 数量增减
- ✅ 删除商品
- ✅ 价格汇总
- ✅ 结算功能

### 5. 个人中心模块
- ✅ 用户资料展示
- ✅ 统计信息（关注、粉丝、文章、获赞）
- ✅ 等级展示
- ✅ 下拉刷新

## 文件结构

```
apps/juejin-lite/src/commonMain/kotlin/com/example/juejin/lite/
├── data/
│   └── repository/
│       ├── ArticleRepositoryImpl.kt
│       ├── CartRepositoryImpl.kt
│       ├── CategoryRepositoryImpl.kt
│       ├── MessageRepositoryImpl.kt
│       └── UserRepositoryImpl.kt
│
├── domain/
│   ├── model/
│   │   ├── Article.kt
│   │   ├── CartItem.kt
│   │   ├── Category.kt
│   │   ├── Message.kt
│   │   └── UserProfile.kt
│   ├── repository/
│   │   ├── ArticleRepository.kt
│   │   ├── CartRepository.kt
│   │   ├── CategoryRepository.kt
│   │   ├── MessageRepository.kt
│   │   └── UserRepository.kt
│   └── usecase/
│       ├── GetCartItemsUseCase.kt
│       ├── GetCategoriesUseCase.kt
│       ├── GetMessagesUseCase.kt
│       ├── GetRecommendedArticlesUseCase.kt
│       └── GetUserProfileUseCase.kt
│
├── presentation/
│   ├── cart/
│   │   ├── CartScreen.kt
│   │   └── CartViewModel.kt
│   ├── category/
│   │   ├── CategoryScreen.kt
│   │   └── CategoryViewModel.kt
│   ├── home/
│   │   ├── HomeScreen.kt
│   │   └── HomeViewModel.kt
│   ├── message/
│   │   ├── MessageScreen.kt
│   │   └── MessageViewModel.kt
│   └── profile/
│       ├── ProfileScreen.kt
│       └── ProfileViewModel.kt
│
├── di/
│   └── AppContainer.kt
│
└── App.kt
```

## 核心技术

### 1. 状态管理
使用 Kotlin StateFlow 进行响应式状态管理：

```kotlin
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<T>) : UiState()
    data class Error(val message: String) : UiState()
}
```

### 2. 依赖注入
使用手动 DI（AppContainer）：

```kotlin
class AppContainer {
    private val repository by lazy { RepositoryImpl() }
    private val useCase by lazy { UseCase(repository) }
    
    fun provideViewModel(): ViewModel {
        return ViewModel(useCase)
    }
}
```

### 3. 数据流
```
User Action → Screen → ViewModel → Use Case → Repository → Data Source
                ↑                                              ↓
                └──────────── StateFlow Update ←──────────────┘
```

## 特色功能

### 1. 购物车管理
- 商品选择状态管理
- 实时价格计算
- 数量控制（增减、删除）
- 全选/取消全选

### 2. 消息系统
- 多类型消息支持
- 未读状态管理
- 消息操作（标记已读、删除）

### 3. 分类浏览
- 两级导航（分类列表 → 文章列表）
- 分类统计信息
- 动态内容加载

## 扩展建议

### 1. 网络层
```kotlin
// 使用 Ktor 替换 Mock 数据
class ArticleRepositoryImpl(
    private val apiService: ApiService
) : ArticleRepository {
    override suspend fun getArticles() = 
        apiService.fetchArticles()
}
```

### 2. 本地缓存
```kotlin
// 使用 SQLDelight 实现离线支持
class ArticleRepositoryImpl(
    private val apiService: ApiService,
    private val database: Database
) : ArticleRepository {
    override suspend fun getArticles() {
        // 先从缓存读取
        val cached = database.getArticles()
        if (cached.isNotEmpty()) return cached
        
        // 网络请求并缓存
        val remote = apiService.fetchArticles()
        database.saveArticles(remote)
        return remote
    }
}
```

### 3. 导航
```kotlin
// 使用 Compose Navigation
NavHost(navController) {
    composable("home") { HomeScreen() }
    composable("detail/{id}") { 
        ArticleDetailScreen(id = it.arguments?.getString("id"))
    }
}
```

### 4. 图片加载
```kotlin
// 使用 Coil
AsyncImage(
    model = article.coverImage,
    contentDescription = article.title
)
```

## 测试策略

### 1. 单元测试
```kotlin
class GetArticlesUseCaseTest {
    @Test
    fun `should return articles when repository succeeds`() = runTest {
        val repository = FakeArticleRepository()
        val useCase = GetArticlesUseCase(repository)
        
        val result = useCase()
        
        assertTrue(result.isSuccess)
    }
}
```

### 2. ViewModel 测试
```kotlin
class HomeViewModelTest {
    @Test
    fun `should update state to Success when articles loaded`() = runTest {
        val viewModel = HomeViewModel(fakeUseCase)
        
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success::class, awaitItem()::class)
        }
    }
}
```

## 性能优化

### 1. 列表优化
- 使用 LazyColumn 实现虚拟滚动
- 实现分页加载
- 避免过度重组

### 2. 状态管理优化
- 使用 StateFlow 而非 LiveData
- 合理使用 remember 和 derivedStateOf
- 避免不必要的状态更新

### 3. 内存优化
- 及时清理不用的资源
- 使用图片缓存
- 控制列表项数量

## 总结

这是一个完整的、生产级别的 MVVM 架构实现，包含：
- ✅ 5 个完整的功能模块（首页、分类、消息、购物车、个人中心）
- ✅ 5 个领域模型（Article, Category, Message, CartItem, UserProfile）
- ✅ 5 个仓库接口和实现
- ✅ 5 个用例
- ✅ 5 个 ViewModel 和 Screen
- ✅ 清晰的分层架构
- ✅ 响应式状态管理
- ✅ 依赖注入
- ✅ Mock 数据支持
- ✅ 可扩展的设计
- ✅ 符合 SOLID 原则

适合作为大型商业项目的基础架构！
