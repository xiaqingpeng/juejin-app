# 字体管理使用指南

## 概述
统一管理应用中的字体大小和样式，确保设计一致性。

## 文件结构
```
ui/typography/
├── Typography.kt    # 统一字体管理
└── README.md        # 使用指南
```

## 使用方法

### 1. 导入
```kotlin
import com.example.juejin.ui.typography.Typography
```

### 2. 使用预定义 TextStyle
```kotlin
// 大标题
Text("标题", style = Typography.largeTitle)

// 中等标题
Text("副标题", style = Typography.mediumTitle)

// 正文
Text("正文内容", style = Typography.body)

// 小文字
Text("辅助文字", style = Typography.caption)

// 按钮
Text("按钮文字", style = Typography.button)
```

### 3. 使用字体大小常量
```kotlin
// 直接使用字体大小
Text("文字", fontSize = Typography.FontSizes.Title.large)
Text("文字", fontSize = Typography.FontSizes.Body.small)
Text("文字", fontSize = Typography.FontSizes.Button.medium)

// 快捷访问
Text("文字", fontSize = Typography.FontSizes.Large)
Text("文字", fontSize = Typography.FontSizes.Small)
```

### 4. 字体大小分类

#### 标题字体 (Title)
- `large = 18.sp` - 页面主标题
- `medium = 16.sp` - 区块标题  
- `small = 14.sp` - 小标题

#### 正文字体 (Body)
- `large = 16.sp` - 大正文
- `medium = 14.sp` - 中等正文
- `small = 12.sp` - 小正文
- `tiny = 10.sp` - 极小文字

#### 按钮字体 (Button)
- `large = 16.sp` - 大按钮
- `medium = 14.sp` - 中等按钮
- `small = 12.sp` - 小按钮

#### 特殊用途字体 (Special)
- `avatar = 20.sp` - 头像文字
- `image = 24.sp` - 图片占位符
- `dialog = 15.sp` - 对话框标题

## 迁移指南

### 替换现有代码
```kotlin
// 之前
Text("标题", fontSize = 18.sp, fontWeight = FontWeight.Bold)
Text("正文", fontSize = 14.sp)
Text("小字", fontSize = 12.sp)

// 现在
Text("标题", style = Typography.largeTitle)
Text("正文", style = Typography.body)
Text("小字", style = Typography.caption)
```

### 批量替换
1. 搜索 `fontSize = \d+\.sp`
2. 根据上下文选择合适的 TextStyle 或 FontSize 常量
3. 更新导入语句

## 最佳实践

1. **优先使用预定义 TextStyle**：包含完整的样式定义
2. **保持一致性**：相同用途的文字使用相同样式
3. **避免硬编码**：不要直接写 `18.sp` 等数值
4. **按场景选择**：
   - 页面标题 → `Typography.largeTitle`
   - 区块标题 → `Typography.mediumTitle`
   - 正文内容 → `Typography.body`
   - 辅助文字 → `Typography.caption`
   - 按钮文字 → `Typography.button`
