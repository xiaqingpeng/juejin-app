package com.example.juejin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.juejin.model.Hot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// HotViewModel.kt
class HotViewModel : ViewModel() {
    private val _hots = MutableStateFlow(emptyList<Hot>())
    val hots: StateFlow<List<Hot>> = _hots.asStateFlow()

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _hots.value = listOf(
                Hot(
                    id = "1",
                    author = "潜龙勿用之化骨龙",
                    avatar = "avatar1",
                    title = "",
                    content = "线程池解决的是“资源问题”，协程解决的是“表达问题”",
                    time = "4分钟前",
                    likeCount = 17,
                    commentCount = 1,
                    shareCount = 1
                ),
                Hot(
                    id = "2",
                    author = "北子龙",
                    avatar = "avatar2",
                    title = "",
                    content = "开心时大笑，困惑时静思",
                    time = "4分钟前",
                    likeCount = 17,
                    commentCount = 1,
                    shareCount = 1
                ),
                Hot(
                    id = "3",
                    author = "塔玛德二等饼干",
                    avatar = "avatar3",
                    title = "",
                    content = "ai对软件行业冲击还挺大的，出门找工作都很焦虑",
                    time = "33分钟前",
                    likeCount = 90,
                    commentCount = 30,
                    shareCount = 5
                ),
                Hot(
                    id = "4",
                    author = "比考试难考的地瓜",
                    avatar = "avatar4",
                    title = "",
                    content = "杭州太子湾赏花",
                    time = "42分钟前",
                    likeCount = 120,
                    commentCount = 20,
                    shareCount = 10,
                    images = listOf("img1", "img2", "img3","img1", "img2", "img3")
                ),
                Hot(
                    id = "5",
                    author = "潜龙勿用之化骨龙",
                    avatar = "avatar1",
                    title = "",
                    content = "线程池解决的是“资源问题”，协程解决的是“表达问题”",
                    time = "4分钟前",
                    likeCount = 17,
                    commentCount = 1,
                    shareCount = 1
                ),
                Hot(
                    id = "6",
                    author = "北子龙",
                    avatar = "avatar2",
                    title = "",
                    content = "开心时大笑，困惑时静思",
                    time = "4分钟前",
                    likeCount = 17,
                    commentCount = 1,
                    shareCount = 1
                ),
                Hot(
                    id = "7",
                    author = "塔玛德二等饼干",
                    avatar = "avatar3",
                    title = "",
                    content = "ai对软件行业冲击还挺大的，出门找工作都很焦虑",
                    time = "33分钟前",
                    likeCount = 90,
                    commentCount = 30,
                    shareCount = 5
                ),
                Hot(
                    id = "8",
                    author = "比考试难考的地瓜",
                    avatar = "avatar4",
                    title = "",
                    content = "杭州太子湾赏花",
                    time = "42分钟前",
                    likeCount = 120,
                    commentCount = 20,
                    shareCount = 10,
                    images = listOf("img1", "img2", "img3","img1", "img2", "img3")
                ),
            )
        }
    }
}

