#!/bin/bash

# KMP Monorepo 设置脚本
# 用于快速创建 Monorepo 目录结构

set -e

echo "🚀 开始设置 KMP Monorepo 结构..."

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 创建目录函数
create_dir() {
    if [ ! -d "$1" ]; then
        mkdir -p "$1"
        echo -e "${GREEN}✓${NC} 创建目录: $1"
    else
        echo -e "${BLUE}→${NC} 目录已存在: $1"
    fi
}

# 创建文件函数
create_file() {
    if [ ! -f "$1" ]; then
        touch "$1"
        echo -e "${GREEN}✓${NC} 创建文件: $1"
    else
        echo -e "${BLUE}→${NC} 文件已存在: $1"
    fi
}

echo ""
echo "📁 创建应用层目录..."
create_dir "apps/juejin-main"
create_dir "apps/juejin-lite"
create_dir "apps/admin-dashboard"

echo ""
echo "📦 创建共享模块 - 核心层..."
create_dir "shared/core/common/src/commonMain/kotlin/com/example/juejin/core/common"
create_dir "shared/core/common/src/androidMain/kotlin/com/example/juejin/core/common"
create_dir "shared/core/common/src/iosMain/kotlin/com/example/juejin/core/common"
create_dir "shared/core/common/src/desktopMain/kotlin/com/example/juejin/core/common"

create_dir "shared/core/network/src/commonMain/kotlin/com/example/juejin/core/network"
create_dir "shared/core/network/src/androidMain/kotlin/com/example/juejin/core/network"
create_dir "shared/core/network/src/iosMain/kotlin/com/example/juejin/core/network"
create_dir "shared/core/network/src/desktopMain/kotlin/com/example/juejin/core/network"

create_dir "shared/core/database/src/commonMain/kotlin/com/example/juejin/core/database"
create_dir "shared/core/database/src/commonMain/sqldelight/com/example/juejin/core/database"
create_dir "shared/core/database/src/androidMain/kotlin/com/example/juejin/core/database"
create_dir "shared/core/database/src/iosMain/kotlin/com/example/juejin/core/database"
create_dir "shared/core/database/src/desktopMain/kotlin/com/example/juejin/core/database"

create_dir "shared/core/storage/src/commonMain/kotlin/com/example/juejin/core/storage"
create_dir "shared/core/storage/src/androidMain/kotlin/com/example/juejin/core/storage"
create_dir "shared/core/storage/src/iosMain/kotlin/com/example/juejin/core/storage"
create_dir "shared/core/storage/src/desktopMain/kotlin/com/example/juejin/core/storage"

echo ""
echo "🎨 创建共享模块 - UI 层..."
create_dir "shared/ui/components/src/commonMain/kotlin/com/example/juejin/ui/components"
create_dir "shared/ui/theme/src/commonMain/kotlin/com/example/juejin/ui/theme"
create_dir "shared/ui/resources/src/commonMain/composeResources"

echo ""
echo "⚡ 创建共享模块 - 功能层..."
create_dir "shared/features/auth/src/commonMain/kotlin/com/example/juejin/feature/auth"
create_dir "shared/features/profile/src/commonMain/kotlin/com/example/juejin/feature/profile"
create_dir "shared/features/article/src/commonMain/kotlin/com/example/juejin/feature/article"
create_dir "shared/features/course/src/commonMain/kotlin/com/example/juejin/feature/course"

echo ""
echo "🏗️ 创建共享模块 - 领域层..."
create_dir "shared/domain/models/src/commonMain/kotlin/com/example/juejin/domain/models"
create_dir "shared/domain/repositories/src/commonMain/kotlin/com/example/juejin/domain/repositories"

echo ""
echo "🔧 创建构建逻辑..."
create_dir "build-logic/convention/src/main/kotlin"

echo ""
echo "📝 创建 build.gradle.kts 文件..."

# 创建各个模块的 build.gradle.kts
modules=(
    "shared/core/common"
    "shared/core/network"
    "shared/core/database"
    "shared/core/storage"
    "shared/ui/components"
    "shared/ui/theme"
    "shared/ui/resources"
    "shared/features/auth"
    "shared/features/profile"
    "shared/features/article"
    "shared/features/course"
    "shared/domain/models"
    "shared/domain/repositories"
)

for module in "${modules[@]}"; do
    create_file "$module/build.gradle.kts"
done

echo ""
echo "📄 创建 README 文件..."
for module in "${modules[@]}"; do
    create_file "$module/README.md"
done

echo ""
echo "✨ Monorepo 结构创建完成！"
echo ""
echo "下一步："
echo "1. 查看 KMP_MONOREPO_GUIDE.md 了解详细的迁移步骤"
echo "2. 开始配置各个模块的 build.gradle.kts"
echo "3. 逐步迁移现有代码到对应的模块"
echo ""
echo "🎉 祝你迁移顺利！"
