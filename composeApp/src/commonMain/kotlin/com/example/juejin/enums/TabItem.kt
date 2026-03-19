package com.example.juejin.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class TabItem(
    val title: String,
    val icon: ImageVector
) {
    Home("首页", Icons.Filled.Home),
    Hot("沸点", Icons.Filled.Fireplace),
    Discover("发现", Icons.Filled.Explore),
    Courses("课程", Icons.Filled.Book),
    Profile("我的", Icons.Filled.Person)
}