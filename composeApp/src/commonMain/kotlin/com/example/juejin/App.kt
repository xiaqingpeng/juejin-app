package com.example.juejin

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch

// Tab items definition
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



@Composable
fun App() {
    MaterialTheme {
        val tabs = listOf(
            TabItem.Home,
            TabItem.Hot,
            TabItem.Discover,
            TabItem.Courses,
            TabItem.Profile
        )

        // Pager state with proper initialization
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            tabs.size
        }

        val coroutineScope = rememberCoroutineScope()

        // Scaffold provides proper layout structure
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                // Bottom Navigation Bar
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val isSelected = pagerState.currentPage == index
                        NavigationBarItem(
                            icon = {
                                // Use Material Icons with dynamic coloring
                                val iconColor = if (isSelected) Color(0xFF1890FF) else Color(0xFF808080)
                                androidx.compose.material3.Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    tint = iconColor
                                )
                            },
                            label = { Text(tab.title) },
                            selected = isSelected,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF1890FF), // Blue color for selected items
                                selectedTextColor = Color(0xFF1890FF),
                                unselectedIconColor = Color(0xFF808080), // Gray color for unselected items
                                unselectedTextColor = Color(0xFF808080),
                                indicatorColor = Color.Transparent // No indicator line
                            )
                        )
                    }
                }
            }
        ) {
            // Content padding from Scaffold
            val padding = it

            // Horizontal Pager with gesture support
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
                    // Add additional drag gesture support for better UX
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures {_, dragAmount ->
                                        coroutineScope.launch {
                                            // Use scrollToPage with threshold for better compatibility
                                            if (kotlin.math.abs(dragAmount) > size.width * 0.25) {
                                                val targetPage = (pagerState.currentPage + (if (dragAmount < 0) 1 else -1)).coerceIn(0, tabs.size - 1)
                                                pagerState.scrollToPage(targetPage)
                                            }
                                        }
                                    }
                    }
            ) {
                // Content for each tab
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (tabs[pagerState.currentPage]) {
                        TabItem.Home -> HomeScreen()
                        TabItem.Hot -> HotScreen()
                        TabItem.Discover -> DiscoverScreen()
                        TabItem.Courses -> CoursesScreen()
                        TabItem.Profile -> ProfileScreen()
                    }
                }
            }
        }
    }
}

// Individual screen components for each tab
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFF1890FF), shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Text("🏠", fontSize = MaterialTheme.typography.headlineLarge.fontSize)
        }
        Text("首页", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun HotScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFF1890FF), shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Text("🔥", fontSize = MaterialTheme.typography.headlineLarge.fontSize)
        }
        Text("沸点", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun DiscoverScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFF1890FF), shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Text("🔍", fontSize = MaterialTheme.typography.headlineLarge.fontSize)
        }
        Text("发现", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun CoursesScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFF1890FF), shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Text("📚", fontSize = MaterialTheme.typography.headlineLarge.fontSize)
        }
        Text("课程", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFF1890FF), shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Text("👤", fontSize = MaterialTheme.typography.headlineLarge.fontSize)
        }
        Text("我的", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp))
    }
}