# 颜色使用指南

本项目采用统一的颜色管理系统，所有颜色定义集中在 `Colors.kt` 文件中。

## 颜色管理文件位置

- **颜色定义**: `composeApp/src/commonMain/kotlin/com/example/juejin/ui/Colors.kt`
- **字体定义**: `composeApp/src/commonMain/kotlin/com/example/juejin/ui/typography/Typography.kt`
- **字符串资源**: `composeApp/src/commonMain/composeResources/values/strings.xml`

## 颜色分类

### 1. 主色调 (Primary Colors)
```kotlin
Colors.primaryBlue      // 主蓝色 #1890FF - 用于主要按钮、链接、强调元素
Colors.primaryWhite     // 纯白色 #FFFFFF - 用于背景、卡片
Colors.primaryBlack     // 纯黑色 #000000 - 用于主要文本
Colors.primaryGray      // 中灰色 #808080 - 用于次要元素
```

### 2. 文本颜色 (Text Colors)
```kotlin
Colors.Text.primary        // 主要文本 #000000 - 标题、重要内容
Colors.Text.secondary      // 次要文本 #808080 - 描述、辅助信息
Colors.Text.tertiary       // 三级文本 #999999 - 不重要的信息
Colors.Text.placeholder    // 占位文本 #BBBBBB - 输入框占位符
Colors.Text.white          // 白色文本 #FFFFFF - 深色背景上的文字
Colors.Text.darkGray       // 深灰文本 #666666 - 中等重要度文本
Colors.Text.lightGray      // 浅灰文本 #CCCCCC - 禁用状态文本
Colors.Text.destructive    // 危险文本 #FF0000 - 删除、警告
Colors.Text.link           // 链接文本 #1890FF - 可点击链接
Colors.Text.success        // 成功文本 #52C41A - 成功提示
Colors.Text.warning        // 警告文本 #FAAD14 - 警告提示
Colors.Text.error          // 错误文本 #FF4D4F - 错误提示
```

### 3. 背景颜色 (Background Colors)
```kotlin
Colors.Background.primary     // 主背景 #F5F5F5 - 页面主背景
Colors.Background.secondary   // 次要背景 #FAFAFA - 区块背景
Colors.Background.surface     // 表面背景 #FFFFFF - 卡片、弹窗
Colors.Background.white       // 纯白背景 #FFFFFF - 特殊白色背景
Colors.Background.dialog      // 对话框背景 #F2F2F2 - 对话框
Colors.Background.input       // 输入框背景 #F5F5F5 - 输入框
Colors.Background.disabled    // 禁用背景 #E0E0E0 - 禁用状态
```

### 4. 界面元素颜色 (UI Colors)
```kotlin
Colors.UI.divider            // 分隔线 #E0E0E0
Colors.UI.border             // 边框 #D9D9D9
Colors.UI.shadow             // 阴影 #1A000000 (10% 透明度)
Colors.UI.overlay            // 遮罩层 #80000000 (50% 透明度)
Colors.UI.avatar             // 头像占位 #E0E0E0
Colors.UI.cardBackground     // 卡片背景 #FFFFFF
Colors.UI.ripple             // 点击波纹 #1A000000
```

### 5. 按钮颜色 (Button Colors)
```kotlin
Colors.Button.primary          // 主按钮背景 #1890FF
Colors.Button.primaryText      // 主按钮文字 #FFFFFF
Colors.Button.secondary        // 次要按钮背景 #E5E5E5
Colors.Button.secondaryText    // 次要按钮文字 #000000
Colors.Button.disabled         // 禁用按钮背景 #E0E0E0
Colors.Button.disabledText     // 禁用按钮文字 #BBBBBB
Colors.Button.danger           // 危险按钮背景 #FF4D4F
Colors.Button.dangerText       // 危险按钮文字 #FFFFFF
```

### 6. 状态颜色 (Status Colors)
```kotlin
Colors.Status.success       // 成功 #52C41A
Colors.Status.warning       // 警告 #FAAD14
Colors.Status.error         // 错误 #FF4D4F
Colors.Status.info          // 信息 #1890FF
Colors.Status.successBg     // 成功背景 #F6FFED
Colors.Status.warningBg     // 警告背景 #FFFBE6
Colors.Status.errorBg       // 错误背景 #FFF1F0
Colors.Status.infoBg        // 信息背景 #E6F7FF
```

## 使用规范

### ✅ 正确使用
```kotlin
// 使用统一的颜色定义
Text(
    text = "标题",
    color = Colors.Text.primary,
    fontSize = Typography.FontSizes.Title.large
)

// 使用背景颜色
Box(
    modifier = Modifier.background(Colors.Background.primary)
)

// 使用按钮颜色
Button(
    colors = ButtonDefaults.buttonColors(
        containerColor = Colors.Button.primary,
        contentColor = Colors.Button.primaryText
    )
)
```

### ❌ 错误使用
```kotlin
// 不要直接使用 Color 构造函数
Text(
    text = "标题",
    color = Color(0xFF000000)  // ❌ 错误
)

// 不要直接使用 Color.XXX
Text(
    text = "标题",
    color = Color.Black  // ❌ 错误
)

// 不要硬编码颜色值
Box(
    modifier = Modifier.background(Color(0xFFF5F5F5))  // ❌ 错误
)
```

## 常见场景使用示例

### 页面背景
```kotlin
MaterialTheme(
    colorScheme = lightColorScheme(
        background = Colors.Background.primary,  // 灰色背景
        surface = Colors.Background.surface      // 白色卡片
    )
)
```

### 文本显示
```kotlin
// 标题
Text("标题", color = Colors.Text.primary, fontSize = 18.sp)

// 描述
Text("描述", color = Colors.Text.secondary, fontSize = 14.sp)

// 占位符
Text("请输入", color = Colors.Text.placeholder, fontSize = 14.sp)
```

### 列表项
```kotlin
Row(
    modifier = Modifier
        .fillMaxWidth()
        .background(Colors.Background.surface)
        .padding(16.dp)
) {
    Text("标签", color = Colors.Text.primary)
    Text("值", color = Colors.Text.secondary)
}

HorizontalDivider(color = Colors.UI.divider)
```

### 按钮
```kotlin
// 主按钮
Button(
    onClick = { },
    colors = ButtonDefaults.buttonColors(
        containerColor = Colors.Button.primary,
        contentColor = Colors.Button.primaryText
    )
) {
    Text("确定")
}

// 危险按钮
Button(
    onClick = { },
    colors = ButtonDefaults.buttonColors(
        containerColor = Colors.Button.danger,
        contentColor = Colors.Button.dangerText
    )
) {
    Text("删除")
}
```

### 状态提示
```kotlin
// 成功提示
Text(
    "操作成功",
    color = Colors.Status.success,
    modifier = Modifier.background(Colors.Status.successBg)
)

// 错误提示
Text(
    "操作失败",
    color = Colors.Status.error,
    modifier = Modifier.background(Colors.Status.errorBg)
)
```

## 迁移指南

如果你的代码中使用了硬编码的颜色，请按以下方式迁移：

| 旧代码 | 新代码 |
|--------|--------|
| `Color.Black` | `Colors.Text.primary` |
| `Color.Gray` | `Colors.Text.secondary` |
| `Color.White` | `Colors.Text.white` |
| `Color(0xFFF5F5F5)` | `Colors.Background.primary` |
| `Color(0xFFFFFFFF)` | `Colors.Background.surface` |
| `Color(0xFFE0E0E0)` | `Colors.UI.divider` |
| `Color(0xFF1890FF)` | `Colors.primaryBlue` |
| `Color.Red` | `Colors.Text.destructive` |

## 注意事项

1. **不要直接使用 `Color` 构造函数**：所有颜色都应该从 `Colors` 对象中获取
2. **不要使用 `Color.XXX` 预定义颜色**：使用 `Colors.Text.XXX` 等语义化的颜色
3. **保持一致性**：相同用途的元素使用相同的颜色
4. **考虑可访问性**：确保文本和背景有足够的对比度
5. **便于主题切换**：统一管理颜色便于未来实现深色模式

## 字体使用

配合 `Typography.kt` 使用字体大小：

```kotlin
Text(
    text = "标题",
    color = Colors.Text.primary,
    fontSize = Typography.FontSizes.Title.large,
    fontWeight = FontWeight.Bold
)

// 或使用预定义样式
Text(
    text = "标题",
    color = Colors.Text.primary,
    style = Typography.largeTitle
)
```

## 更新日志

- 2026-03-25: 创建统一颜色管理系统
- 添加了完整的颜色分类和语义化命名
- 提供了详细的使用指南和迁移方案
