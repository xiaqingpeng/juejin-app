# 📱 设备安装总结

## ✅ 安装状态

### 已安装的应用

| 应用 | 状态 | Package ID | 应用名 |
|------|------|-----------|--------|
| **juejin-main** | ✅ 已安装 | com.example.juejin | 掘金 |
| **juejin-lite** | ✅ 已安装 | com.example.juejin.lite | 掘金轻量版 |

### 设备信息
- **设备类型**：Android 模拟器
- **设备 ID**：emulator-5554
- **状态**：已连接

## 🎯 快速操作

### 方式一：使用一键脚本（最简单）
```bash
./install-all.sh
```

这个脚本会自动：
1. ✅ 检查设备连接
2. ✅ 编译两个应用
3. ✅ 安装到设备
4. ✅ 验证安装结果

### 方式二：使用 Gradle 命令
```bash
# 同时安装两个应用
./gradlew installDebug

# 或分别安装
./gradlew :apps:juejin-main:installDebug
./gradlew :apps:juejin-lite:installDebug
```

### 方式三：使用 Android Studio
1. 打开项目
2. 在顶部选择运行配置（juejin-main 或 juejin-lite）
3. 选择设备
4. 点击运行按钮 ▶️

## 🚀 启动应用

### 从命令行启动

```bash
# 启动完整版
adb shell am start -n com.example.juejin/.MainActivity

# 启动轻量版
adb shell am start -n com.example.juejin.lite/.MainActivity
```

### 从设备启动
在应用抽屉中找到并点击：
- **掘金**（完整版）
- **掘金轻量版**

## 📊 应用对比

### 功能对比

| 功能 | juejin-main | juejin-lite |
|------|-------------|-------------|
| 首页 | ✅ | ✅ |
| 热门 | ✅ | ✅ |
| 发现 | ✅ | ❌ |
| 课程 | ✅ | ❌ |
| 我的 | ✅ | ✅ |

### 性能对比

| 指标 | juejin-main | juejin-lite |
|------|-------------|-------------|
| 编译时间 | ~38秒 | ~7秒 |
| APK 大小 | ~15MB | ~10MB |
| 启动速度 | 正常 | 更快 |
| 内存占用 | 正常 | 更低 |

## 🔍 验证安装

### 查看已安装的应用
```bash
adb shell pm list packages | grep juejin
```

**预期输出**：
```
package:com.example.juejin.lite
package:com.example.juejin
```

### 查看应用详情

#### 完整版
```bash
adb shell dumpsys package com.example.juejin | grep -E "versionName|versionCode"
```

#### 轻量版
```bash
adb shell dumpsys package com.example.juejin.lite | grep -E "versionName|versionCode"
```

## 🛠️ 常用操作

### 重新安装

#### 完整版
```bash
adb uninstall com.example.juejin
./gradlew :apps:juejin-main:installDebug
```

#### 轻量版
```bash
adb uninstall com.example.juejin.lite
./gradlew :apps:juejin-lite:installDebug
```

### 清除应用数据

#### 完整版
```bash
adb shell pm clear com.example.juejin
```

#### 轻量版
```bash
adb shell pm clear com.example.juejin.lite
```

### 查看日志

#### 完整版日志
```bash
adb logcat | grep "com.example.juejin"
```

#### 轻量版日志
```bash
adb logcat | grep "com.example.juejin.lite"
```

### 查看崩溃日志
```bash
adb logcat | grep -E "AndroidRuntime|FATAL"
```

## 📁 APK 文件位置

### 完整版
```
apps/juejin-main/build/outputs/apk/debug/juejin-main-debug.apk
```

### 轻量版
```
apps/juejin-lite/build/outputs/apk/debug/juejin-lite-debug.apk
```

## 🎨 同时运行两个应用

由于两个应用有不同的 `applicationId`，它们可以：
- ✅ 同时安装在同一设备上
- ✅ 同时运行
- ✅ 独立的数据存储
- ✅ 独立的设置和状态

### 演示：同时启动两个应用
```bash
# 启动完整版
adb shell am start -n com.example.juejin/.MainActivity

# 等待 2 秒
sleep 2

# 启动轻量版
adb shell am start -n com.example.juejin.lite/.MainActivity
```

## 🐛 故障排查

### 问题 1：设备未连接
```bash
# 重启 ADB 服务
adb kill-server
adb start-server
adb devices
```

### 问题 2：安装失败
```bash
# 清理并重新构建
./gradlew clean
./gradlew assembleDebug
./gradlew installDebug
```

### 问题 3：应用崩溃
```bash
# 查看详细日志
adb logcat -c  # 清除旧日志
adb logcat | grep -E "AndroidRuntime|FATAL|Exception"
```

### 问题 4：签名冲突
```bash
# 完全卸载后重新安装
adb uninstall com.example.juejin
adb uninstall com.example.juejin.lite
./gradlew installDebug
```

## 📝 开发建议

### 1. 快速迭代
使用轻量版进行开发，因为：
- ✅ 编译速度快（7秒 vs 38秒）
- ✅ 安装速度快
- ✅ 启动速度快

### 2. 功能测试
使用完整版进行完整功能测试：
- ✅ 测试所有功能
- ✅ 验证用户体验
- ✅ 性能测试

### 3. 并行开发
两个应用可以同时安装，方便对比：
- ✅ 对比功能差异
- ✅ 对比性能表现
- ✅ 对比用户体验

## 🎯 下一步

### 1. 完善功能
- [ ] 实现网络请求
- [ ] 添加数据加载
- [ ] 完善 UI 交互

### 2. 性能优化
- [ ] 优化启动速度
- [ ] 减小 APK 体积
- [ ] 优化内存使用

### 3. 测试
- [ ] 功能测试
- [ ] 性能测试
- [ ] 兼容性测试

## 📚 相关文档

- [RUN_APPS_GUIDE.md](RUN_APPS_GUIDE.md) - 完整运行指南
- [QUICK_RUN_REFERENCE.md](QUICK_RUN_REFERENCE.md) - 快速参考
- [MULTI_APP_QUICK_REFERENCE.md](MULTI_APP_QUICK_REFERENCE.md) - 多应用架构参考
- [PROJECT_STATUS.md](PROJECT_STATUS.md) - 项目状态

## 🎉 总结

✅ **两个应用都已成功安装到设备上！**

你现在可以：
1. 在设备上同时运行两个应用
2. 对比完整版和轻量版的差异
3. 进行功能开发和测试
4. 随时使用 `./install-all.sh` 快速重装

---

**设备状态**：🟢 已连接  
**应用状态**：✅ 已安装  
**准备就绪**：🚀 可以开始开发
