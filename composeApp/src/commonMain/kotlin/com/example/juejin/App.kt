package com.example.juejin

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.juejin.ui.Colors
import com.example.juejin.enums.TabItem
import com.example.juejin.screen.CoursesScreen
import com.example.juejin.screen.DiscoverScreen
import com.example.juejin.screen.HomeScreen
import com.example.juejin.screen.HotScreen
import com.example.juejin.screen.ProfileScreen
import kotlinx.coroutines.launch


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
                    containerColor = Colors.white,
                    tonalElevation = 8.dp
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val isSelected = pagerState.currentPage == index
                        NavigationBarItem(
                            icon = {
                                // Use Material Icons with dynamic coloring
                                val iconColor = if (isSelected) Colors.primaryBlue else Colors.unselectedGray
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
                                selectedIconColor = Colors.primaryBlue, // Blue color for selected items
                                selectedTextColor = Colors.primaryBlue,
                                unselectedIconColor = Colors.unselectedGray, // Gray color for unselected items
                                unselectedTextColor = Colors.unselectedGray,
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





