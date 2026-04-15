# 运行应用到设备指南

## 📱 已安装的应用

✅ **juejin-main**（完整版）
- Package: `com.example.juejin`
- 应用名：掘金

✅ **juejin-lite**（轻量版）
- Package: `com.example.juejin.lite`
- 应用名：掘金轻量版

## 🚀 快速安装

### 方法一：使用 Gradle 命令（推荐）

#### 1. 检查连接的设备
```bash
adb devices
```

应该看到类似输出：
```
List of devices attached
emulator-5554   device
```

#### 2. 安装完整版
```bash
./gradlew :apps:juejin-main:installDebug
```

#### 3. 安装轻量版
```bash
./gradlew :apps:juejin-lite:installDebug
```

#### 4. 同时安装两个应用
```bash
./gradlew installDebug
```

### 方法二：使用 Android Studio

#### 1. 打开 Android Studio
- 打开项目根目录

#### 2. 选择运行配置
- 在顶部工具栏找到运行配置下拉菜单
- 选择 `apps.juejin-main` 或 `apps.juejin-lite`

#### 3. 选择设备
- 点击设备选择器
- 选择连接的设备或模拟器

#### 4. 运行
- 点击绿色的运行按钮 ▶️
- 或按快捷键 `Shift + F10`

## 🎯 启动应用

### 从命令行启动

#### 启动完整版
```bash
adb shell am start -n com.example.juejin/.MainActivity
```

#### 启动轻量版
```bash
adb shell am start -n com.example.juejin.lite/.MainActivity
```

### 从设备启动
- 在应用抽屉中找到"掘金"或"掘金轻量版"
- 点击图标启动

## 🔍 验证安装

### 查看已安装的应用
```bash
adb shell pm list packages | grep juejin
```

应该看到：
```
package:com.example.juejin.lite
package:com.example.juejin
```

### 查看应用详情

#### 完整版详情
```bash
adb shell dumpsys package com.example.juejin | grep -A 5 "versionName"
```

#### 轻量版详情
```bash
adb shell dumpsys package com.example.juejin.lite | grep -A 5 "versionName"
```

## 📊 应用对比

| 特性 | juejin-main | juejin-lite |
|------|-------------|-------------|
| Package ID | com.example.juejin | com.example.juejin.lite |
| 应用名 | 掘金 | 掘金轻量版 |
| 功能 | 完整（5个标签） | 核心（3个标签） |
| APK 大小 | ~15MB | ~10MB |
| 启动速度 | 正常 | 更快 |

## 🛠️ 调试命令

### 查看日志

#### 完整版日志
```bash
adb logcat | grep "com.example.juejin"
```

#### 轻量版日志
```bash
adb logcat | grep "com.example.juejin.lite"
```

### 清除应用数据

#### 清除完整版数据
```bash
adb shell pm clear com.example.juejin
```

#### 清除轻量版数据
```bash
adb shell pm clear com.example.juejin.lite
```

### 卸载应用

#### 卸载完整版
```bash
adb uninstall com.example.juejin
# 或
./gradlew :apps:juejin-main:uninstallDebug
```

#### 卸载轻量版
```bash
adb uninstall com.example.juejin.lite
# 或
./gradlew :apps:juejin-lite:uninstallDebug
```

## 🔄 重新安装

### 完整流程

#### 1. 卸载旧版本
```bash
adb uninstall com.example.juejin
adb uninstall com.example.juejin.lite
```

#### 2. 清理构建
```bash
./gradlew clean
```

#### 3. 重新编译和安装
```bash
./gradlew installDebug
```

### 快速重装（保留数据）
```bash
# 完整版
./gradlew :apps:juejin-main:installDebug

# 轻量版
./gradlew :apps:juejin-lite:installDebug
```

## 📱 多设备安装

### 查看所有连接的设备
```bash
adb devices -l
```

### 安装到特定设备

#### 获取设备 ID
```bash
adb devices
```

输出示例：
```
List of devices attached
emulator-5554   device
192.168.1.100:5555   device
```

#### 安装到指定设备
```bash
# 安装到模拟器
adb -s emulator-5554 install apps/juejin-main/build/outputs/apk/debug/juejin-main-debug.apk

# 安装到真机
adb -s 192.168.1.100:5555 install apps/juejin-lite/build/outputs/apk/debug/juejin-lite-debug.apk
```

## 🎨 Android Studio 运行配置

### 创建运行配置

#### 1. 打开运行配置
- 点击顶部工具栏的运行配置下拉菜单
- 选择 "Edit Configurations..."

#### 2. 添加新配置
- 点击左上角的 "+" 按钮
- 选择 "Android App"

#### 3. 配置完整版
- Name: `juejin-main`
- Module: `juejin-app.apps.juejin-main.main`
- Launch: Default Activity

#### 4. 配置轻量版
- Name: `juejin-lite`
- Module: `juejin-app.apps.juejin-lite.main`
- Launch: Default Activity

#### 5. 保存并运行
- 点击 "Apply" 和 "OK"
- 在下拉菜单中选择配置
- 点击运行按钮

## 🐛 常见问题

### 问题 1：设备未连接
```bash
# 检查 ADB 服务
adb kill-server
adb start-server
adb devices
```

### 问题 2：安装失败
```bash
# 清理并重新构建
./gradlew clean
./gradlew :apps:juejin-main:assembleDebug
./gradlew :apps:juejin-main:installDebug
```

### 问题 3：应用崩溃
```bash
# 查看崩溃日志
adb logcat | grep -E "AndroidRuntime|FATAL"
```

### 问题 4：权限问题
```bash
# 授予所有权限
adb shell pm grant com.example.juejin android.permission.INTERNET
adb shell pm grant com.example.juejin.lite android.permission.INTERNET
```

### 问题 5：签名冲突
```bash
# 卸载旧版本
adb uninstall com.example.juejin
adb uninstall com.example.juejin.lite

# 重新安装
./gradlew installDebug
```

## 📦 APK 位置

### 构建输出目录

#### 完整版 APK
```
apps/juejin-main/build/outputs/apk/debug/juejin-main-debug.apk
```

#### 轻量版 APK
```
apps/juejin-lite/build/outputs/apk/debug/juejin-lite-debug.apk
```

### 手动安装 APK
```bash
# 完整版
adb install apps/juejin-main/build/outputs/apk/debug/juejin-main-debug.apk

# 轻量版
adb install apps/juejin-lite/build/outputs/apk/debug/juejin-lite-debug.apk
```

## 🚀 一键脚本

### 创建安装脚本

创建 `install-all.sh`：
```bash
#!/bin/bash

echo "=== 检查设备连接 ==="
adb devices

echo -e "\n=== 编译应用 ==="
./gradlew assembleDebug --no-daemon

echo -e "\n=== 安装完整版 ==="
./gradlew :apps:juejin-main:installDebug

echo -e "\n=== 安装轻量版 ==="
./gradlew :apps:juejin-lite:installDebug

echo -e "\n=== 验证安装 ==="
adb shell pm list packages | grep juejin

echo -e "\n✅ 安装完成！"
echo "完整版: com.example.juejin"
echo "轻量版: com.example.juejin.lite"
```

### 使用脚本
```bash
chmod +x install-all.sh
./install-all.sh
```

## 📱 真机调试

### 启用 USB 调试

#### Android 设备
1. 打开"设置" → "关于手机"
2. 连续点击"版本号" 7次，启用开发者选项
3. 返回"设置" → "开发者选项"
4. 启用"USB 调试"

#### 连接设备
1. 使用 USB 线连接设备和电脑
2. 在设备上允许 USB 调试
3. 运行 `adb devices` 验证连接

### 无线调试（Android 11+）

#### 1. 启用无线调试
- 设置 → 开发者选项 → 无线调试

#### 2. 配对设备
```bash
adb pair <IP>:<PORT>
# 输入配对码
```

#### 3. 连接设备
```bash
adb connect <IP>:<PORT>
```

#### 4. 安装应用
```bash
./gradlew installDebug
```

## 🎯 性能测试

### 启动时间测试

#### 完整版
```bash
adb shell am start -W -n com.example.juejin/.MainActivity
```

#### 轻量版
```bash
adb shell am start -W -n com.example.juejin.lite/.MainActivity
```

### 内存使用
```bash
# 完整版
adb shell dumpsys meminfo com.example.juejin

# 轻量版
adb shell dumpsys meminfo com.example.juejin.lite
```

### APK 大小对比
```bash
ls -lh apps/juejin-main/build/outputs/apk/debug/juejin-main-debug.apk
ls -lh apps/juejin-lite/build/outputs/apk/debug/juejin-lite-debug.apk
```

## 📝 总结

### 快速命令参考

```bash
# 安装所有应用
./gradlew installDebug

# 安装完整版
./gradlew :apps:juejin-main:installDebug

# 安装轻量版
./gradlew :apps:juejin-lite:installDebug

# 启动完整版
adb shell am start -n com.example.juejin/.MainActivity

# 启动轻量版
adb shell am start -n com.example.juejin.lite/.MainActivity

# 查看已安装应用
adb shell pm list packages | grep juejin

# 卸载所有
adb uninstall com.example.juejin
adb uninstall com.example.juejin.lite
```

---

**提示**：两个应用可以同时安装在同一设备上，因为它们有不同的 `applicationId`。
