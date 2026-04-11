# 🚀 快速修复指南 - 模块找不到问题

## 问题
在 Android Studio 运行配置中找不到模块 `juejin-app.apps.juejin-lite.main`

## ✅ 快速解决方案

### 方案 1：运行修复脚本（推荐）

```bash
./fix-android-studio.sh
```

然后按照脚本提示操作。

### 方案 2：手动修复（3步）

#### 步骤 1：重启 Android Studio
```
File → Invalidate Caches / Restart → Invalidate and Restart
```

#### 步骤 2：同步 Gradle
- 点击顶部的 🐘 图标
- 或 `File → Sync Project with Gradle Files`

#### 步骤 3：手动创建运行配置

1. **打开配置编辑器**
   ```
   Run → Edit Configurations...
   ```

2. **添加 juejin-lite 配置**
   - 点击 **+** → **Android App**
   - Name: `juejin-lite`
   - Module: 从下拉菜单选择，查找以下格式之一：
     ```
     ✓ Juejin.apps.juejin-lite.main
     ✓ apps.juejin-lite.main
     ✓ juejin-lite.main
     ```
   - Launch: `Default Activity`
   - 点击 **Apply**

3. **添加 juejin-main 配置**
   - 重复步骤 2，但选择包含 `juejin-main` 的模块

4. **保存并测试**
   - 点击 **OK**
   - 选择 `juejin-lite` 配置
   - 点击 ▶️ 运行

## 🎯 如何找到正确的模块名称

### 在 Module 下拉菜单中查找

打开 **Edit Configurations...** → 点击 **Module** 下拉菜单，你应该能看到：

```
┌─────────────────────────────────────┐
│ Juejin.apps.juejin-lite.main       │  ← 选择这个（轻量版）
│ Juejin.apps.juejin-main.main       │  ← 或这个（完整版）
│ Juejin.shared.core.common.main     │
│ Juejin.shared.core.storage.main    │
│ ...                                 │
└─────────────────────────────────────┘
```

**关键点**：
- 项目名称是 `Juejin`（不是 `juejin-app`）
- 模块路径是 `apps.juejin-lite.main`
- 完整格式：`Juejin.apps.juejin-lite.main`

## 📊 配置对比

| 配置项 | 正确值 | 错误值 |
|--------|--------|--------|
| Name | juejin-lite | ❌ |
| Module | ✅ Juejin.apps.juejin-lite.main | ❌ juejin-app.apps.juejin-lite.main |
| Launch | Default Activity | ❌ |

## 🛠️ 如果仍然找不到模块

### 使用 Gradle 面板（临时方案）

1. **打开 Gradle 面板**
   ```
   右侧边栏 → Gradle 图标
   或 View → Tool Windows → Gradle
   ```

2. **找到安装任务**
   ```
   Gradle
     └── Juejin
         └── apps
             └── juejin-lite
                 └── Tasks
                     └── install
                         └── installDebug  ← 双击运行
   ```

3. **运行任务**
   - 双击 `installDebug`
   - 应用会自动安装到设备

### 使用命令行（备用方案）

```bash
# 安装并运行 juejin-lite
./gradlew :apps:juejin-lite:installDebug
adb shell am start -n com.example.juejin.lite/.MainActivity

# 安装并运行 juejin-main
./gradlew :apps:juejin-main:installDebug
adb shell am start -n com.example.juejin/.MainActivity
```

## 🔍 验证配置

运行以下命令验证模块存在：

```bash
./gradlew projects
```

应该看到：
```
+--- Project ':apps'
|    +--- Project ':apps:juejin-lite'  ← 存在
|    \--- Project ':apps:juejin-main'  ← 存在
```

## 📸 正确的配置截图说明

### Module 下拉菜单应该显示：
```
┌─────────────────────────────────────┐
│ 搜索框: [输入 juejin]               │
├─────────────────────────────────────┤
│ ✓ Juejin.apps.juejin-lite.main     │  ← 轻量版
│   Juejin.apps.juejin-main.main     │  ← 完整版
│   Juejin.shared.core.common.main   │
│   Juejin.shared.core.storage.main  │
│   Juejin.shared.core.network.main  │
│   Juejin.shared.domain.main        │
│   Juejin.shared.ui.theme.main      │
│   Juejin.shared.ui.components.main │
└─────────────────────────────────────┘
```

### 完整的配置应该是：
```
┌────────────────────────────────────────┐
│ Name: juejin-lite                      │
│ Module: Juejin.apps.juejin-lite.main  │
│ Launch: Default Activity               │
│ Deployment Target: Open Select Dialog  │
└────────────────────────────────────────┘
```

## ✅ 成功标志

配置成功后，你应该能够：

- [x] 在运行配置下拉菜单中看到 `juejin-lite`
- [x] Module 字段不是红色（表示找到了模块）
- [x] 可以选择设备
- [x] 点击 ▶️ 可以成功运行
- [x] 应用安装到设备上
- [x] Logcat 显示应用日志

## 📚 详细文档

如果需要更多信息，请查看：
- [MODULE_NOT_FOUND_FIX.md](MODULE_NOT_FOUND_FIX.md) - 详细故障排查
- [ANDROID_STUDIO_QUICK_SETUP.md](ANDROID_STUDIO_QUICK_SETUP.md) - 完整设置指南
- [ANDROID_STUDIO_VISUAL_GUIDE.md](ANDROID_STUDIO_VISUAL_GUIDE.md) - 图文指南

## 💡 关键要点

1. **项目名称是 `Juejin`**（在 settings.gradle.kts 中定义）
2. **模块格式是 `Juejin.apps.juejin-lite.main`**
3. **不是 `juejin-app.apps.juejin-lite.main`**
4. **如果找不到，手动从下拉菜单选择**
5. **实在不行，使用 Gradle 面板或命令行**

---

**更新时间**：2026-04-11  
**状态**：✅ 已修复配置文件
