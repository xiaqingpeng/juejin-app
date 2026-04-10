package com.example.juejin.theme

import androidx.compose.runtime.Composable
import platform.UIKit.UIScreen
import platform.UIKit.UITraitCollection
import platform.UIKit.UIUserInterfaceStyle

@Composable
actual fun isSystemInDarkTheme(): Boolean {
    val traitCollection = UIScreen.mainScreen.traitCollection
    return traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
}
