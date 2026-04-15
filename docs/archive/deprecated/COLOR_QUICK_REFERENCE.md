# 颜色快速参考卡片

## 常用颜色速查表

### 📝 文本颜色

```kotlin
Colors.Text.primary        // #000000 - 标题、重要内容
Colors.Text.secondary      // #808080 - 描述、辅助信息
Colors.Text.tertiary       // #999999 - 不重要的信息
Colors.Text.placeholder    // #BBBBBB - 输入框占位符
Colors.Text.white          // #FFFFFF - 深色背景上的文字
Colors.Text.link           // #1890FF - 可点击链接
Colors.Text.destructive    // #FF0000 - 删除、危险操作
```

### 🎨 背景颜色

```kotlin
Colors.Background.primary    // #F5F5F5 - 页面主背景（灰色）
Colors.Background.surface    // #FFFFFF - 卡片、弹窗背景（白色）
Colors.Background.input      // #F5F5F5 - 输入框背景
Colors.Background.dialog     // #F2F2F2 - 对话框背景
```

### 🔘 按钮颜色

```kotlin
// 主按钮（蓝色）
containerColor = Colors.Button.primary      // #1890FF
contentColor = Colors.Button.primaryText    // #FFFFFF

// 次要按钮（灰色）
containerColor = Colors.Button.secondary    // #E5E5E5
contentColor = Colors.Button.secondaryText  // #000000

// 危险按钮（红色）
containerColor = Colors.Button.danger       // #FF4D4F
contentColor = Colors.Button.dangerText     // #FFFFFF
```

### 🎯 界面元素

```kotlin
Colors.UI.divider          // #E0E0E0 - 分隔线
Colors.UI.border           // #D9D9D9 - 边框
Colors.UI.avatar           // #E0E0E0 - 头像占位背景
```

## 使用模板

### 页面布局
```kotlin
MaterialTheme(
    colorScheme = lightColorScheme(
        background = Colors.Background.primary,  // 灰色背景
        surface = Colors.Background.surface      // 白色卡片
    )
) {
    Scaffold(
        topBar = { /* TopBar */ }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Colors.Background.primary)
        ) {
            // 内容
        }
    }
}
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

### 输入框
```kotlin
OutlinedTextField(
    value = text,
    onValueChange = { text = it },
    colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Colors.primaryBlue,
        unfocusedBorderColor = Colors.UI.divider,
        focusedContainerColor = Colors.Background.surface,
        unfocusedContainerColor = Colors.Background.input
    )
)
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
) { Text("确定") }

// 次要按钮
Button(
    onClick = { },
    colors = ButtonDefaults.buttonColors(
        containerColor = Colors.Button.secondary,
        contentColor = Colors.Button.secondaryText
    )
) { Text("取消") }
```

## ❌ 禁止使用

```kotlin
// ❌ 不要使用
Color.Black
Color.White
Color.Gray
Color(0xFF000000)
Color(0xFFF5F5F5)

// ✅ 应该使用
Colors.Text.primary
Colors.Text.white
Colors.Text.secondary
Colors.Text.primary
Colors.Background.primary
```

## 💡 提示

1. **IDE 自动完成**：输入 `Colors.` 后按 Ctrl+Space 查看所有可用颜色
2. **语义化命名**：选择最符合用途的颜色名称，而不是颜色值
3. **保持一致**：相同用途的元素使用相同的颜色
4. **代码审查**：检查是否有硬编码的颜色值

## 📚 完整文档

详细信息请参考：
- `COLOR_GUIDE.md` - 完整使用指南
- `Colors.kt` - 所有颜色定义
- `COLOR_MIGRATION_SUMMARY.md` - 迁移总结
