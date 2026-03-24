# iOS 版本号修复指南

## 问题
iOS 应用显示版本号为 "v1.0." 而不是 "v1.0.0"，说明版本信息读取不完整。

## 原因
在 iOS 项目中，版本号可以在两个地方设置：
1. `Info.plist` 文件
2. Xcode 项目设置（会覆盖 Info.plist）

## 解决方案

### 方式 1: 在 Xcode 中设置（推荐）

1. 打开 Xcode 项目：
   ```bash
   open iosApp/iosApp.xcodeproj
   ```

2. 选择项目导航器中的 `iosApp` 项目

3. 选择 `iosApp` target

4. 在 "General" 标签页中找到 "Identity" 部分

5. 设置版本号：
   - **Version**: `1.0.0` (对应 CFBundleShortVersionString)
   - **Build**: `1` (对应 CFBundleVersion)

6. 重新构建并运行应用

### 方式 2: 修改 project.pbxproj 文件

在 `iosApp/iosApp.xcodeproj/project.pbxproj` 文件中添加：

```
MARKETING_VERSION = 1.0.0;
CURRENT_PROJECT_VERSION = 1;
```

查找 `buildSettings` 部分，添加这两行配置。

### 方式 3: 确保 Info.plist 生效

1. 检查 `iosApp/iosApp/Info.plist` 是否包含：
   ```xml
   <key>CFBundleShortVersionString</key>
   <string>1.0.0</string>
   <key>CFBundleVersion</key>
   <string>1</string>
   ```

2. 在 Xcode 项目设置中，确保没有覆盖这些值

3. 清理构建缓存：
   ```bash
   rm -rf ~/Library/Developer/Xcode/DerivedData/iosApp-*
   ```

4. 重新构建

## 验证

运行应用后，在设置页面应该显示：
```
当前版本: v1.0.0 (Build-1)
```

## 调试

如果问题仍然存在，查看控制台日志：
```
[iOS AppVersion] versionName: xxx, buildNumber: xxx
```

这会显示实际读取到的值。

## 常见问题

### Q: 为什么显示 "v1.0." 而不是 "v1.0.0"？
A: 可能是 `CFBundleShortVersionString` 的值为空字符串或只有 "1.0"，需要在 Xcode 中正确设置。

### Q: 修改 Info.plist 后没有生效？
A: Xcode 项目设置会覆盖 Info.plist 的值，需要在 Xcode 中设置。

### Q: 如何自动化版本号管理？
A: 可以使用 Xcode 的 Build Phase 脚本或 fastlane 来自动更新版本号。

## 推荐工作流

1. 在 Xcode 中设置版本号（一次性）
2. 使用 Git 标签管理版本
3. 在 CI/CD 中自动更新版本号

## 相关文件

- `iosApp/iosApp/Info.plist` - Info.plist 配置
- `iosApp/iosApp.xcodeproj/project.pbxproj` - Xcode 项目配置
- `composeApp/src/iosMain/kotlin/com/example/juejin/platform/AppVersion.ios.kt` - 版本读取代码

---

最后更新: 2024-03-24
