package com.example.juejin.navigation

import kotlinx.serialization.Serializable

/**
 * 导航路由定义
 * 使用 type-safe navigation 方式
 */
sealed interface NavRoutes {
    @Serializable
    data object Main : NavRoutes
    
    @Serializable
    data object Settings : NavRoutes
    
    @Serializable
    data class SettingDetail(val settingTitle: String) : NavRoutes
    
    @Serializable
    data object EditProfile : NavRoutes
    
    @Serializable
    data object QrScanner : NavRoutes
    
    @Serializable
    data object TestList : NavRoutes
    
    @Serializable
    data class TestDetail(val testId: String) : NavRoutes
    
    @Serializable
    data object CourseList : NavRoutes
    
    @Serializable
    data class CourseDetail(val courseId: String) : NavRoutes
}
