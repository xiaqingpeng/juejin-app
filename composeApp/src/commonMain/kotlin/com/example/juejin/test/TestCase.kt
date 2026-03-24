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
    
    fun register(testCase: TestCase) {
        testCases.add(testCase)
    }
    
    fun getAll(): List<TestCase> = testCases.toList()
    
    fun getById(id: String): TestCase? = testCases.find { it.id == id }
}
