package com.example.juejin.ui

import androidx.compose.ui.graphics.Color

/**
 * 集中管理应用的所有颜色定义
 * 统一颜色管理，便于主题切换和维护
 */
object Colors {
    // ==================== 主色调 ====================
    val primaryBlue = Color(0xFF1890FF)      // 主蓝色
    val primaryWhite = Color(0xFFFFFFFF)     // 纯白色
    val primaryBlack = Color(0xFF000000)     // 纯黑色
    val primaryGray = Color(0xFF808080)      // 中灰色
    val primaryGrey = Color(0xFFE5E5E5)      // 浅灰色（保留兼容）
    
    // ==================== 文本颜色 ====================
    object Text {
        val primary = Color(0xFF000000)       // 主要文本：黑色
        val secondary = Color(0xFF808080)     // 次要文本：灰色
        val tertiary = Color(0xFF999999)      // 三级文本：浅灰色
        val placeholder = Color(0xFFBBBBBB)   // 占位文本：更浅的灰色
        val white = Color(0xFFFFFFFF)         // 白色文本
        val darkGray = Color(0xFF666666)      // 深灰色文本
        val lightGray = Color(0xFFCCCCCC)     // 浅灰色文本
        val destructive = Color(0xFFFF0000)   // 危险/删除：红色
        val link = Color(0xFF1890FF)          // 链接：蓝色
        val success = Color(0xFF52C41A)       // 成功：绿色
        val warning = Color(0xFFFAAD14)       // 警告：橙色
        val error = Color(0xFFFF4D4F)         // 错误：红色
    }
    
    // ==================== 背景颜色 ====================
    object Background {
        val primary = Color(0xFFF5F5F5)       // 主背景：浅灰色
        val secondary = Color(0xFFFAFAFA)     // 次要背景：更浅的灰色
        val surface = Color(0xFFFFFFFF)       // 卡片/表面：白色
        val white = Color(0xFFFFFFFF)         // 纯白背景
        val dialog = Color(0xFFF2F2F2)        // 对话框背景
        val input = Color(0xFFF5F5F5)         // 输入框背景
        val disabled = Color(0xFFE0E0E0)      // 禁用状态背景
    }
    
    // ==================== 界面元素颜色 ====================
    object UI {
        val divider = Color(0xFFE0E0E0)       // 分隔线
        val border = Color(0xFFD9D9D9)        // 边框
        val shadow = Color(0x1A000000)        // 阴影（10% 透明度）
        val overlay = Color(0x80000000)       // 遮罩层（50% 透明度）
        val avatar = Color(0xFFE0E0E0)        // 头像占位背景
        val levelBadge = Color(0xFFE8F4FF)    // 等级徽章背景
        val levelText = Color(0xFF1377EB)     // 等级徽章文字
        val memberBanner = Color(0xFF2C2C54)  // 会员横幅背景
        val memberButton = Color(0xFFFFD700)  // 会员按钮背景
        val memberButtonText = Color(0xFF000000) // 会员按钮文字
        val cardBackground = Color(0xFFFFFFFF) // 卡片背景
        val ripple = Color(0x1A000000)        // 点击波纹效果
    }
    
    // ==================== 快捷功能颜色 ====================
    object QuickFunctions {
        val checkIn = Color(0xFF1377EB)       // 每日签到
        val luckyWheel = Color(0xFFFF9800)    // 幸运转盘
        val bugChallenge = Color(0xFF9C27B0)  // Bug挑战赛
        val welfare = Color(0xFFE91E63)       // 福利兑换
    }
    
    // ==================== Switch 组件颜色 ====================
    object Switch {
        val checkedThumb = primaryBlue
        val uncheckedThumb = primaryGrey
        val checkedTrack = primaryBlue.copy(alpha = 0.3f)
        val uncheckedTrack = primaryGrey.copy(alpha = 0.3f)
    }
    
    // ==================== 按钮颜色 ====================
    object Button {
        val primary = Color(0xFF1890FF)       // 主按钮背景
        val primaryText = Color(0xFFFFFFFF)   // 主按钮文字
        val secondary = Color(0xFFE5E5E5)     // 次要按钮背景
        val secondaryText = Color(0xFF000000) // 次要按钮文字
        val disabled = Color(0xFFE0E0E0)      // 禁用按钮背景
        val disabledText = Color(0xFFBBBBBB)  // 禁用按钮文字
        val danger = Color(0xFFFF4D4F)        // 危险按钮背景
        val dangerText = Color(0xFFFFFFFF)    // 危险按钮文字
    }
    
    // ==================== 状态颜色 ====================
    object Status {
        val success = Color(0xFF52C41A)       // 成功状态
        val warning = Color(0xFFFAAD14)       // 警告状态
        val error = Color(0xFFFF4D4F)         // 错误状态
        val info = Color(0xFF1890FF)          // 信息状态
        val successBg = Color(0xFFF6FFED)     // 成功背景
        val warningBg = Color(0xFFFFFBE6)     // 警告背景
        val errorBg = Color(0xFFFFF1F0)       // 错误背景
        val infoBg = Color(0xFFE6F7FF)        // 信息背景
    }
    
    // ==================== 兼容性别名（保留旧代码兼容） ====================
    @Deprecated("使用 primaryGray 替代", ReplaceWith("primaryGray"))
    val unselectedGray = primaryGray
}