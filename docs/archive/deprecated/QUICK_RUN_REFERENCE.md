# 🚀 快速运行参考

## 一键安装（推荐）

```bash
./install-all.sh
```

## 手动安装

### 安装所有应用
```bash
./gradlew installDebug
```

### 安装单个应用
```bash
# 完整版
./gradlew :apps:juejin-main:installDebug

# 轻量版
./gradlew :apps:juejin-lite:installDebug
```

## 启动应用

### 完整版
```bash
adb shell am start -n com.example.juejin/.MainActivity
```

### 轻量版
```bash
adb shell am start -n com.example.juejin.lite/.MainActivity
```

## 验证安装

```bash
adb shell pm list packages | grep juejin
```

应该看到：
```
package:com.example.juejin.lite
package:com.example.juejin
```

## 卸载应用

```bash
# 完整版
adb uninstall com.example.juejin

# 轻量版
adb uninstall com.example.juejin.lite
```

## 查看日志

```bash
# 完整版
adb logcat | grep "com.example.juejin"

# 轻量版
adb logcat | grep "com.example.juejin.lite"
```

## 应用信息

| 应用 | Package ID | 应用名 |
|------|-----------|--------|
| 完整版 | com.example.juejin | 掘金 |
| 轻量版 | com.example.juejin.lite | 掘金轻量版 |

## 常用命令组合

### 重新安装完整版
```bash
adb uninstall com.example.juejin && \
./gradlew :apps:juejin-main:installDebug && \
adb shell am start -n com.example.juejin/.MainActivity
```

### 重新安装轻量版
```bash
adb uninstall com.example.juejin.lite && \
./gradlew :apps:juejin-lite:installDebug && \
adb shell am start -n com.example.juejin.lite/.MainActivity
```

### 清理并重装所有
```bash
adb uninstall com.example.juejin
adb uninstall com.example.juejin.lite
./gradlew clean
./gradlew installDebug
```

---

💡 **提示**：两个应用可以同时安装和运行在同一设备上！
