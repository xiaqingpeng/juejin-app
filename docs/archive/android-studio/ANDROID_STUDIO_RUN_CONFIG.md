# Android Studio 运行配置指南

## 🎯 目标

在 Android Studio 中添加 `juejin-lite` 运行配置，方便调试和运行。

## 📋 方法一：自动检测（推荐）

### 步骤 1：同步项目
1. 打开 Android Studio
2. 点击顶部工具栏的 **Sync Project with Gradle Files** 按钮（🐘图标）
3. 等待同步完成

### 步骤 2：查看运行配置
1. 点击顶部工具栏的运行配置下拉菜单（通常显示当前配置名称）
2. 你应该能看到：
   - `apps.juejin-main.main`
   - `apps.juejin-lite.main`

### 步骤 3：选择并运行
1. 从下拉菜单选择 `apps.juejin-lite.main`
2. 选择目标设备（模拟器或真机）
3. 点击绿色运行按钮 ▶️ 或按 `Shift + F10`

## 📋 方法二：手动创建配置

如果自动检测没有生成配置，可以手动创建：

### 步骤 1：打开运行配置编辑器
1. 点击顶部工具栏的运行配置下拉菜单
2. 选择 **Edit Configurations...**
3. 或使用快捷键：
   - macOS: `Cmd + ,` 然后选择 Run/Debug Configurations
   - Windows/Linux: `Ctrl + Alt + S` 然后选择 Run/Debug Configurations

### 步骤 2：添加新的 Android App 配置

#### 2.1 创建 juejin-main 配置
1. 点击左上角的 **+** 按钮
2. 选择 **Android App**
3. 配置如下：
   ```
   Name: juejin-main
   Module: juejin-app.apps.juejin-main.main
   Launch: Default Activity
   ```
4. 点击 **Apply**

#### 2.2 创建 juejin-lite 配置
1. 再次点击左上角的 **+** 按钮
2. 选择 **Android App**
3. 配置如下：
   ```
   Name: juejin-lite
   Module: juejin-app.apps.juejin-lite.main
   Launch: Default Activity
   ```
4. 点击 **Apply**

### 步骤 3：保存并使用
1. 点击 **OK** 关闭配置窗口
2. 在运行配置下拉菜单中选择 `juejin-lite`
3. 点击运行按钮 ▶️

## 🎨 详细配置选项

### 基础配置

| 选项 | juejin-main | juejin-lite |
|------|-------------|-------------|
| **Name** | juejin-main | juejin-lite |
| **Module** | juejin-app.apps.juejin-main.main | juejin-app.apps.juejin-lite.main |
| **Launch** | Default Activity | Default Activity |

### 高级配置（可选）

#### Installation Options
```
Deploy: Default APK
Install Flags: (留空)
```

#### Launch Options
```
Launch: Default Activity
Activity: (自动检测)
Launch Flags: (留空)
```

#### Deployment Target Options
```
Target: Open Select Deployment Target Dialog
Use same device for future launches: ✓（可选）
```

#### Debugger
```
Debug type: Auto
```

#### Profiling
```
Enable advanced profiling: ✓（可选，用于性能分析）
```

## 🔧 配置文件位置

Android Studio 会在以下位置保存运行配置：

```
.idea/runConfigurations/
├── juejin_main.xml
└── juejin_lite.xml
```

### 示例配置文件

#### juejin-main.xml
```xml
<component name="ProjectRunConfigurationManager">
  <configuration default="false" name="juejin-main" type="AndroidRunConfigurationType" factoryName="Android App">
    <module name="juejin-app.apps.juejin-main.main" />
    <option name="DEPLOY" value="true" />
    <option name="DEPLOY_APK_FROM_BUNDLE" value="false" />
    <option name="ARTIFACT_NAME" value="" />
    <option name="PM_INSTALL_OPTIONS" value="" />
    <option name="ALL_USERS" value="false" />
    <option name="ALWAYS_INSTALL_WITH_PM" value="false" />
    <option name="CLEAR_APP_STORAGE" value="false" />
    <option name="ACTIVITY_EXTRA_FLAGS" value="" />
    <option name="MODE" value="default_activity" />
    <option name="CLEAR_LOGCAT" value="false" />
    <option name="SHOW_LOGCAT_AUTOMATICALLY" value="false" />
    <option name="INSPECTION_WITHOUT_ACTIVITY_RESTART" value="false" />
    <option name="TARGET_SELECTION_MODE" value="SHOW_DIALOG" />
    <option name="DEBUGGER_TYPE" value="Auto" />
    <method v="2">
      <option name="Android.Gradle.BeforeRunTask" enabled="true" />
    </method>
  </configuration>
</component>
```

#### juejin-lite.xml
```xml
<component name="ProjectRunConfigurationManager">
  <configuration default="false" name="juejin-lite" type="AndroidRunConfigurationType" factoryName="Android App">
    <module name="juejin-app.apps.juejin-lite.main" />
    <option name="DEPLOY" value="true" />
    <option name="DEPLOY_APK_FROM_BUNDLE" value="false" />
    <option name="ARTIFACT_NAME" value="" />
    <option name="PM_INSTALL_OPTIONS" value="" />
    <option name="ALL_USERS" value="false" />
    <option name="ALWAYS_INSTALL_WITH_PM" value="false" />
    <option name="CLEAR_APP_STORAGE" value="false" />
    <option name="ACTIVITY_EXTRA_FLAGS" value="" />
    <option name="MODE" value="default_activity" />
    <option name="CLEAR_LOGCAT" value="false" />
    <option name="SHOW_LOGCAT_AUTOMATICALLY" value="false" />
    <option name="INSPECTION_WITHOUT_ACTIVITY_RESTART" value="false" />
    <option name="TARGET_SELECTION_MODE" value="SHOW_DIALOG" />
    <option name="DEBUGGER_TYPE" value="Auto" />
    <method v="2">
      <option name="Android.Gradle.BeforeRunTask" enabled="true" />
    </method>
  </configuration>
</component>
```

## 🚀 快捷键

### 运行相关
| 操作 | macOS | Windows/Linux |
|------|-------|---------------|
| 运行 | `Ctrl + R` | `Shift + F10` |
| 调试 | `Ctrl + D` | `Shift + F9` |
| 停止 | `Cmd + F2` | `Ctrl + F2` |
| 选择配置 | `Ctrl + Alt + R` | `Alt + Shift + F10` |

### 编辑配置
| 操作 | macOS | Windows/Linux |
|------|-------|---------------|
| 编辑配置 | `Cmd + ,` | `Ctrl + Alt + S` |

## 🎯 使用场景

### 场景 1：快速开发（使用 juejin-lite）
1. 选择 `juejin-lite` 配置
2. 点击运行 ▶️
3. 编译时间：~7秒
4. 适合快速迭代和测试

### 场景 2：完整测试（使用 juejin-main）
1. 选择 `juejin-main` 配置
2. 点击运行 ▶️
3. 编译时间：~38秒
4. 适合完整功能测试

### 场景 3：同时调试两个应用
1. 运行 `juejin-main`
2. 不要停止，再运行 `juejin-lite`
3. 两个应用同时运行在设备上
4. 可以对比功能和性能

## 🐛 调试配置

### 启用调试
1. 选择配置（juejin-main 或 juejin-lite）
2. 点击调试按钮 🐛 或按 `Shift + F9`
3. 设置断点进行调试

### 调试选项
```
Debugger:
  Debug type: Auto
  Symbol Directories: (自动)
  LLDB Startup Commands: (留空)
  LLDB Post Attach Commands: (留空)
```

### 性能分析
```
Profiling:
  ✓ Enable advanced profiling
  
可以分析：
  - CPU 使用率
  - 内存使用
  - 网络活动
  - 能耗
```

## 📊 运行配置对比

| 特性 | juejin-main | juejin-lite |
|------|-------------|-------------|
| 模块 | apps.juejin-main | apps.juejin-lite |
| Package ID | com.example.juejin | com.example.juejin.lite |
| 编译时间 | ~38秒 | ~7秒 |
| 功能 | 完整（5个标签） | 核心（3个标签） |
| 适用场景 | 完整测试 | 快速开发 |

## 🔍 故障排查

### 问题 1：找不到模块
**症状**：Module 下拉菜单中没有 juejin-lite

**解决方案**：
1. 点击 **File** → **Sync Project with Gradle Files**
2. 等待同步完成
3. 重新打开运行配置编辑器

### 问题 2：运行失败
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
1. 检查设备连接：`adb devices`
2. 重启 ADB：`adb kill-server && adb start-server`
3. 在 Android Studio 中刷新设备列表

### 问题 4：安装失败
**症状**：INSTALL_FAILED_*

**解决方案**：
```bash
# 卸载旧版本
adb uninstall com.example.juejin.lite

# 重新运行
```

## 💡 高级技巧

### 技巧 1：创建运行配置组
1. 在运行配置编辑器中
2. 点击 **+** → **Compound**
3. 命名为 "All Apps"
4. 添加 juejin-main 和 juejin-lite
5. 可以一键运行所有应用

### 技巧 2：自定义启动参数
在运行配置中添加：
```
Launch Flags:
  --es extra_key extra_value
  --ez debug_mode true
```

### 技巧 3：使用 Gradle 任务
1. 打开 Gradle 面板（右侧）
2. 展开 `apps` → `juejin-lite` → `Tasks` → `install`
3. 双击 `installDebug`

### 技巧 4：快速切换配置
1. 使用快捷键 `Ctrl + Alt + R`（macOS）或 `Alt + Shift + F10`（Windows/Linux）
2. 输入配置名称的首字母快速筛选
3. 按 Enter 运行

## 📝 配置模板

### 开发配置（juejin-lite）
```
Name: juejin-lite [DEV]
Module: juejin-app.apps.juejin-lite.main
Launch: Default Activity
Target: USB Device
Clear app data: ✓
Show logcat: ✓
```

### 测试配置（juejin-main）
```
Name: juejin-main [TEST]
Module: juejin-app.apps.juejin-main.main
Launch: Default Activity
Target: Emulator
Clear app data: ✓
Enable profiling: ✓
```

### 发布配置
```
Name: juejin-main [RELEASE]
Module: juejin-app.apps.juejin-main.main
Build Variant: release
Launch: Default Activity
```

## 🎓 最佳实践

### 1. 命名规范
- 使用清晰的名称：`juejin-main`, `juejin-lite`
- 添加环境标识：`[DEV]`, `[TEST]`, `[PROD]`

### 2. 开发流程
- 日常开发：使用 `juejin-lite`（快速）
- 功能测试：使用 `juejin-main`（完整）
- 发布前：测试两个配置

### 3. 调试技巧
- 设置有意义的断点
- 使用条件断点
- 利用 Logcat 过滤

### 4. 性能优化
- 启用 Instant Run（如果可用）
- 使用增量编译
- 关闭不必要的插件

## 📚 相关文档

- [RUN_APPS_GUIDE.md](RUN_APPS_GUIDE.md) - 完整运行指南
- [QUICK_RUN_REFERENCE.md](QUICK_RUN_REFERENCE.md) - 快速参考
- [DEVICE_INSTALLATION_SUMMARY.md](DEVICE_INSTALLATION_SUMMARY.md) - 安装总结

## 🎉 总结

现在你可以在 Android Studio 中：
- ✅ 快速切换运行配置
- ✅ 一键运行和调试
- ✅ 同时运行多个应用
- ✅ 使用快捷键提高效率

---

**提示**：建议将 `juejin-lite` 设为默认配置，因为编译速度快，适合日常开发！
