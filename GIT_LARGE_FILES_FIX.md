# 🚨 Git 大文件问题修复

## 问题发现

在推送代码时发现仓库包含 4 个巨大的 Java heap dump 文件：
- `java_pid12891.hprof` (6.6GB)
- `java_pid82724.hprof` (6.2GB)
- `java_pid37475.hprof` (6.2GB)
- `java_pid89055.hprof` (5.2GB)

**总计**: ~24GB 的无用文件！

这些是 JVM 崩溃时生成的内存转储文件，不应该提交到 Git。

## ✅ 已完成的修复

### 1. 更新 .gitignore
已添加以下规则：
```gitignore
# Java heap dumps (JVM crash files)
*.hprof
java_pid*.hprof

# iOS DerivedData
DerivedData/
*.xcworkspace/xcuserdata/

# Kotlin Native cache
.konan/

# Android
*.apk
*.aab
*.dex
*.class
bin/
gen/
out/

# Gradle daemon
.gradle/daemon/
```

### 2. 创建清理脚本
`cleanup-git-large-files.sh` - 用于从 Git 历史中移除大文件

## 🔧 修复步骤

### 等待当前推送完成

**重要**: 先让当前的推送完成！不要中断！

### 推送完成后执行清理

```bash
# 1. 运行清理脚本
./cleanup-git-large-files.sh

# 2. 强制推送清理后的历史
git push --force origin feature/monorepo-migration

# 3. 验证文件大小
du -sh .git
```

## 📊 预期效果

清理前:
- 仓库大小: ~24GB+
- 推送时间: 很长

清理后:
- 仓库大小: <100MB
- 推送时间: 几秒钟

## ⚠️ 注意事项

### 1. 强制推送的影响
- 会重写 Git 历史
- 如果有其他协作者，他们需要重新克隆仓库
- 建议在个人分支上操作

### 2. 本地清理
清理 Git 历史后，还需要删除本地的 .hprof 文件：
```bash
# 删除所有 .hprof 文件
find . -name "*.hprof" -type f -delete

# 验证
ls -lh *.hprof 2>/dev/null || echo "✅ 已清理"
```

### 3. 防止再次提交
`.gitignore` 已更新，以后不会再提交这些文件。

## 🎯 推荐操作流程

### 方案 A: 立即清理（推荐）
```bash
# 1. 等待当前推送完成
# 2. 运行清理脚本
./cleanup-git-large-files.sh

# 3. 强制推送
git push --force origin feature/monorepo-migration
```

### 方案 B: 创建新分支
如果不想强制推送，可以创建新分支：
```bash
# 1. 运行清理脚本
./cleanup-git-large-files.sh

# 2. 创建新分支
git checkout -b feature/monorepo-migration-clean

# 3. 推送新分支
git push origin feature/monorepo-migration-clean

# 4. 删除旧分支（在 GitHub 上）
```

## 📝 清理检查清单

- [ ] 当前推送已完成
- [ ] 运行 `cleanup-git-large-files.sh`
- [ ] 检查清理结果（应该没有 .hprof 文件）
- [ ] 删除本地 .hprof 文件
- [ ] 强制推送或创建新分支
- [ ] 验证远程仓库大小
- [ ] 提交 `.gitignore` 更新

## 🔗 相关命令

### 查看 Git 仓库大小
```bash
du -sh .git
```

### 查看最大的文件
```bash
git ls-files | while read file; do
  if [ -f "$file" ]; then
    size=$(du -h "$file" 2>/dev/null | cut -f1)
    echo "$size $file"
  fi
done | sort -rh | head -20
```

### 查找所有 .hprof 文件
```bash
find . -name "*.hprof" -type f -ls
```

### 删除本地 .hprof 文件
```bash
find . -name "*.hprof" -type f -delete
```

## 💡 预防措施

### 1. 定期检查
```bash
# 添加到 git hooks
cat > .git/hooks/pre-commit << 'EOF'
#!/bin/bash
# 检查是否有大文件
large_files=$(git diff --cached --name-only | while read file; do
  if [ -f "$file" ]; then
    size=$(du -m "$file" | cut -f1)
    if [ $size -gt 10 ]; then
      echo "$file ($size MB)"
    fi
  fi
done)

if [ -n "$large_files" ]; then
  echo "⚠️  警告: 发现大文件:"
  echo "$large_files"
  echo ""
  echo "确认要提交这些文件吗? (y/N)"
  read confirm
  if [ "$confirm" != "y" ]; then
    exit 1
  fi
fi
EOF

chmod +x .git/hooks/pre-commit
```

### 2. 使用 Git LFS
对于必须提交的大文件（如设计资源），使用 Git LFS：
```bash
git lfs install
git lfs track "*.psd"
git lfs track "*.ai"
```

## 📚 参考资料

- [Git 大文件处理](https://git-scm.com/book/en/v2/Git-Tools-Rewriting-History)
- [Git LFS](https://git-lfs.github.com/)
- [.gitignore 最佳实践](https://github.com/github/gitignore)

---

**创建时间**: 2026-04-11  
**状态**: ⚠️ 待清理
