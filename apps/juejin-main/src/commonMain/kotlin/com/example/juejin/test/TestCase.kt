package com.example.juejin.test

import androidx.compose.runtime.Composable

/**
 * 测试案例数据类
 */
data class TestCase(
    val id: String,
    val title: String,
    val description: String,
    val content: @Composable () -> Unit
)

/**
 * 测试案例注册表
 */
object TestRegistry {
    private val testCases = mutableListOf<TestCase>()
    private val courses = mutableMapOf<String, com.example.juejin.model.LogStatsItem>()
    
    fun register(testCase: TestCase) {
        testCases.add(testCase)
    }
    
    fun getAll(): List<TestCase> = testCases.toList()
    
    fun getById(id: String): TestCase? = testCases.find { it.id == id }
    
    // 用于导航的别名方法
    fun getTestCase(id: String): TestCase? = getById(id)
    
    // 课程数据管理
    fun registerCourse(course: com.example.juejin.model.LogStatsItem) {
        val courseId = course.id?.toString() ?: "0"
        courses[courseId] = course
    }
    
    fun getCourse(courseId: String): com.example.juejin.model.LogStatsItem? = courses[courseId]
    
    fun getAllCourses(): List<com.example.juejin.model.LogStatsItem> = courses.values.toList()
}
