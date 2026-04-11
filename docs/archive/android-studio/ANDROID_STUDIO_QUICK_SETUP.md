# 🚀 Android Studio 快速设置指南

## ✅ 已完成的配置

我已经为你创建了两个运行配置文件：

1. **juejin-main**（完整版）
   - 文件：`.idea/runConfigurations/juejin_main.xml`
   - 模块：`juejin-app.apps.juejin-main.main`

2. **juejin-lite**（轻量版）
   - 文件：`.idea/runConfigurations/juejin_lite.xml`
   - 模块：`juejin-app.apps.juejin-lite.main`

## 📋 使用步骤

### 步骤 1：重新打开项目（如果已打开）

如果 Android Studio 已经打开了项目：

1. 关闭 Android Studio
2. 重新打开项目
3. 或者：**File** → **Invalidate Caches / Restart** → **Invalidate and Restart**

### 步骤 2：同步 Gradle

1. 点击顶部工具栏的 **Sync Project with Gradle Files** 按钮（🐘图标）
2. 等待同步完成（可能需要几分钟）

### 步骤 3：查看运行配置

1. 查看顶部工具栏的运行配置下拉菜单
2. 你应该能看到：
   ```
   ▼ juejin-main
     juejin-lite
     Edit Configurations...
   ```

### 步骤 4：选择并运行

#### 运行轻量版（推荐用于开发）
1. 从下拉菜单选择 **juejin-lite**
2. 选择目标设备
3. 点击绿色运行按钮 ▶️
4. 或按快捷键：
   - macOS: `Ctrl + R`
   - Windows/Linux: `Shift + F10`

#### 运行完整版（用于完整测试）
1. 从下拉菜单选择 **juejin-main**
2. 选择目标设备
3. 点击运行按钮 ▶️

## 🎯 快速操作

### 运行
```
macOS: Ctrl + R
Windows/Linux: Shift + F10
```

### 调试
```
macOS: Ctrl + D
Windows/Linux: Shift + F9
```

### 停止
```
macOS: Cmd + F2
Windows/Linux: Ctrl + F2
```

### 切换配置
```
macOS: Ctrl + Alt + R
Windows/Linux: Alt + Shift + F10
```

## 📊 配置对比

| 特性 | juejin-main | juejin-lite |
|------|-------------|-------------|
| 编译时间 | ~38秒 | ~7秒 ⚡ |
| 功能 | 完整（5个标签） | 核心（3个标签） |
| 适用场景 | 完整测试 | 快速开发 |
| 推荐用途 | 发布前测试 | 日常开发 |

## 🎨 界面说明

### 运行配置下拉菜单位置
```
┌─────────────────────────────────────────┐
│ File Edit View ... Run Tools Window Help│
├─────────────────────────────────────────┤
│ ◀ ▶ ⚙️  [juejin-lite ▼] 📱 ▶️ 🐛 ⏹️    │  ← 这里
├─────────────────────────────────────────┤
│                                          │
│  Project 视图                            │
│                                          │
└─────────────────────────────────────────┘
```

### 运行配置下拉菜单内容
```
┌──────────────────────────┐
│ ▼ juejin-main           │  ← 完整版
│   juejin-lite           │  ← 轻量版（推荐）
│ ─────────────────────── │
│   Edit Configurations...│  ← 编辑配置
│   Save 'juejin-lite'... │
└──────────────────────────┘
```

## 🔧 如果配置没有显示

### 方法 1：刷新项目
```bash
# 在终端执行
./gradlew clean
./gradlew build
```

然后在 Android Studio 中：
1. **File** → **Sync Project with Gradle Files**
2. 等待同步完成

### 方法 2：手动导入配置
1. 确认文件存在：
   ```
   .idea/runConfigurations/juejin_main.xml
   .idea/runConfigurations/juejin_lite.xml
   ```
2. 重启 Android Studio
3. 配置应该自动加载

### 方法 3：手动创建（如果上述方法都不行）
参考 [ANDROID_STUDIO_RUN_CONFIG.md](ANDROID_STUDIO_RUN_CONFIG.md) 中的"方法二：手动创建配置"

## 🐛 常见问题

### 问题 1：找不到模块
**症状**：Module 下拉菜单中没有 `juejin-app.apps.juejin-lite.main`

**解决方案**：
1. 确认 `settings.gradle.kts` 中包含：
   ```kotlin
   include(":apps:juejin-lite")
   ```
2. 同步 Gradle：**File** → **Sync Project with Gradle Files**
3. 重启 Android Studio

### 问题 2：配置显示但无法运行
**症状**：点击运行后报错

**解决方案**：
```bash
# 清理并重新构建
./gradlew clean
./gradlew :apps:juejin-lite:assembleDebug
```

### 问题 3：设备未检测到
**症状**：没有可用的设备

**解决方案**：
1. 检查设备连接：
   ```bash
   adb devices
   ```
2. 如果没有设备，重启 ADB：
   ```bash
   adb kill-server
   adb start-server
   ```
3. 在 Android Studio 中点击设备下拉菜单刷新

### 问题 4：编译错误
**症状**：Gradle sync 失败

**解决方案**：
1. 检查 Gradle 版本
2. 清理缓存：
   ```bash
   ./gradlew clean
   rm -rf .gradle
   ```
3. 重新同步

## 💡 使用建议

### 日常开发流程
1. ✅ 使用 **juejin-lite** 进行开发（编译快）
2. ✅ 定期切换到 **juejin-main** 测试完整功能
3. ✅ 发布前在两个配置上都进行测试

### 调试技巧
1. 使用 **juejin-lite** 快速定位问题
2. 在 **juejin-main** 中验证修复
3. 利用 Logcat 过滤查看日志：
   ```
   package:com.example.juejin.lite
   ```

### 性能分析
1. 选择配置
2. 点击 **Profile** 按钮（而不是 Run）
3. 选择分析类型：
   - CPU
   - Memory
   - Network
   - Energy

## 📱 设备选择

### 模拟器（推荐用于开发）
- 优点：快速、方便、可以模拟多种设备
- 缺点：性能可能不如真机

### 真机（推荐用于测试）
- 优点：真实性能、真实用户体验
- 缺点：需要 USB 连接或无线调试

### 选择设备的位置
```
┌─────────────────────────────────────────┐
│ [juejin-lite ▼] [📱 设备选择 ▼] ▶️ 🐛   │
│                  ↑                       │
│                  这里选择设备             │
└─────────────────────────────────────────┘
```

## 🎓 进阶技巧

### 技巧 1：创建快捷方式
在 **Preferences** → **Keymap** 中为运行配置设置快捷键：
```
Run 'juejin-lite': Cmd + Shift + L
Run 'juejin-main': Cmd + Shift + M
```

### 技巧 2：使用 Gradle 面板
1. 打开右侧的 **Gradle** 面板
2. 展开：`juejin-app` → `apps` → `juejin-lite` → `Tasks` → `install`
3. 双击 `installDebug` 直接安装

### 技巧 3：查看构建输出
1. 点击底部的 **Build** 标签
2. 查看详细的编译信息
3. 点击错误可以直接跳转到代码

### 技巧 4：同时运行多个应用
1. 运行 `juejin-main`
2. 不要停止，切换到 `juejin-lite`
3. 再次点击运行
4. 两个应用同时运行在设备上

## 📚 相关文档

- [ANDROID_STUDIO_RUN_CONFIG.md](ANDROID_STUDIO_RUN_CONFIG.md) - 详细配置指南
- [RUN_APPS_GUIDE.md](RUN_APPS_GUIDE.md) - 命令行运行指南
- [QUICK_RUN_REFERENCE.md](QUICK_RUN_REFERENCE.md) - 快速参考

## ✅ 验证清单

完成以下步骤确认配置成功：

- [ ] Android Studio 已打开项目
- [ ] Gradle 同步成功
- [ ] 运行配置下拉菜单中显示 `juejin-main` 和 `juejin-lite`
- [ ] 可以选择设备
- [ ] 点击运行按钮可以成功启动应用
- [ ] Logcat 显示应用日志

## 🎉 完成！

现在你可以在 Android Studio 中：
- ✅ 快速切换运行配置
- ✅ 一键运行和调试
- ✅ 使用快捷键提高效率
- ✅ 同时运行多个应用

---

**推荐**：将 `juejin-lite` 设为默认配置，因为编译速度快（7秒 vs 38秒），非常适合日常开发！
