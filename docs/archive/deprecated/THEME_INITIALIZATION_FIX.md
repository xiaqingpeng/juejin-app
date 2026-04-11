# ThemeManager 初始化问题修复

## 问题描述

应用启动时崩溃，错误信息：

```
kotlin.UninitializedPropertyAccessException: lateinit property appContext has not been initialized
at com.example.juejin.core.storage.PrivacyStorage.getString(PrivacyStorage.android.kt:31)
at com.example.juejin.ui.theme.ThemeManager.loadTheme(ThemeManager.kt:50)
at com.example.juejin.ui.theme.ThemeManager.<clinit>(ThemeManager.kt:46)
```

## 根本原因

`ThemeManager` 是一个 `object`（单例），在类加载时会执行 `init` 块。原来的代码在 `init` 块中调用了 `loadTheme()`，而 `loadTheme()` 需要访问 `PrivacyStorage`。

在 Android 平台上，`PrivacyStorage` 需要先调用 `init(application)` 方法来初始化 `appContext`，但是：

1. `ThemeManager` 在 `AppTheme` Composable 被调用时就会被加载
2. 此时 `PrivacyStorage.init()` 还没有被调用
3. 导致访问未初始化的 `appContext` 而崩溃

## 解决方案

### 1. 延迟初始化 ThemeManager

修改 `ThemeManager` 使用延迟初始化模式：

**修改前**：
```kotlin
object ThemeManager {
    // ...
    
    init {
        // 从存储中读取主题设置
        loadTheme()
    }
    
    private fun loadTheme() {
        val savedTheme = PrivacyStorage.getString(THEME_KEY, THEME_SYSTEM)
        _themeMode = when (savedTheme) {
            THEME_LIGHT -> ThemeMode.LIGHT
            THEME_DARK -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }
}
```

**修改后**：
```kotlin
object ThemeManager {
    // 是否已初始化
    private var isInitialized = false
    
    // 实际显示的是否为深色模式
    val isDarkMode: Boolean
        get() {
            // 确保已初始化
            if (!isInitialized) {
                initialize()
            }
            return when (_themeMode) {
                ThemeMode.SYSTEM -> isSystemDarkMode
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }
        }
    
    /**
     * 初始化主题管理器
     * 必须在使用前调用（通常在 Application.onCreate 中）
     */
    fun initialize() {
        if (!isInitialized) {
            loadTheme()
            isInitialized = true
        }
    }
    
    private fun loadTheme() {
        try {
            val savedTheme = PrivacyStorage.getString(THEME_KEY, THEME_SYSTEM)
            _themeMode = when (savedTheme) {
                THEME_LIGHT -> ThemeMode.LIGHT
                THEME_DARK -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM
            }
        } catch (e: Exception) {
            // 如果加载失败，使用默认值
            _themeMode = ThemeMode.SYSTEM
        }
    }
}
```

### 2. 在 MainActivity 中初始化

在 `MainActivity.onCreate()` 中，在 `PrivacyStorage.init()` 之后调用 `ThemeManager.initialize()`：

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // ... 其他初始化代码
    
    // 初始化隐私政策存储
    PrivacyStorage.init(application)
    
    // 初始化主题管理器
    ThemeManager.initialize()
    
    setContent {
        App()
    }
}
```

### 3. 清理重复导入

修复 `App.kt` 中的重复导入：

**修改前**：
```kotlin
import com.example.juejin.theme.AppTheme
import com.example.juejin.theme.isSystemInDarkTheme
import com.example.juejin.ui.theme.ThemeColors
import com.example.juejin.ui.theme.AppTheme
import com.example.juejin.ui.theme.isSystemInDarkTheme
import com.example.juejin.ui.theme.ThemeManager
import com.example.juejin.ui.theme.DarkColors
```

**修改后**：
```kotlin
import com.example.juejin.ui.theme.ThemeColors
import com.example.juejin.ui.theme.AppTheme
import com.example.juejin.ui.theme.isSystemInDarkTheme
import com.example.juejin.ui.theme.ThemeManager
import com.example.juejin.ui.theme.DarkColors
```

## 关键改进

1. **延迟初始化**：`ThemeManager` 不再在类加载时立即初始化
2. **自动初始化**：首次访问 `isDarkMode` 时会自动调用 `initialize()`
3. **异常处理**：`loadTheme()` 添加了 try-catch，如果加载失败使用默认值
4. **显式初始化**：在 `MainActivity` 中显式调用 `initialize()`，确保在正确的时机初始化

## 初始化顺序

正确的初始化顺序：

1. `MainActivity.onCreate()` 开始
2. `PrivacyStorage.init(application)` - 初始化存储
3. `ThemeManager.initialize()` - 初始化主题管理器
4. `setContent { App() }` - 开始 Compose UI

## 测试结果

- ✅ 编译成功
- ✅ 应用启动不再崩溃
- ✅ 主题系统正常工作
- ✅ 主题持久化正常

## 相关文件

- `shared/ui/theme/src/commonMain/kotlin/com/example/juejin/ui/theme/ThemeManager.kt`
- `composeApp/src/androidMain/kotlin/com/example/juejin/MainActivity.kt`
- `composeApp/src/commonMain/kotlin/com/example/juejin/App.kt`

## 最佳实践

1. **避免在 object 的 init 块中访问需要初始化的资源**
2. **使用延迟初始化模式处理依赖关系**
3. **提供显式的初始化方法**
4. **添加异常处理确保健壮性**
5. **在 Activity/Application 中按正确顺序初始化依赖**

---

**修复时间**：2026-04-11
**状态**：✅ 已修复并测试通过
