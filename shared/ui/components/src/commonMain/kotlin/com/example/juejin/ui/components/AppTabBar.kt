package com.example.juejin.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.juejin.ui.theme.ThemeColors
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppTabBar(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = ThemeColors.primaryBlue
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = ThemeColors.Background.primary
    ) {
        NavigationBar(
            containerColor = ThemeColors.Background.primary,
            tonalElevation = 0.dp
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedIndex == index
                NavigationBarItem(
                    icon = {
                        val iconColor = if (isSelected) {
                            selectedColor
                        } else {
                            ThemeColors.Text.secondary
                        }
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = stringResource(tab.title),
                            tint = iconColor
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(tab.title),
                            color = if (isSelected) {
                                selectedColor
                            } else {
                                ThemeColors.Text.secondary
                            }
                        )
                    },
                    selected = isSelected,
                    onClick = { onTabSelected(index) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = selectedColor,
                        selectedTextColor = selectedColor,
                        unselectedIconColor = ThemeColors.Text.secondary,
                        unselectedTextColor = ThemeColors.Text.secondary,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
