#!/bin/bash

echo "🧹 清理 Git 中的大文件"
echo "================================"
echo ""
echo "⚠️  警告: 这将重写 Git 历史！"
echo "⚠️  如果已经推送到远程，需要强制推送！"
echo ""
read -p "确认继续? (y/N): " confirm

if [ "$confirm" != "y" ] && [ "$confirm" != "Y" ]; then
    echo "已取消"
    exit 0
fi

echo ""
echo "1️⃣ 从 Git 历史中移除 .hprof 文件..."
git filter-branch --force --index-filter \
  'git rm --cached --ignore-unmatch *.hprof java_pid*.hprof' \
  --prune-empty --tag-name-filter cat -- --all

echo ""
echo "2️⃣ 清理引用..."
rm -rf .git/refs/original/
git reflog expire --expire=now --all
git gc --prune=now --aggressive

echo ""
echo "3️⃣ 检查剩余大文件..."
git ls-files | while read file; do
  if [ -f "$file" ]; then
    size=$(du -h "$file" 2>/dev/null | cut -f1)
    echo "$size $file"
  fi
done | sort -rh | head -10

echo ""
echo "✅ 清理完成！"
echo ""
echo "📝 下一步:"
echo "1. 检查上面的文件列表，确认大文件已移除"
echo "2. 如果已推送到远程，需要强制推送:"
echo "   git push --force --all"
echo "   git push --force --tags"
echo ""
echo "⚠️  注意: 强制推送会影响其他协作者！"
