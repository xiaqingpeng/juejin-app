package com.example.juejin.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.juejin.model.Article
import com.example.juejin.model.Circle
import com.example.juejin.model.DiscoverModule

class DiscoverViewModel : ViewModel() {
    private val _modules = MutableStateFlow(emptyList<DiscoverModule>())
    val modules: StateFlow<List<DiscoverModule>> = _modules.asStateFlow()

    private val _articles = MutableStateFlow(emptyList<Article>())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    private val _circles = MutableStateFlow(emptyList<Circle>())
    val circles: StateFlow<List<Circle>> = _circles.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _modules.value = listOf(
                DiscoverModule("1", "职场锦囊", Icons.Default.Work, "hot"),
                DiscoverModule("2", "行业速递", Icons.AutoMirrored.Filled.TrendingUp, "hot"),
                DiscoverModule("3", "掘金一周", Icons.Default.DateRange, "hot"),
                DiscoverModule("4", "高校精选", Icons.Default.School, "hot"),
                DiscoverModule("5", "直播", Icons.Default.LiveTv, "column"),
                DiscoverModule("6", "专栏", Icons.Default.Book, "column"),
                DiscoverModule("7", "收藏集", Icons.Default.Collections, "column"),
                DiscoverModule("8", "文章榜", Icons.AutoMirrored.Filled.Article, "column"),
                DiscoverModule("9", "作者榜", Icons.Default.Person, "column")
            )

            _articles.value = listOf(
                Article(
                    id = "1",
                    title = "掘金社区 MCP 上线、Claude 4 与 Gemini 2.5 正面交锋...",
                    author = "掘金官方",
                    summary = "本周技术圈热点事件汇总，包括 MCP 上线、AI 模型对比等...",
                    publishTime = "2024-03-23",
                    likeCount = 12000,
                    commentCount = 320
                )
            )

            _circles.value = listOf(
                Circle(
                    id = "1",
                    name = "AICoding交流",
                    memberCount = 1000,
                    hotCount = 2000,
                    description = "AI 编程技术交流圈",
                    avatar = "ic_ai_circle"
                )
            )
        }
    }
}