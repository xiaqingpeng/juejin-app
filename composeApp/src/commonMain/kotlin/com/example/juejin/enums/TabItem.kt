package com.example.juejin.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import juejin.composeapp.generated.resources.Res
import juejin.composeapp.generated.resources.tab_courses
import juejin.composeapp.generated.resources.tab_discover
import juejin.composeapp.generated.resources.tab_home
import juejin.composeapp.generated.resources.tab_hot
import juejin.composeapp.generated.resources.tab_profile
import org.jetbrains.compose.resources.StringResource

enum class TabItem(
    val title: StringResource,
    val icon: ImageVector
) {
    Home(Res.string.tab_home, Icons.Filled.Home),
    Hot(Res.string.tab_hot, Icons.Filled.Fireplace),
    Discover(Res.string.tab_discover, Icons.Filled.Explore),
    Courses(Res.string.tab_courses, Icons.Filled.Book),
    Profile(Res.string.tab_profile, Icons.Filled.Person)
}