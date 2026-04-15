package com.example.juejin.ui.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * 统一字体管理
 */
object Typography {
    
    // ==================== 字体大小常量 ====================
    
    object FontSizes {
        // 标题字体
        object Title {
            val large = 18.sp      // 页面主标题
            val medium = 16.sp     // 区块标题
            val small = 14.sp      // 小标题
        }
        
        // 正文字体
        object Body {
            val large = 16.sp      // 大正文
            val medium = 14.sp     // 中等正文
            val small = 12.sp      // 小正文
            val tiny = 10.sp       // 极小文字
        }
        
        // 按钮字体
        object Button {
            val large = 16.sp      // 大按钮
            val medium = 14.sp     // 中等按钮
            val small = 12.sp      // 小按钮
        }
        
        // 特殊用途字体
        object Special {
            val avatar = 20.sp     // 头像文字
            val image = 24.sp      // 图片占位符
            val dialog = 15.sp     // 对话框标题
        }
        
        // 常用快捷访问
        val Large = 18.sp
        val Medium = 16.sp
        val Normal = 14.sp
        val Small = 12.sp
        val Tiny = 10.sp
    }
    
    // ==================== 预定义 TextStyle ====================
    
    /**
     * 大标题样式
     */
    val largeTitle = TextStyle(
        fontSize = FontSizes.Title.large,
        fontWeight = FontWeight.Bold
    )
    
    /**
     * 中等标题样式
     */
    val mediumTitle = TextStyle(
        fontSize = FontSizes.Title.medium,
        fontWeight = FontWeight.Bold
    )
    
    /**
     * 小标题样式
     */
    val smallTitle = TextStyle(
        fontSize = FontSizes.Title.small,
        fontWeight = FontWeight.Medium
    )
    
    /**
     * 正文样式
     */
    val body = TextStyle(
        fontSize = FontSizes.Body.medium
    )
    
    /**
     * 小文字样式
     */
    val caption = TextStyle(
        fontSize = FontSizes.Body.small
    )
    
    /**
     * 极小文字样式
     */
    val tiny = TextStyle(
        fontSize = FontSizes.Body.tiny
    )
    
    /**
     * 按钮文字样式
     */
    val button = TextStyle(
        fontSize = FontSizes.Button.medium,
        fontWeight = FontWeight.Medium
    )
    
    // ==================== 兼容旧版本的属性 ====================
    
    /**
     * 主页面标题样式 (兼容旧版本)
     */
    val screenTitle = largeTitle
    
    /**
     * Tab 标签样式 (兼容旧版本)
     */
    val tabLabel = body
    
    /**
     * 大图标文字样式 (兼容旧版本)
     */
    val largeIconText = largeTitle
    
    /**
     * 正文样式 (兼容旧版本)
     */
    val bodyMediumText = body
}
