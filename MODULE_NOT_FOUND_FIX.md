# 🔧 模块找不到问题解决方案

## 问题描述

在 Android Studio 运行配置中找不到模块：
- ❌ `juejin-app.apps.juejin-lite.main`
- ❌ `juejin-app.apps.juejin-main.main`

## ✅ 解决方案

### 方案 1：使用正确的模块名称（已修复）

我已经更新了配置文件，使用正确的模块名称：
- ✅ `Juejin.apps.juejin-lite.main`
- ✅ `Juejin.apps.juejin-main.main`

**操作步骤**：
1. 重启 Android Studio
2. 同步 Gradle（点击 🐘 图标）
3. 查看运行配置下拉菜单

### 方案 2：手动选择模块

如果自动配置仍然不工作，手动创建配置：

#### 步骤 1：打开运行配置编辑器
```
顶部菜单 → Run → Edit Configurations...
或点击运行配置下拉菜单 → Edit Configurations...
```

#### 步骤 2：添加新配置
1. 点击左上角的 **+** 按钮
2. 选择 **Android App**

#### 步骤 3：配置 juejin-lite
```
Name: juejin-lite
Module: 从下拉菜单中选择
  - 查找包含 "juejin-lite" 的选项
  - 可能的格式：
    ✓ Juejin.apps.juejin-lite.main
    ✓ apps.juejin-lite.main
    ✓ juejin-lite.main
Launch: Default Activity
```

#### 步骤 4：配置 juejin-main
```
Name: juejin-main
Module: 从下拉菜单中选择
  - 查找包含 "juejin-main" 的选项
  - 可能的格式：
    ✓ Juejin.apps.juejin-main.main
    ✓ apps.juejin-main.main
    ✓ juejin-main.main
Launch: Default Activity
```

#### 步骤 5：保存
1. 点击 **Apply**
2. 点击 **OK**

### 方案 3：重新同步项目

#### 步骤 1：清理缓存
```
File → Invalidate Caches / Restart → Invalidate and Restart
```

#### 步骤 2：同步 Gradle
```bash
# 在终端执行
./gradlew clean
./gradlew build --no-daemon
```

#### 步骤 3：重新打开项目
1. 关闭 Android Studio
2. 删除 `.idea` 目录（可选，会重置所有 IDE 设置）
   ```bash
   rm -rf .idea
   ```
3. 重新打开项目

### 方案 4：使用 Gradle 任务（临时方案）

如果运行配置仍然不工作，可以使用 Gradle 面板：

#### 步骤 1：打开 Gradle 面板
```
右侧边栏 → Gradle 图标
或 View → Tool Windows → Gradle
```

#### 步骤 2：找到安装任务
```
Gradle 面板
  └── Juejin
      └── apps
          ├── juejin-main
          │   └── Tasks
          │       └── install
          │           └── installDebug  ← 双击运行
          └── juejin-lite
              └── Tasks
                  └── install
                      └── installDebug  ← 双击运行
```

## 🔍 诊断步骤

### 1. 验证模块存在
```bash
./gradlew projects
```

应该看到：
```
+--- Project ':apps'
|    +--- Project ':apps:juejin-lite'
|    \--- Project ':apps:juejin-main'
```

### 2. 验证模块可以编译
```bash
# 编译 juejin-lite
./gradlew :apps:juejin-lite:assembleDebug

# 编译 juejin-main
./gradlew :apps:juejin-main:assembleDebug
```

### 3. 检查 settings.gradle.kts
```bash
cat settings.gradle.kts | grep -A 5 "include"
```

应该包含：
```kotlin
include(":apps:juejin-main")
include(":apps:juejin-lite")
```

### 4. 检查项目名称
```bash
cat settings.gradle.kts | grep "rootProject.name"
```

应该显示：
```kotlin
rootProject.name = "Juejin"
```

## 📋 可能的模块名称格式

根据项目配置，模块名称可能是以下格式之一：

### 格式 1：完整路径（推荐）
```
Juejin.apps.juejin-lite.main
Juejin.apps.juejin-main.main
```

### 格式 2：相对路径
```
apps.juejin-lite.main
apps.juejin-main.main
```

### 格式 3：简短名称
```
juejin-lite.main
juejin-main.main
```

### 格式 4：带连字符
```
Juejin.apps.juejin-lite
Juejin.apps.juejin-main
```

## 🎯 如何找到正确的模块名称

### 方法 1：在运行配置编辑器中查看

1. 打开 **Edit Configurations...**
2. 点击 **+** → **Android App**
3. 点击 **Module** 下拉菜单
4. 查看所有可用的模块名称
5. 选择包含 "juejin-lite" 或 "juejin-main" 的选项

### 方法 2：查看现有配置

如果项目中已经有其他运行配置：

1. 打开 **Edit Configurations...**
2. 查看现有配置的 Module 字段
3. 使用相同的命名格式

### 方法 3：查看 .iml 文件

```bash
find . -name "*.iml" | grep juejin
```

## 🛠️ 完整修复流程

### 步骤 1：清理项目
```bash
./gradlew clean
rm -rf .gradle
rm -rf build
rm -rf apps/*/build
```

### 步骤 2：重新构建
```bash
./gradlew build --no-daemon
```

### 步骤 3：重启 Android Studio
```
File → Invalidate Caches / Restart → Invalidate and Restart
```

### 步骤 4：同步 Gradle
```
点击顶部的 🐘 图标
或 File → Sync Project with Gradle Files
```

### 步骤 5：手动创建配置
1. Run → Edit Configurations...
2. 点击 **+** → **Android App**
3. 配置 juejin-lite：
   - Name: `juejin-lite`
   - Module: 从下拉菜单选择（查找包含 juejin-lite 的选项）
   - Launch: `Default Activity`
4. 点击 **Apply**
5. 重复步骤 2-4 创建 juejin-main 配置

### 步骤 6：测试运行
1. 选择 `juejin-lite` 配置
2. 选择设备
3. 点击 ▶️ 运行

## 📸 截图参考

### Module 下拉菜单应该显示：
```
┌─────────────────────────────────────┐
│ Juejin.apps.juejin-lite.main       │  ← 选择这个
│ Juejin.apps.juejin-main.main       │
│ Juejin.shared.core.common.main     │
│ Juejin.shared.core.storage.main    │
│ Juejin.shared.core.network.main    │
│ Juejin.shared.domain.main          │
│ Juejin.shared.ui.theme.main        │
│ Juejin.shared.ui.components.main   │
└─────────────────────────────────────┘
```

## ✅ 验证配置成功

配置成功后，你应该能够：

- [ ] 在运行配置下拉菜单中看到 `juejin-lite` 和 `juejin-main`
- [ ] Module 字段显示正确的模块名称（不是红色）
- [ ] 可以选择设备
- [ ] 点击运行按钮可以启动应用
- [ ] Logcat 显示应用日志

## 🆘 如果仍然无法解决

### 使用命令行运行（临时方案）

```bash
# 安装并运行 juejin-lite
./gradlew :apps:juejin-lite:installDebug
adb shell am start -n com.example.juejin.lite/.MainActivity

# 安装并运行 juejin-main
./gradlew :apps:juejin-main:installDebug
adb shell am start -n com.example.juejin/.MainActivity
```

### 联系支持

如果以上方法都不行，请提供以下信息：

1. Android Studio 版本
2. Gradle 版本
3. 运行 `./gradlew projects` 的输出
4. Module 下拉菜单中显示的所有选项
5. 错误信息截图

## 📚 相关文档

- [ANDROID_STUDIO_QUICK_SETUP.md](ANDROID_STUDIO_QUICK_SETUP.md) - 快速设置
- [ANDROID_STUDIO_RUN_CONFIG.md](ANDROID_STUDIO_RUN_CONFIG.md) - 详细配置
- [RUN_APPS_GUIDE.md](RUN_APPS_GUIDE.md) - 命令行运行

## 💡 预防措施

为了避免将来出现类似问题：

1. ✅ 保持 Android Studio 更新到最新版本
2. ✅ 定期同步 Gradle
3. ✅ 不要手动修改 `.idea` 目录中的文件
4. ✅ 使用 Git 忽略 `.idea` 中的临时文件
5. ✅ 团队共享运行配置时使用相对路径

---

**更新时间**：2026-04-11  
**状态**：已修复配置文件，使用 `Juejin.apps.juejin-lite.main` 格式
