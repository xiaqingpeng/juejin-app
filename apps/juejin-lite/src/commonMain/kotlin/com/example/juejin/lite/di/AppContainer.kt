package com.example.juejin.lite.di

import com.example.juejin.lite.data.repository.ArticleRepositoryImpl
import com.example.juejin.lite.data.repository.CartRepositoryImpl
import com.example.juejin.lite.data.repository.CategoryRepositoryImpl
import com.example.juejin.lite.data.repository.MessageRepositoryImpl
import com.example.juejin.lite.data.repository.UserRepositoryImpl
import com.example.juejin.lite.domain.repository.ArticleRepository
import com.example.juejin.lite.domain.repository.CartRepository
import com.example.juejin.lite.domain.repository.CategoryRepository
import com.example.juejin.lite.domain.repository.MessageRepository
import com.example.juejin.lite.domain.repository.UserRepository
import com.example.juejin.lite.domain.usecase.GetCartItemsUseCase
import com.example.juejin.lite.domain.usecase.GetCategoriesUseCase
import com.example.juejin.lite.domain.usecase.GetMessagesUseCase
import com.example.juejin.lite.domain.usecase.GetRecommendedArticlesUseCase
import com.example.juejin.lite.domain.usecase.GetUserProfileUseCase
import com.example.juejin.lite.presentation.cart.CartViewModel
import com.example.juejin.lite.presentation.category.CategoryViewModel
import com.example.juejin.lite.presentation.home.HomeViewModel
import com.example.juejin.lite.presentation.message.MessageViewModel
import com.example.juejin.lite.presentation.profile.ProfileViewModel

/**
 * 应用依赖容器
 * 简单的手动依赖注入实现
 * 
 * 支持 5 个核心模块：
 * 1. 首页 - 推荐文章
 * 2. 分类 - 文章分类浏览
 * 3. 消息 - 系统消息管理
 * 4. 购物车 - 商品管理
 * 5. 个人中心 - 用户资料
 */
class AppContainer {
    
    // ==================== Repositories ====================
    
    private val articleRepository: ArticleRepository by lazy {
        ArticleRepositoryImpl()
    }
    
    private val userRepository: UserRepository by lazy {
        UserRepositoryImpl()
    }
    
    private val categoryRepository: CategoryRepository by lazy {
        CategoryRepositoryImpl()
    }
    
    private val messageRepository: MessageRepository by lazy {
        MessageRepositoryImpl()
    }
    
    private val cartRepository: CartRepository by lazy {
        CartRepositoryImpl()
    }
    
    // ==================== Use Cases ====================
    
    private val getRecommendedArticlesUseCase: GetRecommendedArticlesUseCase by lazy {
        GetRecommendedArticlesUseCase(articleRepository)
    }
    
    private val getUserProfileUseCase: GetUserProfileUseCase by lazy {
        GetUserProfileUseCase(userRepository)
    }
    
    private val getCategoriesUseCase: GetCategoriesUseCase by lazy {
        GetCategoriesUseCase(categoryRepository)
    }
    
    private val getMessagesUseCase: GetMessagesUseCase by lazy {
        GetMessagesUseCase(messageRepository)
    }
    
    private val getCartItemsUseCase: GetCartItemsUseCase by lazy {
        GetCartItemsUseCase(cartRepository)
    }
    
    // ==================== ViewModels ====================
    
    fun provideHomeViewModel(): HomeViewModel {
        return HomeViewModel(getRecommendedArticlesUseCase)
    }
    
    fun provideProfileViewModel(): ProfileViewModel {
        return ProfileViewModel(getUserProfileUseCase)
    }
    
    fun provideCategoryViewModel(): CategoryViewModel {
        return CategoryViewModel(getCategoriesUseCase, categoryRepository)
    }
    
    fun provideMessageViewModel(): MessageViewModel {
        return MessageViewModel(getMessagesUseCase, messageRepository)
    }
    
    fun provideCartViewModel(): CartViewModel {
        return CartViewModel(getCartItemsUseCase, cartRepository)
    }
    
    companion object {
        private var instance: AppContainer? = null
        
        fun getInstance(): AppContainer {
            return instance ?: synchronized(this) {
                instance ?: AppContainer().also { instance = it }
            }
        }
    }
}
