# 设置详情页面功能说明

## 📋 功能概述

已为设置页面添加了二级路由功能，用户点击设置项后可以进入详情页面查看更多信息。

## 🎯 实现的功能

### 1. 二级路由导航
- 点击普通设置项（`SettingType.NORMAL`）跳转到详情页
- 开关类型（`SettingType.SWITCH`）直接切换，不跳转
- 选择器类型（`SettingType.SELECTOR`）显示下拉菜单，不跳转
- 破坏性操作（如"退出登录"）执行特定逻辑，不跳转

### 2. 详情页面内容

已为以下设置项创建了详细内容：

#### 账号与安全
- 手机号
- 邮箱
- 密码
- 实名认证
- 账号注销

#### 隐私设置
- 个人信息保护
- 数据使用
- 位置信息
- 广告追踪
- 隐私政策

#### 通用设置
- 语言
- 字体大小
- 自动播放
- 流量提醒
- 存储管理

#### 消息通知
- 系统通知
- 评论通知
- 点赞通知
- 关注通知
- 私信通知
- 活动通知

#### 清除缓存
- 图片缓存
- 视频缓存
- 文章缓存
- 其他缓存
- 总计大小

#### 关于我们
- 应用名称
- 版本号
- 开发者
- 官方网站
- 用户协议
- 隐私政策
- 开源许可

#### 帮助与反馈
- 常见问题
- 使用教程
- 意见反馈
- 联系客服
- 问题报告

## 🔧 技术实现

### 文件结构

```
composeApp/src/commonMain/kotlin/com/example/juejin/screen/
├── SettingsScreen.kt          # 设置主页面（包含导航逻辑）
└── SettingDetailScreen.kt     # 设置详情页面
```

### 核心代码

#### 1. 导航状态管理

```kotlin
@Composable
fun SettingsScreen(...) {
    // 导航状态
    var selectedSetting by remember { mutableStateOf<SettingItem?>(null) }
    
    // 根据状态显示不同页面
    if (selectedSetting != null) {
        SettingDetailScreen(
            settingItem = selectedSetting!!,
            onLeftClick = { selectedSetting = null }
        )
    } else {
        SettingsListScreen(...)
    }
}
```

#### 2. 点击事件处理

```kotlin
onItemClick = { item ->
    // 只有 NORMAL 类型的设置项才跳转到详情页
    if (item.type == SettingType.NORMAL && !item.isDestructive) {
        selectedSetting = item
    } else if (item.isDestructive) {
        // 处理退出登录等破坏性操作
        println("执行破坏性操作: ${item.title}")
    }
}
```

#### 3. 详情页面组件

```kotlin
@Composable
fun SettingDetailScreen(
    settingItem: SettingItem,
    onLeftClick: () -> Unit
) {
    // 根据不同的设置项显示不同的内容
    when (settingItem.title) {
        "账号与安全" -> AccountSecurityContent()
        "隐私设置" -> PrivacySettingsContent()
        // ...
    }
}
```

## 🎨 UI 设计

### 页面结构

```
┌─────────────────────────┐
│  ← 设置项标题            │  顶部导航栏
├─────────────────────────┤
│                         │
│  标题                    │  详情标题
│  描述文字                │  可选描述
│                         │
│  ┌───────────────────┐  │
│  │ 标签              │  │  详情项
│  │ 值                │  │
│  └───────────────────┘  │
│                         │
│  ┌───────────────────┐  │
│  │ 标签              │  │  详情项
│  │ 值                │  │
│  └───────────────────┘  │
│                         │
└─────────────────────────┘
```

### 样式特点

- 使用统一的颜色系统（`Colors`）
- 清晰的层级结构
- 适当的间距和对齐
- 支持高亮显示重要信息

## 📱 使用示例

### 用户操作流程

1. 打开"我的"页面
2. 点击"设置"按钮
3. 在设置列表中点击任意普通设置项（如"账号与安全"）
4. 进入详情页面查看详细信息
5. 点击返回按钮回到设置列表

### 不同类型设置项的行为

| 设置项类型 | 点击行为 | 示例 |
|-----------|---------|------|
| NORMAL | 跳转到详情页 | 账号与安全、隐私设置 |
| SWITCH | 切换开关状态 | 推送通知 |
| SELECTOR | 显示下拉菜单 | 深色模式 |
| 破坏性操作 | 执行特定逻辑 | 退出登录 |

## 🔄 扩展指南

### 添加新的详情页面

1. 在 `SettingDetailScreen.kt` 中添加新的内容组件：

```kotlin
@Composable
private fun NewSettingContent() {
    DetailSection(
        title = "新设置项",
        description = "描述信息",
        items = listOf(
            DetailItem("标签1", "值1"),
            DetailItem("标签2", "值2")
        )
    )
}
```

2. 在 `SettingDetailScreen` 的 `when` 语句中添加新的分支：

```kotlin
when (settingItem.title) {
    "新设置项" -> NewSettingContent()
    // ...
}
```

### 自定义详情页面布局

如果需要完全自定义的布局，可以直接在 `when` 分支中编写：

```kotlin
when (settingItem.title) {
    "自定义页面" -> {
        Column(modifier = Modifier.padding(16.dp)) {
            // 自定义 UI 组件
            Text("自定义内容")
            Button(onClick = {}) {
                Text("自定义按钮")
            }
        }
    }
}
```

## 🎯 后续优化建议

### 短期
1. 添加详情页面的交互功能（如编辑、保存）
2. 实现真实的数据加载和保存
3. 添加加载状态和错误处理

### 中期
1. 使用 ViewModel 管理详情页面状态
2. 添加页面切换动画
3. 支持深层嵌套的三级、四级页面

### 长期
1. 实现完整的导航框架
2. 添加页面历史记录
3. 支持 Deep Link

## 📝 注意事项

1. **状态管理**: 当前使用 `remember` 管理导航状态，适合简单场景
2. **数据持久化**: 详情页面显示的数据目前是静态的，需要连接真实数据源
3. **返回处理**: 使用 `onLeftClick` 回调处理返回操作，确保状态正确重置
4. **类型判断**: 通过 `SettingType` 判断是否跳转，避免不必要的页面跳转

## 🔗 相关文件

- `SettingsScreen.kt` - 设置主页面
- `SettingDetailScreen.kt` - 设置详情页面
- `SettingModel.kt` - 设置项数据模型
- `SettingViewModel.kt` - 设置页面 ViewModel

---

最后更新: 2024-03-24
