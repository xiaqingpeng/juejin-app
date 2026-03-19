package com.example.juejin.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle


/**
 * Centralized text style definitions for the application
 */
object Typographys {
    /**
     * Main screen title style used in tab screens
     */
    val screenTitle: TextStyle
        @Composable
        get() = MaterialTheme.typography.headlineMedium
    
    /**
     * Tab bar label style
     */
    val tabLabel: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyMedium
    
    /**
     * Large icon text style (used for emoji icons in screens)
     */
    val largeIconText: TextStyle
        @Composable
        get() = MaterialTheme.typography.headlineLarge
}



