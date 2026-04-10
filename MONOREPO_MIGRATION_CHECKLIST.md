# Monorepo 迁移检查清单

使用此清单跟踪迁移进度。完成每一项后，将 `[ ]` 改为 `[x]`。

## 阶段 1: 准备工作 (预计 1-2 天)

### 备份和分支管理
- [ ] 创建备份分支 `backup/before-monorepo`
- [ ] 创建开发分支 `feature/monorepo-migration`
- [ ] 通知团队成员迁移计划

### 目录结构
- [ ] 运行 `./scripts/setup-monorepo.sh` 创建基础目录
- [ ] 验证目录结构正确
- [ ] 创建 `.gitkeep` 文件保持空目录

## 阶段 2: 共享模块创建 (预计 3-5 天)

### Core 模块

#### shared/core/common
- [ ] 创建 build.gradle.kts
- [ ] 迁移 Logger
- [ ] 迁移 DateTimeUtil
- [ ] 迁移扩展函数
- [ ] 迁移通用工具类
- [ ] 编写单元测试
- [ ] 更新 README.md

#### shared/core/network
- [ ] 创建 build.gradle.kts
- [ ] 配置 Ktor 客户端
- [ ] 迁移 API 接口定义
- [ ] 迁移请求/响应模型
- [ ] 实现错误处理
- [ ] 编写单元测试
- [ ] 更新 README.md

#### shared/core/database
- [ ] 创建 build.gradle.kts
- [ ] 配置 SQLDelight
- [ ] 迁移数据库表定义
- [ ] 迁移 DatabaseDriverFactory
- [ ] 迁移 Repository 实现
- [ ] 编写单元测试
- [ ] 更新 README.md

#### shared/core/storage
- [ ] 创建 build.gradle.kts
- [ ] 迁移 PrivacyStorage
- [ ] 迁移 PreferencesManager
- [ ] 实现平台特定存储
- [ ] 编写单元测试
- [ ] 更新 README.md

### UI 模块

#### shared/ui/components
- [ ] 创建 build.gradle.kts
- [ ] 迁移 TopNavigationBar
- [ ] 迁移 BottomNavigationBar
- [ ] 迁移对话框组件
- [ ] 迁移加载指示器
- [ ] 迁移其他通用组件
- [ ] 编写 UI 测试
- [ ] 更新 README.md

#### shared/ui/theme
- [ ] 创建 build.gradle.kts
- [ ] 迁移 ThemeManager
- [ ] 迁移 ThemeColors
- [ ] 迁移 Typography
- [ ] 迁移主题相关工具
- [ ] 编写单元测试
- [ ] 更新 README.md

#### shared/ui/resources
- [ ] 创建 build.gradle.kts
- [ ] 迁移图片资源
- [ ] 迁移字符串资源
- [ ] 迁移颜色资源
- [ ] 组织资源文件
- [ ] 更新 README.md

### Feature 模块

#### shared/features/auth
- [ ] 创建 build.gradle.kts
- [ ] 迁移登录功能
- [ ] 迁移注册功能
- [ ] 迁移认证状态管理
- [ ] 迁移 ViewModel
- [ ] 编写单元测试
- [ ] 更新 README.md

#### shared/features/profile
- [ ] 创建 build.gradle.kts
- [ ] 迁移个人资料页面
- [ ] 迁移编辑资料功能
- [ ] 迁移设置功能
- [ ] 迁移 ViewModel
- [ ] 编写单元测试
- [ ] 更新 README.md

#### shared/features/article
- [ ] 创建 build.gradle.kts
- [ ] 迁移文章列表
- [ ] 迁移文章详情
- [ ] 迁移文章搜索
- [ ] 迁移 ViewModel
- [ ] 编写单元测试
- [ ] 更新 README.md

#### shared/features/course
- [ ] 创建 build.gradle.kts
- [ ] 迁移课程列表
- [ ] 迁移课程详情
- [ ] 迁移课程学习
- [ ] 迁移 ViewModel
- [ ] 编写单元测试
- [ ] 更新 README.md

### Domain 模块

#### shared/domain/models
- [ ] 创建 build.gradle.kts
- [ ] 迁移 User 模型
- [ ] 迁移 Article 模型
- [ ] 迁移 Course 模型
- [ ] 迁移其他数据模型
- [ ] 更新 README.md

#### shared/domain/repositories
- [ ] 创建 build.gradle.kts
- [ ] 定义 Repository 接口
- [ ] 迁移 Repository 实现
- [ ] 编写单元测试
- [ ] 更新 README.md

## 阶段 3: 应用层迁移 (预计 2-3 天)

### apps/juejin-main (主应用)
- [ ] 移动 composeApp 到 apps/juejin-main
- [ ] 移动 iosApp 到 apps/juejin-main/iosApp
- [ ] 更新 build.gradle.kts
- [ ] 更新依赖引用
- [ ] 更新 iOS 项目配置
- [ ] 测试 Android 编译
- [ ] 测试 iOS 编译
- [ ] 测试 Desktop 编译
- [ ] 测试应用运行

### apps/juejin-lite (轻量版)
- [ ] 创建 build.gradle.kts
- [ ] 创建 Android 应用入口
- [ ] 创建 iOS 应用入口
- [ ] 实现简化的功能集
- [ ] 测试编译
- [ ] 测试运行

### apps/admin-dashboard (管理后台)
- [ ] 创建 build.gradle.kts
- [ ] 创建 Desktop 应用入口
- [ ] 实现管理功能
- [ ] 测试编译
- [ ] 测试运行

## 阶段 4: 配置和优化 (预计 1-2 天)

### 构建配置
- [ ] 更新 settings.gradle.kts
- [ ] 配置 Version Catalog
- [ ] 创建构建约定插件
- [ ] 配置代码检查工具
- [ ] 配置 CI/CD

### 文档
- [ ] 更新主 README.md
- [ ] 为每个模块创建 README.md
- [ ] 创建架构文档
- [ ] 创建开发指南
- [ ] 创建贡献指南

### 测试
- [ ] 运行所有单元测试
- [ ] 运行集成测试
- [ ] 测试所有应用
- [ ] 性能测试
- [ ] 修复发现的问题

## 阶段 5: 发布和部署 (预计 1 天)

### 代码审查
- [ ] 创建 Pull Request
- [ ] 团队代码审查
- [ ] 处理审查意见
- [ ] 获得批准

### 合并和发布
- [ ] 合并到主分支
- [ ] 创建发布标签
- [ ] 更新 CHANGELOG
- [ ] 通知团队

### 后续工作
- [ ] 监控应用稳定性
- [ ] 收集团队反馈
- [ ] 优化构建时间
- [ ] 持续改进架构

## 风险和注意事项

### 高风险项
- [ ] 数据库迁移可能导致数据丢失
- [ ] 依赖关系复杂可能导致循环依赖
- [ ] iOS 框架导出可能遇到问题
- [ ] 构建时间可能显著增加

### 缓解措施
- [ ] 在迁移前完整备份数据库
- [ ] 使用依赖图工具检查循环依赖
- [ ] 提前测试 iOS 框架导出
- [ ] 使用 Gradle 构建缓存优化构建时间

## 回滚计划

如果迁移失败，执行以下步骤：

1. [ ] 切换回备份分支
2. [ ] 恢复原有配置
3. [ ] 通知团队
4. [ ] 分析失败原因
5. [ ] 制定新的迁移计划

## 成功标准

迁移成功的标准：

- [ ] 所有应用可以正常编译
- [ ] 所有应用可以正常运行
- [ ] 所有测试通过
- [ ] 构建时间在可接受范围内
- [ ] 团队成员理解新架构
- [ ] 文档完整且准确

## 时间估算

- 阶段 1: 1-2 天
- 阶段 2: 3-5 天
- 阶段 3: 2-3 天
- 阶段 4: 1-2 天
- 阶段 5: 1 天

**总计: 8-13 天**

## 团队分工建议

- **架构师**: 设计模块结构，审查代码
- **后端开发**: 迁移 core 模块
- **前端开发**: 迁移 UI 和 feature 模块
- **移动开发**: 配置 iOS/Android 应用
- **DevOps**: 配置 CI/CD

## 每日站会议题

1. 昨天完成了什么？
2. 今天计划做什么？
3. 遇到了什么阻碍？
4. 需要什么帮助？

## 里程碑

- [ ] **里程碑 1**: 完成目录结构创建
- [ ] **里程碑 2**: 完成第一个共享模块
- [ ] **里程碑 3**: 完成所有共享模块
- [ ] **里程碑 4**: 主应用迁移完成
- [ ] **里程碑 5**: 第二个应用创建完成
- [ ] **里程碑 6**: 所有测试通过
- [ ] **里程碑 7**: 代码审查完成
- [ ] **里程碑 8**: 合并到主分支

---

**开始日期**: ___________
**预计完成日期**: ___________
**实际完成日期**: ___________

**项目负责人**: ___________
**团队成员**: ___________
