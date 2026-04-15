# Juejin Lite - 快速参考指南

## 🎯 5 个核心模块

| 模块 | ViewModel | Screen | Repository | UseCase | Model |
|------|-----------|--------|------------|---------|-------|
| 首页 | HomeViewModel | HomeScreen | ArticleRepository | GetRecommendedArticlesUseCase | Article |
| 分类 | CategoryViewModel | CategoryScreen | CategoryRepository | GetCategoriesUseCase | Category |
| 消息 | MessageViewModel | MessageScreen | MessageRepository | GetMessagesUseCase | Message |
| 购物车 | CartViewModel | CartScreen | CartRepository | GetCartItemsUseCase | CartItem |
| 个人中心 | ProfileViewModel | ProfileScreen | UserRepository | GetUserProfileUseCase | UserProfile |

## 📦 组件清单

### Domain Layer (15 个)

**领域模型 (5)**
```kotlin
Article.kt          // 文章模型
Category.kt         // 分类模型
Message.kt          // 消息模型
CartItem.kt         // 购物车商品模型
UserProfile.kt      // 用户资料模型
```

**仓库接口 (5)**
```kotlin
ArticleRepository.kt    // 文章仓库接口
CategoryRepository.kt   // 分类仓库接口
MessageRepository.kt    // 消息仓库接口
CartRepository.kt       // 购物车仓库接口
UserRepository.kt       // 用户仓库接口
```

**用例 (5)**
```kotlin
GetRecommendedArticlesUseCase.kt  // 获取推荐文章
GetCategoriesUseCase.kt           // 获取分类列表
GetMessagesUseCase.kt             // 获取消息列表
GetCartItemsUseCase.kt            // 获取购物车
GetUserProfileUseCase.kt          // 获取用户资料
```

### Data Layer (5 个)

**仓库实现 (5)**
```kotlin
ArticleRepositoryImpl.kt    // 文章仓库实现 (Mock)
CategoryRepositoryImpl.kt   // 分类仓库实现 (Mock)
MessageRepositoryImpl.kt    // 消息仓库实现 (Mock)
CartRepositoryImpl.kt       // 购物车仓库实现 (Mock)
UserRepositoryImpl.kt       // 用户仓库实现 (Mock)
```

### Presentation Layer (10 个)

**ViewModel (5)**
```kotlin
HomeViewModel.kt        // 首页视图模型
CategoryViewModel.kt    // 分类视图模型
MessageViewModel.kt     // 消息视图模型
CartViewModel.kt        // 购物车视图模型
ProfileViewModel.kt     // 个人中心视图模型
```

**Screen (5)**
```kotlin
HomeScreen.kt          // 首页界面
CategoryScreen.kt      // 分类界面
MessageScreen.kt       // 消息界面
CartScreen.kt          // 购物车界面
ProfileScreen.kt       // 个人中心界面
```

### DI Layer (1 个)

```kotlin
AppContainer.kt        // 依赖注入容器
```

## 🔧 如何添加新模块

### 步骤 1: 创建领域模型
```kotlin
// domain/model/YourModel.kt
data class YourModel(
    val id: String,
    val name: String
)
```

### 步骤 2: 创建仓库接口
```kotlin
// domain/repository/YourRepository.kt
interface YourRepository {
    suspend fun getData(): Result<List<YourModel>>
}
```

### 步骤 3: 创建用例
```kotlin
// domain/usecase/GetYourDataUseCase.kt
class GetYourDataUseCase(
    private val repository: YourRepository
) {
    suspend operator fun invoke(): Result<List<YourModel>> {
        return repository.getData()
    }
}
```

### 步骤 4: 实现仓库
```kotlin
// data/repository/YourRepositoryImpl.kt
class YourRepositoryImpl : YourRepository {
    override suspend fun getData(): Result<List<YourModel>> {
        // Mock 数据或 API 调用
        return Result.success(listOf())
    }
}
```

### 步骤 5: 创建 ViewModel
```kotlin
// presentation/your/YourViewModel.kt
class YourViewModel(
    private val getYourDataUseCase: GetYourDataUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<YourUiState>(YourUiState.Loading)
    val uiState: StateFlow<YourUiState> = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    fun loadData() {
        viewModelScope.launch {
            _uiState.value = YourUiState.Loading
            getYourDataUseCase()
                .onSuccess { data ->
                    _uiState.value = YourUiState.Success(data)
                }
                .onFailure { error ->
                    _uiState.value = YourUiState.Error(error.message ?: "加载失败")
                }
        }
    }
}

sealed class YourUiState {
    object Loading : YourUiState()
    data class Success(val data: List<YourModel>) : YourUiState()
    data class Error(val message: String) : YourUiState()
}
```

### 步骤 6: 创建 Screen
```kotlin
// presentation/your/YourScreen.kt
@Composable
fun YourScreen(viewModel: YourViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (val state = uiState) {
        is YourUiState.Loading -> LoadingView()
        is YourUiState.Success -> SuccessView(state.data)
        is YourUiState.Error -> ErrorView(state.message)
    }
}
```

### 步骤 7: 更新 AppContainer
```kotlin
// di/AppContainer.kt
class AppContainer {
    private val yourRepository: YourRepository by lazy {
        YourRepositoryImpl()
    }
    
    private val getYourDataUseCase: GetYourDataUseCase by lazy {
        GetYourDataUseCase(yourRepository)
    }
    
    fun provideYourViewModel(): YourViewModel {
        return YourViewModel(getYourDataUseCase)
    }
}
```

### 步骤 8: 在 App.kt 中使用
```kotlin
val yourViewModel = remember { appContainer.provideYourViewModel() }

// 在适当的地方显示
YourScreen(viewModel = yourViewModel)
```

## 🎨 UI 状态模式

所有 ViewModel 都遵循相同的状态模式：

```kotlin
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: T) : UiState()
    data class Error(val message: String) : UiState()
}
```

## 🔄 常用操作

### 加载数据
```kotlin
fun loadData() {
    viewModelScope.launch {
        _uiState.value = UiState.Loading
        useCase()
            .onSuccess { data ->
                _uiState.value = UiState.Success(data)
            }
            .onFailure { error ->
                _uiState.value = UiState.Error(error.message ?: "加载失败")
            }
    }
}
```

### 刷新数据
```kotlin
fun refresh() {
    currentPage = 0
    items.clear()
    loadData()
}
```

### 加载更多
```kotlin
fun loadMore() {
    viewModelScope.launch {
        _uiState.value = UiState.Success(items, isLoadingMore = true)
        useCase(currentPage)
            .onSuccess { newItems ->
                items.addAll(newItems)
                currentPage++
                _uiState.value = UiState.Success(items, isLoadingMore = false)
            }
    }
}
```

## 📚 资源文件

### 字符串资源位置
```
apps/juejin-lite/src/commonMain/composeResources/values/strings.xml
```

### 添加新字符串
```xml
<string name="your_key">你的文本</string>
```

### 使用字符串
```kotlin
import juejin.lite.generated.resources.Res
import juejin.lite.generated.resources.your_key
import org.jetbrains.compose.resources.stringResource

Text(stringResource(Res.string.your_key))
```

## 🐛 调试技巧

### 1. 查看状态流
```kotlin
LaunchedEffect(uiState) {
    println("UI State: $uiState")
}
```

### 2. 模拟延迟
```kotlin
kotlinx.coroutines.delay(500) // 模拟网络延迟
```

### 3. 打印日志
```kotlin
println("[YourViewModel] Loading data...")
```

## 📖 相关文档

- [ARCHITECTURE_SUMMARY.md](./ARCHITECTURE_SUMMARY.md) - 架构总结
- [MVVM_IMPLEMENTATION.md](./MVVM_IMPLEMENTATION.md) - 详细实现说明
- [ARCHITECTURE.md](./ARCHITECTURE.md) - 原始架构文档

## 🚀 快速开始

1. 查看 `AppContainer.kt` 了解依赖注入
2. 查看任一 ViewModel 了解状态管理
3. 查看任一 Screen 了解 UI 实现
4. 参考本文档添加新模块

祝你开发愉快！🎉
