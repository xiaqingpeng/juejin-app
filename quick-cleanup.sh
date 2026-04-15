#!/bin/bash

echo "🧹 快速清理本地大文件"
echo "================================"
echo ""

# 1. 删除 .hprof 文件
echo "1️⃣ 删除 .hprof 文件..."
hprof_count=$(find . -name "*.hprof" -type f | wc -l | tr -d ' ')
if [ "$hprof_count" -gt 0 ]; then
  find . -name "*.hprof" -type f -delete
  echo "   ✅ 已删除 $hprof_count 个 .hprof 文件"
else
  echo "   ℹ️  没有找到 .hprof 文件"
fi

# 2. 清理 build 目录
echo ""
echo "2️⃣ 清理 build 目录..."
find . -type d -name "build" -not -path "*/node_modules/*" -exec rm -rf {} + 2>/dev/null
echo "   ✅ 已清理 build 目录"

# 3. 清理 .gradle 缓存
echo ""
echo "3️⃣ 清理 .gradle 缓存..."
if [ -d ".gradle" ]; then
  rm -rf .gradle
  echo "   ✅ 已清理 .gradle"
fi

# 4. 清理 .kotlin 缓存
echo ""
echo "4️⃣ 清理 .kotlin 缓存..."
if [ -d ".kotlin" ]; then
  rm -rf .kotlin
  echo "   ✅ 已清理 .kotlin"
fi

# 5. 清理 iOS DerivedData
echo ""
echo "5️⃣ 清理 iOS DerivedData..."
if [ -d ~/Library/Developer/Xcode/DerivedData ]; then
  rm -rf ~/Library/Developer/Xcode/DerivedData
  echo "   ✅ 已清理 DerivedData"
fi

# 6. 显示当前目录大小
echo ""
echo "📊 当前目录大小:"
du -sh . 2>/dev/null

echo ""
echo "✅ 清理完成！"
echo ""
echo "💡 提示:"
echo "- 这些文件已添加到 .gitignore"
echo "- 下次构建时会重新生成"
echo "- 不会影响 Git 历史"
